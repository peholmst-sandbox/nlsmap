package net.pkhapps.nlsmap.mongodb.query;

import com.vividsolutions.jts.geom.LineString;
import net.pkhapps.nlsmap.api.codes.*;
import net.pkhapps.nlsmap.api.features.RoadSegment;
import net.pkhapps.nlsmap.api.types.Language;
import net.pkhapps.nlsmap.api.types.LocalizedString;
import net.pkhapps.nlsmap.mongodb.documents.RoadSegmentDocument;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Optional;

/**
 * TODO Implement me!
 */
class RoadSegmentImpl implements RoadSegment {

    private final RoadSegmentDocument roadSegmentDocument;
    private LocalizedString name;

    RoadSegmentImpl(@NotNull RoadSegmentDocument roadSegmentDocument) {
        // TODO Null checks etc.
        this.roadSegmentDocument = roadSegmentDocument;
        LocalizedString.Builder nameBuilder = new LocalizedString.Builder();
        nameBuilder.withValue(Language.FINNISH, roadSegmentDocument.getNameFin());
        nameBuilder.withValue(Language.SWEDISH, roadSegmentDocument.getNameSwe());
        nameBuilder.withValue(Language.INARI_SAMI, roadSegmentDocument.getNameSmn());
    }

    @Override
    public @NotNull RelativeElevation getRelativeElevation() {
        return roadSegmentDocument.getRelativeElevation();
    }

    @Override
    public @NotNull CompletenessState getCompletenessState() {
        return roadSegmentDocument.getCompletenessState();
    }

    @Override
    public @NotNull RoadSurface getSurface() {
        return roadSegmentDocument.getSurface();
    }

    @Override
    public @NotNull TravelDirection getDirection() {
        return roadSegmentDocument.getDirection();
    }

    @Override
    public @NotNull Optional<Integer> getRoadNumber() {
        return Optional.ofNullable(roadSegmentDocument.getRoadNumber());
    }

    @Override
    public @NotNull Optional<Integer> getRoadPartNumber() {
        return Optional.ofNullable(roadSegmentDocument.getRoadPartNumber());
    }

    @Override
    public @NotNull Optional<RoadAdministrator> getAdministrator() {
        return Optional.ofNullable(roadSegmentDocument.getAdministrator());
    }

    @Override
    public @NotNull Optional<Integer> getMinAddressNumberOnTheLeft() {
        return Optional.ofNullable(roadSegmentDocument.getMinAddressNumberOnTheLeft());
    }

    @Override
    public @NotNull Optional<Integer> getMaxAddressNumberOnTheLeft() {
        return Optional.ofNullable(roadSegmentDocument.getMaxAddressNumberOnTheLeft());
    }

    @Override
    public @NotNull Optional<Integer> getMinAddressNumberOnTheRight() {
        return Optional.ofNullable(roadSegmentDocument.getMinAddressNumberOnTheRight());
    }

    @Override
    public @NotNull Optional<Integer> getMaxAddressNumberOnTheRight() {
        return Optional.ofNullable(roadSegmentDocument.getMaxAddressNumberOnTheRight());
    }

    @Override
    public @NotNull Optional<LocalizedString> getName() {
        return Optional.ofNullable(name);
    }

    @Override
    public @NotNull RoadClass getFeatureClass() {
        return roadSegmentDocument.getFeatureClass();
    }

    @Override
    public @NotNull LocationAccuracy getLocationAccuracy() {
        return roadSegmentDocument.getLocationAccuracy();
    }

    @Override
    public @NotNull LineString getLocation() {
        return roadSegmentDocument.getLocation();
    }

    @Override
    public @NotNull Long getId() {
        return roadSegmentDocument.getId();
    }

    @Override
    public @NotNull MaterialProvider getMaterialProvider() {
        return roadSegmentDocument.getMaterialProvider();
    }

    @Override
    public @NotNull Optional<LocalDate> getStartDate() {
        return Optional.ofNullable(roadSegmentDocument.getStartDate());
    }

    @Override
    public @NotNull Optional<LocalDate> getEndDate() {
        return Optional.ofNullable(roadSegmentDocument.getEndDate());
    }

    @Override
    public @NotNull Municipality getMunicipality() {
        return roadSegmentDocument.getMunicipality();
    }

    @Override
    public String toString() {
        return String.format("%s[id=%d, municipality=%s, name=%s, location=%s]", getClass().getSimpleName(), getId(),
                getMunicipality(), getName(), getLocation());
    }
}
