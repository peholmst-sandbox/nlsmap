package net.pkhapps.nlsmap.mongodb.importer.nimisto;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xsd.XSDAnnotation;
import org.eclipse.xsd.XSDEnumerationFacet;
import org.eclipse.xsd.XSDSchema;
import org.geotools.xml.Schemas;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import java.io.IOException;
import java.util.Optional;

public class NimistoImporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(NimistoImporter.class);
    private final XSDSchema schema;
    private final MongoCollection<BsonDocument> mongoCollection;

    private NimistoImporter(@NotNull String source,
                            @NotNull MongoCollection<BsonDocument> mongoCollection) throws IOException {
        schema = Schemas.parse(source);
        this.mongoCollection = mongoCollection;
    }

    private NimistoImporter(@NotNull String source, @NotNull MongoDatabase mongoDatabase,
                            @NotNull String collectionName) throws IOException {
        this(source, mongoDatabase.getCollection(collectionName, BsonDocument.class));
    }

    public static void main(String[] args) throws Exception {
        try (MongoClient mongoClient = new MongoClient()) {
            MongoDatabase nimistorekisteri = mongoClient.getDatabase("nimistorekisteri");

            // TODO We don't know what exactly we are going to need yet

            new NimistoImporter("http://xml.nls.fi/Nimisto/Nimistorekisteri/kieli.xsd", nimistorekisteri,
                    "kielet").importData();
            new NimistoImporter("http://xml.nls.fi/Nimisto/Nimistorekisteri/kunta.xsd", nimistorekisteri,
                    "kunnat").importData();
            new NimistoImporter("http://xml.nls.fi/Nimisto/Nimistorekisteri/maakunta.xsd", nimistorekisteri,
                    "maakunnat").importData();
            new NimistoImporter("http://xml.nls.fi/Nimisto/Nimistorekisteri/mittakaavaluokka.xsd", nimistorekisteri,
                    "mittakaavaluokat").importData();
            new NimistoImporter("http://xml.nls.fi/Nimisto/Nimistorekisteri/paikannimiLahde.xsd", nimistorekisteri,
                    "paikannimilahteet").importData();
            new NimistoImporter("http://xml.nls.fi/Nimisto/Nimistorekisteri/paikkatyyppi.xsd", nimistorekisteri,
                    "paikkatyypit").importData();
            new NimistoImporter("http://xml.nls.fi/Nimisto/Nimistorekisteri/paikkatyyppialaryhma.xsd", nimistorekisteri,
                    "paikkatyyppialaryhmat").importData();
            new NimistoImporter("http://xml.nls.fi/Nimisto/Nimistorekisteri/paikkatyyppiryhma.xsd", nimistorekisteri,
                    "paikkatyyppiryhmat").importData();
            new NimistoImporter("http://xml.nls.fi/Nimisto/Nimistorekisteri/seutukunta.xsd", nimistorekisteri,
                    "seutukunnat").importData();
            new NimistoImporter("http://xml.nls.fi/Nimisto/Nimistorekisteri/suuralue.xsd", nimistorekisteri,
                    "suuralueet").importData();
        }
    }

    private static @NotNull Optional<String> translateLanguage(@NotNull String language) {
        switch (language) {
            case "fin":
                return Optional.of("suomi");
            case "swe":
                return Optional.of("ruotsi");
            default:
                return Optional.empty();
        }
    }

    private void importData() {
        LOGGER.info("Importing data from {}", schema.getSchemaLocation());
        schema.getContents().get(0).eContents().forEach(enumeration -> {
            if (enumeration instanceof XSDEnumerationFacet) {
                EList<Object> enumerationValues = ((XSDEnumerationFacet) enumeration).getValue();
                if (enumerationValues.size() == 1 && enumeration.eContents().size() > 0) {
                    BsonDocument document = createDocumentFromEnumeration(enumerationValues.get(0),
                            ((XSDAnnotation) enumeration.eContents().get(0)).getUserInformation());
                    mongoCollection.replaceOne(
                            Filters.eq("_id", document.get("_id")),
                            document,
                            new UpdateOptions().upsert(true));
                }
            }
        });
    }

    private @NotNull BsonDocument createDocumentFromEnumeration(@NotNull Object id, @NotNull EList<Element> names) {
        BsonDocument document = new BsonDocument();
        if (id instanceof String) {
            document.append("_id", new BsonString((String) id));
        } else if (id instanceof Number) {
            document.append("_id", new BsonInt32(((Number) id).intValue()));
        } else {
            throw new IllegalArgumentException("Unknown ID: " + id);
        }
        names.forEach(nameElement -> {
            String language = nameElement.getAttribute("xml:lang");
            String name = nameElement.getTextContent();
            translateLanguage(language).ifPresent(translatedLanguage -> {
                document.append("nimi_" + translatedLanguage, createLocalizedName(language, name));
            });
        });

        LOGGER.debug("Created {}", document);
        return document;
    }

    private @NotNull BsonDocument createLocalizedName(@NotNull String language, @NotNull String name) {
        BsonDocument document = new BsonDocument();
        document.append("value", new BsonString(name)); // We use this in the terrain database as well
        document.append("kieli", new BsonString(language));
        return document;
    }
}
