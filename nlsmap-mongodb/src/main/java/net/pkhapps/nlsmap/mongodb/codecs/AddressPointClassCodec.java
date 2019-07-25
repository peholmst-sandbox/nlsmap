package net.pkhapps.nlsmap.mongodb.codecs;

import net.pkhapps.nlsmap.api.codes.AddressPointClass;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

/**
 * BSON codec for {@link AddressPointClass}.
 */
public class AddressPointClassCodec implements Codec<AddressPointClass> {

    @Override
    public AddressPointClass decode(BsonReader reader, DecoderContext decoderContext) {
        return AddressPointClass.findByCode(reader.readString()).orElse(AddressPointClass.UNKNOWN);
    }

    @Override
    public void encode(BsonWriter writer, AddressPointClass value, EncoderContext encoderContext) {
        writer.writeString(value.getCode());
    }

    @Override
    public Class<AddressPointClass> getEncoderClass() {
        return AddressPointClass.class;
    }
}
