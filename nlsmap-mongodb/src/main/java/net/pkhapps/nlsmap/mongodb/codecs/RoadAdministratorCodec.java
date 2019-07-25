package net.pkhapps.nlsmap.mongodb.codecs;

import net.pkhapps.nlsmap.api.codes.RoadAdministrator;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

/**
 * BSON codec for {@link RoadAdministrator}.
 */
public class RoadAdministratorCodec implements Codec<RoadAdministrator> {

    @Override
    public RoadAdministrator decode(BsonReader reader, DecoderContext decoderContext) {
        return RoadAdministrator.findByCode(reader.readString()).orElse(RoadAdministrator.UNKNOWN);
    }

    @Override
    public void encode(BsonWriter writer, RoadAdministrator value, EncoderContext encoderContext) {
        writer.writeString(value.getCode());
    }

    @Override
    public Class<RoadAdministrator> getEncoderClass() {
        return RoadAdministrator.class;
    }
}
