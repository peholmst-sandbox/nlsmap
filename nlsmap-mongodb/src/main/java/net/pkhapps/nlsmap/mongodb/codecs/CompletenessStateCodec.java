package net.pkhapps.nlsmap.mongodb.codecs;

import net.pkhapps.nlsmap.api.codes.CompletenessState;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

/**
 * BSON codec for {@link CompletenessState}.
 */
public class CompletenessStateCodec implements Codec<CompletenessState> {

    @Override
    public CompletenessState decode(BsonReader reader, DecoderContext decoderContext) {
        return CompletenessState.findByCode(reader.readString()).orElse(CompletenessState.UNKNOWN);
    }

    @Override
    public void encode(BsonWriter writer, CompletenessState value, EncoderContext encoderContext) {
        writer.writeString(value.getCode());
    }

    @Override
    public Class<CompletenessState> getEncoderClass() {
        return CompletenessState.class;
    }
}
