package net.pkhapps.nlsmap.mongodb.importer.maastotiedot;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Map;

class OsoitepisteetImporter extends AbstractImporter {

    OsoitepisteetImporter(@NotNull Iterator<Map<String, Object>> featureIterator,
                          @NotNull MongoDatabase mongoDatabase) {
        super(featureIterator, mongoDatabase.getCollection("osoitepisteet", BsonDocument.class));
        getMongoCollection().createIndex(Indexes.geo2dsphere("sijainti"));
        getMongoCollection().createIndex(Indexes.compoundIndex(
                Indexes.text("nimi_ruotsi"),
                Indexes.text("nimi_suomi")));
        getMongoCollection().createIndex(Indexes.hashed("kuntatunnus"));
    }

    @Override
    @NotNull BsonDocument featureToDocument(@NotNull Map<String, Object> feature) {
        BsonDocument document = new BsonDocument();
        document.append("_id", new BsonInt64((Long) feature.get("gid")));

        appendValueIfNotNull(document, "sijaintitarkkuus", feature);
        appendValueIfNotNull(document, "aineistolahde", feature);
        appendValueIfNotNull(document, "alkupvm", feature);
        appendValueIfNotNull(document, "loppupvm", feature);
        appendValueIfNotNull(document, "suunta", feature);
        appendValueIfNotNull(document, "sijainti", feature);
        appendValueIfNotNull(document, "kohderyhma", feature);
        appendValueIfNotNull(document, "kohdeluokka", feature);
        appendValueIfNotNull(document, "numero", feature);
        appendValueIfNotNull(document, "nimi_suomi", feature);
        appendValueIfNotNull(document, "nimi_ruotsi", feature);
        appendValueIfNotNull(document, "nimi_inarinsaame", feature);
        appendValueIfNotNull(document, "nimi_koltansaame", feature);
        appendValueIfNotNull(document, "nimi_pohjoissaame", feature);
        appendValueIfNotNull(document, "kuntatunnus", feature);

        logger.debug("Created {}", document);
        return document;
    }
}
