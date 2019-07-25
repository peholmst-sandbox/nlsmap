package net.pkhapps.nlsmap.mongodb.codecs;

import net.pkhapps.nlsmap.api.codes.RoadSurface;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

/**
 * BSON codec for {@link RoadSurface}.
 */
public class RoadSurfaceCodec implements Codec<RoadSurface> {

    @Override
    public RoadSurface decode(BsonReader reader, DecoderContext decoderContext) {
        return RoadSurface.findByCode(reader.readString()).orElse(RoadSurface.UNKNOWN);
    }

    @Override
    public void encode(BsonWriter writer, RoadSurface value, EncoderContext encoderContext) {
        writer.writeString(value.getCode());
    }

    @Override
    public Class<RoadSurface> getEncoderClass() {
        return RoadSurface.class;
    }
}
