package net.pkhapps.nlsmap.api.features.query;

import net.pkhapps.nlsmap.api.features.AddressPoint;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Query interface for querying {@link net.pkhapps.nlsmap.api.features.AddressPoint}s.
 */
public interface AddressPointQuery extends FeatureQuery<AddressPoint, AddressPointQuery.AddressPointCriteria,
        AddressPointQuery.AddressPointCriteriaBuilder> {

    /**
     * Interface defining a criteria to use when searching for {@link AddressPoint}s.
     */
    interface AddressPointCriteria extends Criteria {

        /**
         * The name or part of the name of the address points. All languages are checked. Case is insensitive.
         *
         * @see AddressPoint#getName()
         */
        @NotNull Optional<String> getName();

        /**
         * The number or part of the number of the address point. Can also include letters. Case is insensitive.
         *
         * @see AddressPoint#getNumber()
         */
        @NotNull Optional<String> getNumber();
    }

    /**
     * Builder interface for building new {@link AddressPointCriteria} objects.
     */
    interface AddressPointCriteriaBuilder extends CriteriaBuilder<AddressPointCriteria, AddressPointCriteriaBuilder> {

        /**
         * @see AddressPointCriteria#getName()
         */
        @NotNull AddressPointCriteriaBuilder byName(@NotNull String name);

        /**
         * @see AddressPointCriteria#getNumber()
         */
        @NotNull AddressPointCriteriaBuilder byNumber(@NotNull String number);
    }
}
