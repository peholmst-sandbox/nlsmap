package net.pkhapps.nlsmap.api.query;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Point;
import net.pkhapps.nlsmap.api.codes.Municipality;
import net.pkhapps.nlsmap.api.features.RoadSegment;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Query interface for querying {@link RoadSegment}s.
 */
public interface RoadSegmentQuery {

    /**
     * Finds all road segments that meet the given criteria. If the criteria is empty, no search is performed and
     * an empty list is returned.
     */
    @NotNull List<RoadSegment> findByCriteria(@NotNull Criteria criteria);

    /**
     * Creates a new {@link CriteriaBuilder} to be used to build {@link Criteria} objects for
     * {@link #findByCriteria(Criteria)}.
     */
    @NotNull CriteriaBuilder newCriteriaBuilder();

    /**
     * Interface defining a criteria to use when searching for {@link RoadSegment}s.
     */
    interface Criteria {

        /**
         * The envelope that the road segments must either pass through or start or end in.
         *
         * @see RoadSegment#getLocation()
         */
        @NotNull Optional<Envelope> getEnvelope();

        /**
         * A point that the road segments must be close to. The implementation is free to decide how close is close
         * enough.
         *
         * @see RoadSegment#getLocation()
         */
        @NotNull Optional<Point> getPoint();

        /**
         * The municipality that the road segments must belong to.
         *
         * @see RoadSegment#getMunicipality()
         */
        @NotNull Optional<Municipality> getMunicipality();

        /**
         * The name or part of the name of the road segments. All languages are checked.
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
     * Builder interface for building new {@link Criteria} objects.
     *
     * @see RoadSegmentQuery#newCriteriaBuilder()
     */
    interface CriteriaBuilder {

        /**
         * @see Criteria#getEnvelope()
         */
        @NotNull CriteriaBuilder within(@NotNull Envelope envelope);

        /**
         * @see Criteria#getPoint()
         */
        @NotNull CriteriaBuilder closeTo(@NotNull Point point);

        /**
         * @see Criteria#getMunicipality()
         */
        @NotNull CriteriaBuilder within(@NotNull Municipality municipality);

        /**
         * @see Criteria#getName()
         */
        @NotNull CriteriaBuilder byName(@NotNull String name);

        /**
         * @see Criteria#getRoadNumber()
         */
        @NotNull CriteriaBuilder byRoadNumber(@NotNull Integer roadNumber);

        /**
         * @see Criteria#getAddressNumber()
         */
        @NotNull CriteriaBuilder byAddressNumber(@NotNull Integer addressNumber);

        /**
         * Builds a new {@link Criteria} object.
         */
        @NotNull Criteria build();
    }
}
