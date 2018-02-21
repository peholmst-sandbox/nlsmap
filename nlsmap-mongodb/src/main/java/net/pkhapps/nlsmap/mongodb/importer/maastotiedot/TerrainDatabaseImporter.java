package net.pkhapps.nlsmap.mongodb.importer.maastotiedot;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import net.pkhapps.nlsmap.mongodb.MongoConstants;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.xml.AppSchemaConfiguration;
import org.geotools.xml.PullParser;
import org.geotools.xml.resolver.SchemaCache;
import org.geotools.xml.resolver.SchemaResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Application for importing data from the NLS terrain database ("maastotietokanta") into a Mongo database.
 */
public class TerrainDatabaseImporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerrainDatabaseImporter.class);
    private static final String NAMESPACE = "http://xml.nls.fi/XML/Namespace/Maastotietojarjestelma/SiirtotiedostonMalli/2011-02";
    private final AppSchemaConfiguration configuration;
    private final MongoDatabase mongoDatabase;

    private TerrainDatabaseImporter(@NotNull MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;

        File cacheDirectory = Paths.get("nlsmap-schema-cache").toAbsolutePath().toFile();
        if (!cacheDirectory.exists()) {
            LOGGER.info("Creating directory {}", cacheDirectory);
            if (!cacheDirectory.mkdirs()) {
                throw new RuntimeException("Could not create directory " + cacheDirectory);
            }
        } else if (!cacheDirectory.isDirectory()) {
            throw new RuntimeException("The path " + cacheDirectory + " does not point to a directory");
        }
        LOGGER.info("Storing cached XML schemas in {}", cacheDirectory);

        SchemaCache schemaCache = new SchemaCache(cacheDirectory, true);
        SchemaResolver resolver = new SchemaResolver(schemaCache);
        configuration = new AppSchemaConfiguration(NAMESPACE,
                "http://xml.nls.fi/XML/Schema/Maastotietojarjestelma/MTK/201405/Maastotiedot.xsd",
                resolver) {
            {
                addDependency(new GMLConfiguration());
            }

            @Override
            protected void configureBindings(Map bindings) {
                // We don't want any Features, only Maps (otherwise we might lose some data). Other GML types are fine
                // so we still want the GMLConfiguration.
                bindings.remove(new QName("http://www.opengis.net/gml", "AbstractFeatureType"));
            }
        };
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Arguments: mongodbHost[:port] dataDirectory");
        } else {
            String hostArgument = args[0];
            String directoryArgument = args[1];

            File directory = new File(directoryArgument);
            if (!directory.exists()) {
                throw new FileNotFoundException(directoryArgument);
            }

            try (MongoClient mongoClient = new MongoClient(hostArgument)) {
                MongoDatabase maastotietokanta = mongoClient.getDatabase(MongoConstants.TERRAIN_DATABASE);

                // TODO Make it possible to specify whether the collections should be dropped or not
                // Drop all old collections
                maastotietokanta.listCollectionNames().forEach((Consumer<String>) name -> {
                    if (!name.startsWith("system")) {
                        LOGGER.info("Dropping old collection {}", name);
                        maastotietokanta.getCollection(name).drop();
                    }
                });

                TerrainDatabaseImporter terrainDatabaseImporter = new TerrainDatabaseImporter(maastotietokanta);
                terrainDatabaseImporter.importData(new File("C:\\Users\\pette\\OneDrive\\Maps\\maastotietokanta_kaikki_kohteet"));
            }
        }
    }

    private void importData(@NotNull File file) {
        if (file.isDirectory()) {
            LOGGER.info("Scanning directory {} for data files", file);
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(file.toPath(), "*.xml")) {
                stream.forEach(path -> importDataFromFile(path.toFile()));
            } catch (IOException ex) {
                throw new RuntimeException("Error scanning directory " + file);
            }
        } else {
            importDataFromFile(file);
        }
    }

    private void importDataFromFile(@NotNull File file) {
        LOGGER.info("Starting import from {}", file);

        importData(file, featureIterator -> new PaikannimetImporter(featureIterator, mongoDatabase),
                new QName(NAMESPACE, "Paikannimi"));
        importData(file, featureIterator -> new TieviivatImporter(featureIterator, mongoDatabase),
                new QName(NAMESPACE, "Tieviiva"));
        importData(file, featureIterator -> new OsoitepisteetImporter(featureIterator, mongoDatabase),
                new QName(NAMESPACE, "Osoitepiste"));
        importData(file, featureIterator -> new KunnatImporter(featureIterator, mongoDatabase),
                new QName(NAMESPACE, "Kunta"));

        LOGGER.info("Finished import from {}", file);
    }

    private void importData(@NotNull File file, @NotNull Function<Iterator<Map<String, Object>>, AbstractImporter> importer,
                            @NotNull QName featureName) {
        try (FileInputStream is = new FileInputStream(file)) {
            LOGGER.info("Looking for features: {}", featureName);
            PullParser parser = new PullParser(configuration, is, featureName);
            importer.apply(createIterator(parser)).importData();
        } catch (Exception ex) {
            throw new RuntimeException("Error importing data from " + file, ex);
        }
    }

    private @NotNull Iterator<Map<String, Object>> createIterator(@NotNull PullParser parser) {
        return new Iterator<Map<String, Object>>() {

            private Map<String, Object> next = parse();

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public Map<String, Object> next() {
                try {
                    return next;
                } finally {
                    next = parse();
                }
            }

            @SuppressWarnings("unchecked")
            private @Nullable Map<String, Object> parse() {
                try {
                    return (Map<String, Object>) parser.parse();
                } catch (Exception ex) {
                    throw new RuntimeException("Error parsing source file", ex);
                }
            }
        };
    }
}
