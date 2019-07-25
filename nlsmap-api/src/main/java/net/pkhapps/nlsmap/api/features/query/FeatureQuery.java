package net.pkhapps.nlsmap.api.features.query;

import net.pkhapps.nlsmap.api.features.Feature;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Base interface for querying for geographical features.
 */
public interface FeatureQuery<F extends Feature, C extends Criteria, B extends CriteriaBuilder<C, B>> {

    /**
     * Finds all features that meet the given criteria. If the criteria is empty, no search is performed and an empty
     * list is returned.
     */
    @NotNull List<F> findByCriteria(@NotNull C criteria);

    /**
     * Creates a new criteria builder to be used to build criteria objects for {@link #findByCriteria(Criteria)}.
     */
    @NotNull B newCriteriaBuilder();
}
