package net.pkhapps.nlsmap.mongodb.codecs;

import net.pkhapps.nlsmap.api.codes.*;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

/**
 * Codec provider for {@link Code} enumerations.
 */
public class CodeCodecProvider implements CodecProvider {

    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (clazz.equals(AddressPointClass.class)) {
            return (Codec<T>) new AddressPointClassCodec();
        } else if (clazz.equals(CompletenessState.class)) {
            return (Codec<T>) new CompletenessStateCodec();
        } else if (clazz.equals(LocationAccuracy.class)) {
            return (Codec<T>) new LocationAccuracyCodec();
        } else if (clazz.equals(MaterialProvider.class)) {
            return (Codec<T>) new MaterialProviderCodec();
        } else if (clazz.equals(Municipality.class)) {
            return (Codec<T>) new MunicipalityCodec();
        } else if (clazz.equals(RelativeElevation.class)) {
            return (Codec<T>) new RelativeElevationCodec();
        } else if (clazz.equals(RoadAdministrator.class)) {
            return (Codec<T>) new RoadAdministratorCodec();
        } else if (clazz.equals(RoadClass.class)) {
            return (Codec<T>) new RoadClassCodec();
        } else if (clazz.equals(RoadSurface.class)) {
            return (Codec<T>) new RoadSurfaceCodec();
        } else if (clazz.equals(TravelDirection.class)) {
            return (Codec<T>) new TravelDirectionCodec();
        } else {
            return null;
        }
    }
}
