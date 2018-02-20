package net.pkhapps.nlsmap.mongodb.importer.maastotiedot;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateFilter;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.geojson.GeoJsonWriter;
import com.vividsolutions.jts.util.Stopwatch;
import org.bson.*;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Base class for importers of data from the terrain database ("maastotietokanta").
 */
abstract class AbstractImporter {

    private static final CoordinateReferenceSystem ETRS89_TM35FIN;
    private static final MathTransform TRANSFORM;

    static {
        try {
            // Source: http://spatialreference.org/ref/epsg/3067/prettywkt/
            ETRS89_TM35FIN = CRS.parseWKT("PROJCS[\"ETRS89 / TM35FIN(E,N)\",\n" +
                    "    GEOGCS[\"ETRS89\",\n" +
                    "        DATUM[\"European_Terrestrial_Reference_System_1989\",\n" +
                    "            SPHEROID[\"GRS 1980\",6378137,298.257222101,\n" +
                    "                AUTHORITY[\"EPSG\",\"7019\"]],\n" +
                    "            TOWGS84[565.04,49.91,465.84,1.9848,-1.7439,9.0587,4.0772],\n" +
                    "            AUTHORITY[\"EPSG\",\"6258\"]],\n" +
                    "        PRIMEM[\"Greenwich\",0,\n" +
                    "            AUTHORITY[\"EPSG\",\"8901\"]],\n" +
                    "        UNIT[\"degree\",0.0174532925199433,\n" +
                    "            AUTHORITY[\"EPSG\",\"9122\"]],\n" +
                    "        AUTHORITY[\"EPSG\",\"4258\"]],\n" +
                    "    PROJECTION[\"Transverse_Mercator\"],\n" +
                    "    PARAMETER[\"latitude_of_origin\",0],\n" +
                    "    PARAMETER[\"central_meridian\",27],\n" +
                    "    PARAMETER[\"scale_factor\",0.9996],\n" +
                    "    PARAMETER[\"false_easting\",500000],\n" +
                    "    PARAMETER[\"false_northing\",0],\n" +
                    "    UNIT[\"metre\",1,\n" +
                    "        AUTHORITY[\"EPSG\",\"9001\"]],\n" +
                    "    AXIS[\"Easting\",EAST],\n" +
                    "    AXIS[\"Northing\",NORTH],\n" +
                    "    AUTHORITY[\"EPSG\",\"3067\"]]");
            TRANSFORM = CRS.findMathTransform(ETRS89_TM35FIN, DefaultGeographicCRS.WGS84, true);
        } catch (FactoryException ex) {
            throw new RuntimeException("Error setting up ETRS89_TM35FIN", ex);
        }
    }

    final Logger logger = LoggerFactory.getLogger(getClass());
    private final Iterator<Map<String, Object>> featureIterator;
    private final GeoJsonWriter geoJsonWriter;
    private final MongoCollection<BsonDocument> mongoCollection;

    AbstractImporter(@NotNull Iterator<Map<String, Object>> featureIterator,
                     @NotNull MongoCollection<BsonDocument> mongoCollection) {
        this.featureIterator = featureIterator;
        this.mongoCollection = mongoCollection;
        geoJsonWriter = new GeoJsonWriter();
        geoJsonWriter.setEncodeCRS(false);
    }

    final @NotNull MongoCollection<BsonDocument> getMongoCollection() {
        return mongoCollection;
    }

    final void importData() {
        logger.info("Importing into {}", getMongoCollection().getNamespace());
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        long count = 0;
        while (featureIterator.hasNext()) {
            importFeature(featureIterator.next());
            count++;
        }
        stopwatch.stop();
        logger.info("Finished importing {} feature(s) into {} in {}", count, getMongoCollection().getNamespace(),
                stopwatch.getTimeString());
    }

    private void importFeature(@NotNull Map<String, Object> feature) {
        logger.trace("Importing feature {}", feature);
        BsonDocument document = featureToDocument(feature);
        getMongoCollection().replaceOne(
                Filters.eq("_id", document.get("_id")),
                document,
                new UpdateOptions().upsert(true));
        // TODO Batch updates/inserts
    }

    abstract @NotNull BsonDocument featureToDocument(@NotNull Map<String, Object> feature);

    final void appendValueIfNotNull(@NotNull BsonDocument document, @NotNull String propertyName,
                                    @NotNull Map<String, Object> propertyMap) {
        final Object propertyValue = propertyMap.get(propertyName);
        if (propertyValue != null) {
            logger.trace("Appending {}: {} of type {}", propertyName, propertyValue, propertyValue.getClass());
            BsonValue convertedValue = convertValue(propertyValue);
            if (convertedValue != null) {
                document.append(propertyName, convertedValue);
            }
        }
    }

    private @Nullable BsonValue convertValue(@NotNull Object value) {
        if (value instanceof Integer) {
            return new BsonInt32((Integer) value);
        } else if (value instanceof Long) {
            return new BsonInt64((Long) value);
        } else if (value instanceof String) {
            return new BsonString((String) value);
        } else if (value instanceof Date) {
            return new BsonDateTime(((Date) value).getTime());
        } else if (value instanceof Geometry) {
            return convertGeometry((Geometry) value);
        } else if (value instanceof Map) {
            return convertMap((Map) value);
        } else {
            logger.warn("Could not convert {} of type {} to BsonValue", value, value.getClass());
            return null;
        }
    }

    private @Nullable BsonValue convertGeometry(@NotNull Geometry value) {
        try {
            // Flatten to two dimensions since MongoDB does not understand three dimensions.
            AtomicBoolean converted = new AtomicBoolean(false);
            value.apply((CoordinateFilter) coord -> {
                if (coord.z != Coordinate.NULL_ORDINATE) {
                    logger.trace("Converting {} into 2 dimensions", coord);
                    coord.setCoordinate(new Coordinate(coord.x, coord.y));
                    converted.set(true);
                }
            });
            if (converted.get()) {
                value.geometryChanged();
            }
            return BsonDocument.parse(geoJsonWriter.write(JTS.transform(value, TRANSFORM)));
        } catch (TransformException ex) {
            logger.error("Could not transform geometry to WGS84", ex);
            return null;
        }
    }

    private @NotNull BsonValue convertMap(@NotNull Map<?, ?> value) {
        BsonDocument document = new BsonDocument();
        value.forEach((mapKey, mapValue) -> {
            if (mapValue != null) {
                if (mapKey == null) {
                    document.append("value", convertValue(mapValue));
                } else {
                    document.append(mapKey.toString(), convertValue(mapValue));
                }
            }
        });
        return document;
    }
}
