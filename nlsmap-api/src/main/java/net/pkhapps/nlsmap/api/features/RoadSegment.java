package net.pkhapps.nlsmap.api.features;

import net.pkhapps.nlsmap.api.codes.*;
import net.pkhapps.nlsmap.api.types.LocalizedString;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Interface for a Road Segment feature. A road segment is a part of a road and can cover multiple addresses. Depending
 * on the granularity of the segment, it might not be possible to pinpoint the exact location of a particular street
 * address. You can only track down the road segment that the address is on.
 *
 * @see AddressPoint
 */
public interface RoadSegment extends Feature, HasFeatureClass<RoadClass>, HasMunicipality, HasLineStringLocation {

    /**
     * The relative elevation of the segment (e.g. whether the road segment is in a tunnel, on the surface, etc.).
     */
    @NotNull RelativeElevation getRelativeElevation();

    /**
     * The completeness state of the segment (e.g. roads that are still under construction might already be on the map).
     */
    @NotNull CompletenessState getCompletenessState();

    /**
     * The surface material of the segment.
     */
    @NotNull RoadSurface getSurface();

    /**
     * The travel direction of the segment (e.g. one-way or two-way).
     */
    @NotNull TravelDirection getDirection();

    /**
     * The number of the road that this segment belongs to. This can be used to identify the road when no name is
     * available.
     */
    @NotNull Optional<Integer> getRoadNumber();

    /**
     * The number of the road part that this segment belongs to.
     */
    @NotNull Optional<Integer> getRoadPartNumber();

    /**
     * The administrative class of the segment (e.g. the party who is in charge of maintaining and
     * administrating the road segment).
     */
    @NotNull Optional<AdministrativeRoadClass> getAdministrativeClass();

    /**
     * The smallest address number on the left side of the road segment.
     */
    @NotNull Optional<Integer> getMinAddressNumberOnTheLeft();

    /**
     * The largest address number on the left side of the road segment.
     */
    @NotNull Optional<Integer> getMaxAddressNumberOnTheLeft();

    /**
     * The smallest address number on the right side of the road segment.
     */
    @NotNull Optional<Integer> getMinAddressNumberOnTheRight();

    /**
     * The largest address number on the right side of the road segment.
     */
    @NotNull Optional<Integer> getMaxAddressNumberOnTheRight();

    /**
     * The name of the road that this segment belongs to.
     */
    @NotNull Optional<LocalizedString> getName();
}
