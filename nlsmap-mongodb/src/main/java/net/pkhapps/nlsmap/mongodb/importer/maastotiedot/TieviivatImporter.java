package net.pkhapps.nlsmap.mongodb.importer.maastotiedot;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import net.pkhapps.nlsmap.mongodb.MongoConstants;
import net.pkhapps.nlsmap.mongodb.MongoConstants.RoadSegmentProperties;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Map;

class TieviivatImporter extends AbstractImporter {

    TieviivatImporter(@NotNull Iterator<Map<String, Object>> featureIterator,
                      @NotNull MongoDatabase mongoDatabase) {
        super(featureIterator, mongoDatabase.getCollection(MongoConstants.ROAD_SEGMENTS_COLLECTION, BsonDocument.class));
        getMongoCollection().createIndex(Indexes.geo2dsphere("sijainti"));
        getMongoCollection().createIndex(Indexes.compoundIndex(
                Indexes.text("nimi_ruotsi.value"),
                Indexes.text("nimi_suomi.value")));
        getMongoCollection().createIndex(Indexes.hashed("kuntatunnus"));
        // TODO We probably need to add more indexes for the other search fields
    }

    @Override
    @NotNull BsonDocument featureToDocument(@NotNull Map<String, Object> feature) {
        BsonDocument document = new BsonDocument();
        document.append(MongoConstants.DOCUMENT_ID, new BsonInt64((Long) feature.get("gid")));

        appendValueIfNotNull(document, RoadSegmentProperties.LOCATION_ACCURACY, feature);
        appendValueIfNotNull(document, RoadSegmentProperties.MATERIAL_PROVIDER, feature);
        appendValueIfNotNull(document, RoadSegmentProperties.START_DATE, feature);
        appendValueIfNotNull(document, RoadSegmentProperties.END_DATE, feature);
        appendValueIfNotNull(document, RoadSegmentProperties.LOCATION, feature);
        appendValueIfNotNull(document, RoadSegmentProperties.FEATURE_CLASS, feature);
        appendValueIfNotNull(document, RoadSegmentProperties.RELATIVE_ELEVATION, feature);
        appendValueIfNotNull(document, RoadSegmentProperties.COMPLETENESS_STATE, feature);
        appendValueIfNotNull(document, RoadSegmentProperties.SURFACE, feature);
        appendValueIfNotNull(document, RoadSegmentProperties.TRAVEL_DIRECTION, feature);
        appendValueIfNotNull(document, RoadSegmentProperties.ROAD_ADMINISTRATOR, feature);
        appendValueIfNotNull(document, RoadSegmentProperties.MIN_ADDRESS_NUMBER_LEFT, feature);
        appendValueIfNotNull(document, RoadSegmentProperties.MIN_ADDRESS_NUMBER_RIGHT, feature);
        appendValueIfNotNull(document, RoadSegmentProperties.MAX_ADDRESS_NUMBER_LEFT, feature);
        appendValueIfNotNull(document, RoadSegmentProperties.MAX_ADDRESS_NUMBER_RIGHT, feature);
        appendValueIfNotNull(document, RoadSegmentProperties.ROAD_NUMBER, feature);
        appendValueIfNotNull(document, RoadSegmentProperties.ROAD_PART_NUMBER, feature);
        appendValueIfNotNull(document, RoadSegmentProperties.NAME_FIN, feature);
        appendValueIfNotNull(document, RoadSegmentProperties.NAME_SWE, feature);
        appendValueIfNotNull(document, RoadSegmentProperties.NAME_SMN, feature);
        appendValueIfNotNull(document, RoadSegmentProperties.NAME_SMS, feature);
        appendValueIfNotNull(document, RoadSegmentProperties.NAME_SME, feature);
        appendValueIfNotNull(document, RoadSegmentProperties.MUNICIPALITY, feature);

        logger.debug("Created {}", document);
        return document;
    }
}
