package net.pkhapps.nlsmap.mongodb.codecs;

import net.pkhapps.nlsmap.api.codes.Municipality;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

/**
 * BSON codec for {@link Municipality}.
 */
public class MunicipalityCodec implements Codec<Municipality> {

    @Override
    public Municipality decode(BsonReader reader, DecoderContext decoderContext) {
        return Municipality.findByCode(reader.readString()).orElse(Municipality.UNKNOWN);
    }

    @Override
    public void encode(BsonWriter writer, Municipality value, EncoderContext encoderContext) {
        writer.writeString(value.getCode());
    }

    @Override
    public Class<Municipality> getEncoderClass() {
        return Municipality.class;
    }
}
