package net.pkhapps.nlsmap.mongodb.codecs;

import net.pkhapps.nlsmap.api.codes.LocationAccuracy;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

/**
 * BSON codec for {@link LocationAccuracy}.
 */
public class LocationAccuracyCodec implements Codec<LocationAccuracy> {

    @Override
    public LocationAccuracy decode(BsonReader reader, DecoderContext decoderContext) {
        return LocationAccuracy.findByCode(reader.readString()).orElse(LocationAccuracy.UNDEFINED);
    }

    @Override
    public void encode(BsonWriter writer, LocationAccuracy value, EncoderContext encoderContext) {
        writer.writeString(value.getCode());
    }

    @Override
    public Class<LocationAccuracy> getEncoderClass() {
        return LocationAccuracy.class;
    }
}
