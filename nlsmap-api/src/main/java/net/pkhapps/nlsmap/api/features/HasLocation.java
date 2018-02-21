package net.pkhapps.nlsmap.api.features;

import com.vividsolutions.jts.geom.Geometry;
import net.pkhapps.nlsmap.api.codes.LocationAccuracy;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for features that have a geographical location.
 */
public interface HasLocation<LocationType extends Geometry> extends Feature {

    /**
     * The accuracy of the location of the feature.
     */
    @NotNull LocationAccuracy getLocationAccuracy();

    /**
     * The location of the feature, in two dimensions. The returned {@link LocationType} is a clone, so any changes made
     * to it will not be reflected in the feature itself.
     */
    @NotNull LocationType getLocation();
}
