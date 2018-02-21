package net.pkhapps.nlsmap.mongodb.query;

import com.vividsolutions.jts.geom.LineString;
import net.pkhapps.nlsmap.api.codes.*;
import net.pkhapps.nlsmap.api.features.RoadSegment;
import net.pkhapps.nlsmap.api.types.LocalizedString;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Optional;

/**
 * TODO Implement me!
 */
class RoadSegmentImpl implements RoadSegment {

    // TODO Null checks etc.

    Long id;
    RelativeElevation relativeElevation;
    CompletenessState completenessState;
    LocationAccuracy locationAccuracy;
    MaterialProvider materialProvider;
    LineString location;
    RoadSurface surface;
    TravelDirection direction;
    Integer roadNumber;
    Integer roadPartNumber;
    AdministrativeRoadClass administrativeClass;
    Integer minAddressNumberOnTheLeft;
    Integer maxAddressNumberOnTheLeft;
    Integer minAddressNumberOnTheRight;
    Integer maxAddressNumberOnTheRight;
    Municipality municipality;
    LocalizedString name;
    RoadClass featureClass;
    LocalDate startDate;
    LocalDate endDate;

    @Override
    public @NotNull RelativeElevation getRelativeElevation() {
        return relativeElevation;
    }

    @Override
    public @NotNull CompletenessState getCompletenessState() {
        return completenessState;
    }

    @Override
    public @NotNull RoadSurface getSurface() {
        return surface;
    }

    @Override
    public @NotNull TravelDirection getDirection() {
        return direction;
    }

    @Override
    public @NotNull Optional<Integer> getRoadNumber() {
        return Optional.ofNullable(roadNumber);
    }

    @Override
    public @NotNull Optional<Integer> getRoadPartNumber() {
        return Optional.ofNullable(roadPartNumber);
    }

    @Override
    public @NotNull Optional<AdministrativeRoadClass> getAdministrativeClass() {
        return Optional.ofNullable(administrativeClass);
    }

    @Override
    public @NotNull Optional<Integer> getMinAddressNumberOnTheLeft() {
        return Optional.ofNullable(minAddressNumberOnTheLeft);
    }

    @Override
    public @NotNull Optional<Integer> getMaxAddressNumberOnTheLeft() {
        return Optional.ofNullable(maxAddressNumberOnTheLeft);
    }

    @Override
    public @NotNull Optional<Integer> getMinAddressNumberOnTheRight() {
        return Optional.ofNullable(minAddressNumberOnTheRight);
    }

    @Override
    public @NotNull Optional<Integer> getMaxAddressNumberOnTheRight() {
        return Optional.ofNullable(maxAddressNumberOnTheRight);
    }

    @Override
    public @NotNull Optional<LocalizedString> getName() {
        return Optional.ofNullable(name);
    }

    @NotNull
    @Override
    public RoadClass getFeatureClass() {
        return featureClass;
    }

    @NotNull
    @Override
    public LocationAccuracy getLocationAccuracy() {
        return locationAccuracy;
    }

    @NotNull
    @Override
    public LineString getLocation() {
        return location;
    }

    @Override
    public @NotNull Long getId() {
        return id;
    }

    @Override
    public @NotNull MaterialProvider getMaterialProvider() {
        return materialProvider;
    }

    @Override
    public @NotNull Optional<LocalDate> getStartDate() {
        return Optional.ofNullable(startDate);
    }

    @Override
    public @NotNull Optional<LocalDate> getEndDate() {
        return Optional.ofNullable(endDate);
    }

    @Override
    public @NotNull Municipality getMunicipality() {
        return municipality;
    }
}
