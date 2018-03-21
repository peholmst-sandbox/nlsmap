package net.pkhapps.nlsmap.api.features;

import net.pkhapps.nlsmap.api.types.LocalizedString;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for features that have a human-readable name.
 */
public interface HasName extends Feature {

    /**
     * The name of the feature (for example a street name). Please note that the name can be
     * {@link LocalizedString#isEmpty() empty}.
     */
    @NotNull LocalizedString getName();
}
