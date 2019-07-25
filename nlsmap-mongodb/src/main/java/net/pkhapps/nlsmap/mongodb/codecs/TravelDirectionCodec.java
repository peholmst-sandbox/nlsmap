package net.pkhapps.nlsmap.mongodb.codecs;

import net.pkhapps.nlsmap.api.codes.TravelDirection;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

/**
 * BSON codec for {@link TravelDirection}.
 */
public class TravelDirectionCodec implements Codec<TravelDirection> {

    @Override
    public TravelDirection decode(BsonReader reader, DecoderContext decoderContext) {
        return TravelDirection.findByCode(reader.readString()).orElse(TravelDirection.UNKNOWN);
    }

    @Override
    public void encode(BsonWriter writer, TravelDirection value, EncoderContext encoderContext) {
        writer.writeString(value.getCode());
    }

    @Override
    public Class<TravelDirection> getEncoderClass() {
        return TravelDirection.class;
    }
}
