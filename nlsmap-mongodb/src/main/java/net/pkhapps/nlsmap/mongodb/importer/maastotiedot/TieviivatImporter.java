package net.pkhapps.nlsmap.mongodb.importer.maastotiedot;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import net.pkhapps.nlsmap.mongodb.MongoConstants;
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
        document.append("_id", new BsonInt64((Long) feature.get("gid")));

        appendValueIfNotNull(document, MongoConstants.RoadSegmentProperties.LOCATION_ACCURACY, feature);
        appendValueIfNotNull(document, MongoConstants.RoadSegmentProperties.MATERIAL_PROVIDER, feature);
        appendValueIfNotNull(document, "alkupvm", feature);
        appendValueIfNotNull(document, "loppupvm", feature);
        appendValueIfNotNull(document, MongoConstants.RoadSegmentProperties.LOCATION, feature);
        appendValueIfNotNull(document, "kohdeluokka", feature);
        appendValueIfNotNull(document, "tasosijainti", feature);
        appendValueIfNotNull(document, MongoConstants.RoadSegmentProperties.COMPLETENESS_STATE, feature);
        appendValueIfNotNull(document, MongoConstants.RoadSegmentProperties.SURFACE, feature);
        appendValueIfNotNull(document, "yksisuuntaisuus", feature);
        appendValueIfNotNull(document, "hallinnollinenLuokka", feature);
        appendValueIfNotNull(document, "minOsoitenumeroVasen", feature);
        appendValueIfNotNull(document, "minOsoitenumeroOikea", feature);
        appendValueIfNotNull(document, "maxOsoitenumeroVasen", feature);
        appendValueIfNotNull(document, "maxOsoitenumeroOikea", feature);
        appendValueIfNotNull(document, "tienumero", feature);
        appendValueIfNotNull(document, "tieosanumero", feature);
        appendValueIfNotNull(document, MongoConstants.RoadSegmentProperties.NAME_FIN, feature);
        appendValueIfNotNull(document, MongoConstants.RoadSegmentProperties.NAME_SWE, feature);
        appendValueIfNotNull(document, MongoConstants.RoadSegmentProperties.NAME_SMN, feature);
        appendValueIfNotNull(document, MongoConstants.RoadSegmentProperties.NAME_SMS, feature);
        appendValueIfNotNull(document, MongoConstants.RoadSegmentProperties.NAME_SME, feature);
        appendValueIfNotNull(document, MongoConstants.RoadSegmentProperties.MUNICIPALITY, feature);

        logger.debug("Created {}", document);
        return document;
    }
}
