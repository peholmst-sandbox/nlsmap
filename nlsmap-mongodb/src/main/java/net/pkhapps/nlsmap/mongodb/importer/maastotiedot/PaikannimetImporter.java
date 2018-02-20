package net.pkhapps.nlsmap.mongodb.importer.maastotiedot;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Map;

class PaikannimetImporter extends AbstractImporter {

    PaikannimetImporter(@NotNull Iterator<Map<String, Object>> featureIterator,
                        @NotNull MongoDatabase mongoDatabase) {
        super(featureIterator, mongoDatabase.getCollection("paikannimet", BsonDocument.class));
        getMongoCollection().createIndex(Indexes.geo2dsphere("sijainti"));
        getMongoCollection().createIndex(Indexes.text("teksti"));
    }

    @Override
    @NotNull BsonDocument featureToDocument(@NotNull Map<String, Object> feature) {
        BsonDocument document = new BsonDocument();
        document.append("_id", new BsonInt64((Long) feature.get("gid")));

        appendValueIfNotNull(document, "sijaintitarkkuus", feature);
        appendValueIfNotNull(document, "aineistolahde", feature);
        appendValueIfNotNull(document, "alkupvm", feature);
        appendValueIfNotNull(document, "suunta", feature);
        appendValueIfNotNull(document, "teksti", feature);
        appendValueIfNotNull(document, "dx", feature);
        appendValueIfNotNull(document, "dy", feature);
        appendValueIfNotNull(document, "sijainti", feature);
        appendValueIfNotNull(document, "kohderyhma", feature);
        appendValueIfNotNull(document, "kohdeluokka", feature);
        appendValueIfNotNull(document, "ladontatunnus", feature);
        appendValueIfNotNull(document, "versaalitieto", feature);
        appendValueIfNotNull(document, "nrKarttanimiId", feature);

        logger.debug("Created {}", document);
        return document;
    }
}
