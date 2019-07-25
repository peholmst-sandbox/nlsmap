package net.pkhapps.nlsmap.mongodb.codecs;

import net.pkhapps.nlsmap.api.codes.RoadClass;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

/**
 * BSON codec for {@link RoadClass}.
 */
public class RoadClassCodec implements Codec<RoadClass> {

    @Override
    public RoadClass decode(BsonReader reader, DecoderContext decoderContext) {
        return RoadClass.findByCode(reader.readString()).orElse(RoadClass.UNKNOWN);
    }

    @Override
    public void encode(BsonWriter writer, RoadClass value, EncoderContext encoderContext) {
        writer.writeString(value.getCode());
    }

    @Override
    public Class<RoadClass> getEncoderClass() {
        return RoadClass.class;
    }
}
