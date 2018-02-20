package net.pkhapps.nlsmap.mongodb.importer.maastotiedot;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Map;

class KunnatImporter extends AbstractImporter {

    KunnatImporter(@NotNull Iterator<Map<String, Object>> featureIterator,
                   @NotNull MongoDatabase mongoDatabase) {
        super(featureIterator, mongoDatabase.getCollection("kunnat", BsonDocument.class));
        getMongoCollection().createIndex(Indexes.geo2dsphere("sijainti.Piste"));
        getMongoCollection().createIndex(Indexes.geo2dsphere("sijainti.Alue"));
        getMongoCollection().createIndex(Indexes.hashed("kuntatunnus"));
    }

    @Override
    @NotNull BsonDocument featureToDocument(@NotNull Map<String, Object> feature) {
        BsonDocument document = new BsonDocument();
        document.append("_id", new BsonInt64((Long) feature.get("gid")));

        appendValueIfNotNull(document, "sijaintitarkkuus", feature);
        appendValueIfNotNull(document, "aineistolahde", feature);
        appendValueIfNotNull(document, "alkupvm", feature);
        appendValueIfNotNull(document, "sijainti", feature);
        appendValueIfNotNull(document, "kohderyhma", feature);
        appendValueIfNotNull(document, "kohdeluokka", feature);
        appendValueIfNotNull(document, "kuntatunnus", feature);

        logger.debug("Created {}", document);
        return document;
    }
}
