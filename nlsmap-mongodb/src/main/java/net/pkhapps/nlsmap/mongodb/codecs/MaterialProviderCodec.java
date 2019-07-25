package net.pkhapps.nlsmap.mongodb.codecs;

import net.pkhapps.nlsmap.api.codes.MaterialProvider;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

/**
 * BSON codec for {@link MaterialProvider}.
 */
public class MaterialProviderCodec implements Codec<MaterialProvider> {

    @Override
    public MaterialProvider decode(BsonReader reader, DecoderContext decoderContext) {
        return MaterialProvider.findByCode(reader.readString()).orElse(MaterialProvider.UNKNOWN);
    }

    @Override
    public void encode(BsonWriter writer, MaterialProvider value, EncoderContext encoderContext) {
        writer.writeString(value.getCode());
    }

    @Override
    public Class<MaterialProvider> getEncoderClass() {
        return MaterialProvider.class;
    }
}
