package net.pkhapps.nlsmap.api.features;

import net.pkhapps.nlsmap.api.codes.Municipality;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for features that belong to a particular {@link Municipality}.
 */
public interface HasMunicipality {

    /**
     * The municipality that the feature belongs to.
     */
    @NotNull Municipality getMunicipality();
}
