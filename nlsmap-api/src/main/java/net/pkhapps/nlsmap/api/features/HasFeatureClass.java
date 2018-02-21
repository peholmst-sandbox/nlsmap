package net.pkhapps.nlsmap.api.features;

import net.pkhapps.nlsmap.api.codes.Code;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for features that are classified by a particular feature class.
 */
public interface HasFeatureClass<FeatureClass extends Code<?>> extends Feature {

    /**
     * The feature class.
     */
    @NotNull FeatureClass getFeatureClass();
}
