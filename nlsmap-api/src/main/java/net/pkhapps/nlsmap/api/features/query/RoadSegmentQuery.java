package net.pkhapps.nlsmap.api.features.query;

import net.pkhapps.nlsmap.api.features.RoadSegment;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Query interface for querying {@link RoadSegment}s.
 */
public interface RoadSegmentQuery extends FeatureQuery<RoadSegment, RoadSegmentQuery.RoadSegmentCriteria,
        RoadSegmentQuery.RoadSegmentCriteriaBuilder> {

    /**
     * Interface defining a criteria to use when searching for {@link RoadSegment}s.
     */
    interface RoadSegmentCriteria extends Criteria {

        /**
         * The name or part of the name of the road segments. All languages are checked. Case is insensitive.
         *
         * @see RoadSegment#getName()
         */
        @NotNull Optional<String> getName();

        /**
         * The number of the road that the road segments must belong to.
         *
         * @see RoadSegment#getRoadNumber()
         */
        @NotNull Optional<Integer> getRoadNumber();

        /**
         * The address number that the road segments must include. Both sides of the road are checked.
         *
         * @see RoadSegment#getMaxAddressNumberOnTheLeft()
         * @see RoadSegment#getMinAddressNumberOnTheLeft()
         * @see RoadSegment#getMaxAddressNumberOnTheRight()
         * @see RoadSegment#getMinAddressNumberOnTheRight()
         */
        @NotNull Optional<Integer> getAddressNumber();
    }

    /**
     * Builder interface for building new {@link RoadSegmentCriteria} objects.
     *
     * @see RoadSegmentQuery#newCriteriaBuilder()
     */
    interface RoadSegmentCriteriaBuilder extends CriteriaBuilder<RoadSegmentCriteria, RoadSegmentCriteriaBuilder> {

        /**
         * @see RoadSegmentCriteria#getName()
         */
        @NotNull RoadSegmentCriteriaBuilder byName(@NotNull String name);

        /**
         * @see RoadSegmentCriteria#getRoadNumber()
         */
        @NotNull RoadSegmentCriteriaBuilder byRoadNumber(@NotNull Integer roadNumber);

        /**
         * @see RoadSegmentCriteria#getAddressNumber()
         */
        @NotNull RoadSegmentCriteriaBuilder byAddressNumber(@NotNull Integer addressNumber);
    }
}
