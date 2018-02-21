package net.pkhapps.nlsmap.api.features;

import net.pkhapps.nlsmap.api.codes.MaterialProvider;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Base interface for a geographical feature.
 */
public interface Feature extends Serializable {

    /**
     * A numerical ID of the feature. This is mostly for internal use only, applications should not rely on this
     * property.
     */
    @NotNull Long getId();

    /**
     * The provider of the source material that the feature is imported from.
     */
    @NotNull MaterialProvider getMaterialProvider();

    /**
     * The date at which the feature became or becomes valid, if any.
     */
    @NotNull Optional<LocalDate> getStartDate();

    /**
     * The date at which the feature became or becomes invalid, if any.
     */
    @NotNull Optional<LocalDate> getEndDate();
}
