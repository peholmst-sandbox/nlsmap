package net.pkhapps.nlsmap.api.features;

import net.pkhapps.nlsmap.api.codes.AddressPointClass;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Interface for an Address Point feature. An address point is a place with a name and optionally a number and can be
 * used as a street address in places where no streets exists, such as islands. Address points are also used as places
 * of entry for vessels.
 *
 * @see RoadSegment
 */
public interface AddressPoint extends Feature, HasFeatureClass<AddressPointClass>, HasMunicipality, HasPointLocation,
        HasName {

    /**
     * An optional number of the address point (like a street number), can also contain letters.
     */
    @NotNull Optional<String> getNumber();
}
