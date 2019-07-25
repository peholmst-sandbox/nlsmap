package net.pkhapps.nlsmap.mongodb.documents;

import com.vividsolutions.jts.geom.LineString;
import net.pkhapps.nlsmap.api.codes.*;
import net.pkhapps.nlsmap.mongodb.MongoConstants;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDate;

/**
 * TODO Implement me!
 */
public class RoadSegmentDocument {

    @BsonProperty(MongoConstants.RoadSegmentProperties.ROAD_ADMINISTRATOR)
    private RoadAdministrator administrator;

    @BsonProperty(MongoConstants.RoadSegmentProperties.COMPLETENESS_STATE)
    private CompletenessState completenessState;

    @BsonProperty(MongoConstants.RoadSegmentProperties.MAX_ADDRESS_NUMBER_LEFT)
    private Integer maxAddressNumberOnTheLeft;

    @BsonProperty(MongoConstants.RoadSegmentProperties.MAX_ADDRESS_NUMBER_RIGHT)
    private Integer maxAddressNumberOnTheRight;

    @BsonProperty(MongoConstants.RoadSegmentProperties.MIN_ADDRESS_NUMBER_LEFT)
    private Integer minAddressNumberOnTheLeft;

    @BsonProperty(MongoConstants.RoadSegmentProperties.MIN_ADDRESS_NUMBER_RIGHT)
    private Integer minAddressNumberOnTheRight;

    @BsonProperty(MongoConstants.RoadSegmentProperties.ROAD_NUMBER)
    private Integer roadNumber;

    @BsonProperty(MongoConstants.RoadSegmentProperties.ROAD_PART_NUMBER)
    private Integer roadPartNumber;

    @BsonProperty(MongoConstants.RoadSegmentProperties.LOCATION)
    private LineString location;

    @BsonProperty(MongoConstants.RoadSegmentProperties.END_DATE)
    private LocalDate endDate;

    @BsonProperty(MongoConstants.RoadSegmentProperties.START_DATE)
    private LocalDate startDate;

    @BsonProperty(MongoConstants.RoadSegmentProperties.NAME_FIN)
    private String nameFin;

    @BsonProperty(MongoConstants.RoadSegmentProperties.NAME_SWE)
    private String nameSwe;

    @BsonProperty(MongoConstants.RoadSegmentProperties.NAME_SME)
    private String nameSme;

    @BsonProperty(MongoConstants.RoadSegmentProperties.NAME_SMN)
    private String nameSmn;

    @BsonProperty(MongoConstants.RoadSegmentProperties.NAME_SMS)
    private String nameSms;

    @BsonProperty(MongoConstants.RoadSegmentProperties.LOCATION_ACCURACY)
    private LocationAccuracy locationAccuracy;

    @BsonId
    private Long id;

    @BsonProperty(MongoConstants.RoadSegmentProperties.MATERIAL_PROVIDER)
    private MaterialProvider materialProvider;

    @BsonProperty(MongoConstants.RoadSegmentProperties.MUNICIPALITY)
    private Municipality municipality;

    @BsonProperty(MongoConstants.RoadSegmentProperties.RELATIVE_ELEVATION)
    private RelativeElevation relativeElevation;

    @BsonProperty(MongoConstants.RoadSegmentProperties.FEATURE_CLASS)
    private RoadClass featureClass;

    @BsonProperty(MongoConstants.RoadSegmentProperties.SURFACE)
    private RoadSurface surface;

    @BsonProperty(MongoConstants.RoadSegmentProperties.TRAVEL_DIRECTION)
    private TravelDirection direction;

    public RoadAdministrator getAdministrator() {
        return administrator;
    }

    public void setAdministrator(RoadAdministrator administrator) {
        this.administrator = administrator;
    }

    public CompletenessState getCompletenessState() {
        return completenessState;
    }

    public void setCompletenessState(CompletenessState completenessState) {
        this.completenessState = completenessState;
    }

    public Integer getMaxAddressNumberOnTheLeft() {
        return maxAddressNumberOnTheLeft;
    }

    public void setMaxAddressNumberOnTheLeft(Integer maxAddressNumberOnTheLeft) {
        this.maxAddressNumberOnTheLeft = maxAddressNumberOnTheLeft;
    }

    public Integer getMaxAddressNumberOnTheRight() {
        return maxAddressNumberOnTheRight;
    }

    public void setMaxAddressNumberOnTheRight(Integer maxAddressNumberOnTheRight) {
        this.maxAddressNumberOnTheRight = maxAddressNumberOnTheRight;
    }

    public Integer getMinAddressNumberOnTheLeft() {
        return minAddressNumberOnTheLeft;
    }

    public void setMinAddressNumberOnTheLeft(Integer minAddressNumberOnTheLeft) {
        this.minAddressNumberOnTheLeft = minAddressNumberOnTheLeft;
    }

    public Integer getMinAddressNumberOnTheRight() {
        return minAddressNumberOnTheRight;
    }

    public void setMinAddressNumberOnTheRight(Integer minAddressNumberOnTheRight) {
        this.minAddressNumberOnTheRight = minAddressNumberOnTheRight;
    }

    public Integer getRoadNumber() {
        return roadNumber;
    }

    public void setRoadNumber(Integer roadNumber) {
        this.roadNumber = roadNumber;
    }

    public Integer getRoadPartNumber() {
        return roadPartNumber;
    }

    public void setRoadPartNumber(Integer roadPartNumber) {
        this.roadPartNumber = roadPartNumber;
    }

    public LineString getLocation() {
        return location;
    }

    public void setLocation(LineString location) {
        this.location = location;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getNameFin() {
        return nameFin;
    }

    public void setNameFin(String nameFin) {
        this.nameFin = nameFin;
    }

    public String getNameSwe() {
        return nameSwe;
    }

    public void setNameSwe(String nameSwe) {
        this.nameSwe = nameSwe;
    }

    public String getNameSme() {
        return nameSme;
    }

    public void setNameSme(String nameSme) {
        this.nameSme = nameSme;
    }

    public String getNameSmn() {
        return nameSmn;
    }

    public void setNameSmn(String nameSmn) {
        this.nameSmn = nameSmn;
    }

    public String getNameSms() {
        return nameSms;
    }

    public void setNameSms(String nameSms) {
        this.nameSms = nameSms;
    }

    public LocationAccuracy getLocationAccuracy() {
        return locationAccuracy;
    }

    public void setLocationAccuracy(LocationAccuracy locationAccuracy) {
        this.locationAccuracy = locationAccuracy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MaterialProvider getMaterialProvider() {
        return materialProvider;
    }

    public void setMaterialProvider(MaterialProvider materialProvider) {
        this.materialProvider = materialProvider;
    }

    public Municipality getMunicipality() {
        return municipality;
    }

    public void setMunicipality(Municipality municipality) {
        this.municipality = municipality;
    }

    public RelativeElevation getRelativeElevation() {
        return relativeElevation;
    }

    public void setRelativeElevation(RelativeElevation relativeElevation) {
        this.relativeElevation = relativeElevation;
    }

    public RoadClass getFeatureClass() {
        return featureClass;
    }

    public void setFeatureClass(RoadClass featureClass) {
        this.featureClass = featureClass;
    }

    public RoadSurface getSurface() {
        return surface;
    }

    public void setSurface(RoadSurface surface) {
        this.surface = surface;
    }

    public TravelDirection getDirection() {
        return direction;
    }

    public void setDirection(TravelDirection direction) {
        this.direction = direction;
    }
}
