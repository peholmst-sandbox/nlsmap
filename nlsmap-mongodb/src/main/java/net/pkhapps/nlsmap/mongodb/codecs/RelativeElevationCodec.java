package net.pkhapps.nlsmap.mongodb.codecs;

import net.pkhapps.nlsmap.api.codes.RelativeElevation;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

/**
 * BSON codec for {@link RelativeElevation}.
 */
public class RelativeElevationCodec implements Codec<RelativeElevation> {
    
    @Override
    public RelativeElevation decode(BsonReader reader, DecoderContext decoderContext) {
        return RelativeElevation.findByCode(reader.readString()).orElse(RelativeElevation.UNDEFINED);
    }

    @Override
    public void encode(BsonWriter writer, RelativeElevation value, EncoderContext encoderContext) {
        writer.writeString(value.getCode());
    }

    @Override
    public Class<RelativeElevation> getEncoderClass() {
        return RelativeElevation.class;
    }
}
