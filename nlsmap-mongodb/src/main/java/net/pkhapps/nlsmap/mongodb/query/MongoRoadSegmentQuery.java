package net.pkhapps.nlsmap.mongodb.query;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.geojson.GeoJsonReader;
import net.pkhapps.nlsmap.api.codes.Municipality;
import net.pkhapps.nlsmap.api.features.RoadSegment;
import net.pkhapps.nlsmap.api.features.query.RoadSegmentQuery;
import net.pkhapps.nlsmap.mongodb.MongoConstants;
import net.pkhapps.nlsmap.mongodb.codecs.CodeCodecProvider;
import net.pkhapps.nlsmap.mongodb.documents.FeatureClassModels;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * TODO Document me!
 */
public class MongoRoadSegmentQuery implements RoadSegmentQuery {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoRoadSegmentQuery.class);
    private final GeoJsonReader geoJsonReader = new GeoJsonReader();
    private MongoCollection<RoadSegmentImpl> mongoCollection;

    public MongoRoadSegmentQuery(@NotNull MongoDatabase mongoDatabase) {
        PojoCodecProvider codecProvider = PojoCodecProvider.builder()
                .register(FeatureClassModels.ROAD_SEGMENT_CLASS_MODEL)
                .build();
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(mongoDatabase.getCodecRegistry(),
                CodecRegistries.fromProviders(codecProvider, new CodeCodecProvider()));
        mongoCollection = mongoDatabase.getCollection(MongoConstants.ROAD_SEGMENTS_COLLECTION, RoadSegmentImpl.class)
                .withCodecRegistry(codecRegistry);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static <T> @NotNull T getOrThrow(@NotNull Optional<T> optional) throws DocumentMappingException {
        return optional.orElseThrow(() -> new DocumentMappingException("A required field did not contain a value"));
    }

    @Override
    public @NotNull List<RoadSegment> findByCriteria(@NotNull RoadSegmentQuery.RoadSegmentCriteria criteria) {
        return toBsonFilter(criteria)
                .map(mongoCollection::find)
                .map(d -> d.into((List<RoadSegment>) new ArrayList<RoadSegment>()))
                .orElse(Collections.emptyList());
    }

    @Override
    public @NotNull RoadSegmentQuery.RoadSegmentCriteriaBuilder newCriteriaBuilder() {
        return new RoadSegmentCriteriaBuilderImpl();
    }

    private @NotNull Optional<Bson> toBsonFilter(@NotNull RoadSegmentQuery.RoadSegmentCriteria criteria) {
        final List<Bson> filterList = new ArrayList<>();

        criteria.getName().map(Filters::text).ifPresent(filterList::add);
        // TODO Implement the rest of the filters

        if (filterList.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(Filters.and(filterList));
        }
    }
/*
    private @NotNull List<RoadSegment> toRoadSegments(@NotNull FindIterable<BsonDocument> queryResult) {
        List<RoadSegment> result = new ArrayList<>(); // TODO Limit result
        queryResult.forEach((Consumer<BsonDocument>) document -> {
            try {
                result.add(toRoadSegment(document));
            } catch (DocumentMappingException ex) {
                LOGGER.error("The document {} could not be mapped to a RoadSegment POJO", document);
            }
        });
        return result;
    }

    private @NotNull RoadSegment toRoadSegment(@NotNull BsonDocument document) throws DocumentMappingException {
        RoadSegmentImpl roadSegment = new RoadSegmentImpl();
        roadSegment.setId(getOrThrow(getLong(document, MongoConstants.DOCUMENT_ID)));
        roadSegment.setRelativeElevation(getOrThrow(getString(document, RoadSegmentProperties.RELATIVE_ELEVATION)
                .flatMap(RelativeElevation::findByCode)));
        roadSegment.setCompletenessState(getOrThrow(getString(document, RoadSegmentProperties.COMPLETENESS_STATE)
                .flatMap(CompletenessState::findByCode)));
        roadSegment.setSurface(getOrThrow(getString(document, RoadSegmentProperties.SURFACE)
                .flatMap(RoadSurface::findByCode)));
        roadSegment.setDirection(getOrThrow(getString(document, RoadSegmentProperties.TRAVEL_DIRECTION)
                .flatMap(TravelDirection::findByCode)));
        roadSegment.setFeatureClass(getOrThrow(getString(document, RoadSegmentProperties.FEATURE_CLASS)
                .flatMap(RoadClass::findByCode)));
        roadSegment.setMaterialProvider(getOrThrow(getString(document, RoadSegmentProperties.MATERIAL_PROVIDER)
                .flatMap(MaterialProvider::findByCode)));
        roadSegment.setMunicipality(getOrThrow(getString(document, RoadSegmentProperties.MUNICIPALITY)
                .flatMap(Municipality::findByCode)));
        roadSegment.setLocation(toGeometry(LineString.class,
                getOrThrow(getField(document, RoadSegmentProperties.LOCATION, BsonDocument::getDocument))));


        //System.out.println(document.get(MongoConstants.RoadSegmentProperties.LOCATION).getBsonType());
        return roadSegment;
    }

    private @NotNull Optional<Long> getLong(@NotNull BsonDocument document, @NotNull String fieldName) {
        return getField(document, fieldName, BsonDocument::getInt64).map(BsonInt64::getValue);
    }

    private @NotNull Optional<String> getString(@NotNull BsonDocument document, @NotNull String fieldName) {
        return getField(document, fieldName, BsonDocument::getString).map(BsonString::getValue);
    }

    private <B extends BsonValue> @NotNull Optional<B> getField(@NotNull BsonDocument document,
                                                                @NotNull String fieldName,
                                                                @NotNull BiFunction<BsonDocument, String, B> fieldAccessor) {
        if (document.containsKey(fieldName)) {
            return Optional.of(fieldAccessor.apply(document, fieldName));
        } else {
            return Optional.empty();
        }
    }

    private <G extends Geometry> @NotNull G toGeometry(@NotNull Class<G> geometryClass, @NotNull BsonDocument document)
            throws DocumentMappingException {
        try {
            return geometryClass.cast(geoJsonReader.read(document.toJson()));
        } catch (Exception ex) {
            throw new DocumentMappingException("Could not convert GeoJson document to Geometry", ex);
        }
    }*/

    static class RoadSegmentCriteriaImpl implements RoadSegmentCriteria {

        private Envelope envelope;
        private Point point;
        private Municipality municipality;
        private String name;
        private Integer roadNumber;
        private Integer addressNumber;

        @Override
        public @NotNull Optional<Envelope> getEnvelope() {
            return Optional.ofNullable(envelope);
        }

        @Override
        public @NotNull Optional<Point> getPoint() {
            return Optional.ofNullable(point);
        }

        @Override
        public @NotNull Optional<Municipality> getMunicipality() {
            return Optional.ofNullable(municipality);
        }

        @Override
        public @NotNull Optional<String> getName() {
            return Optional.ofNullable(name);
        }

        @Override
        public @NotNull Optional<Integer> getRoadNumber() {
            return Optional.ofNullable(roadNumber);
        }

        @Override
        public @NotNull Optional<Integer> getAddressNumber() {
            return Optional.ofNullable(addressNumber);
        }
    }

    static class RoadSegmentCriteriaBuilderImpl implements RoadSegmentCriteriaBuilder {

        private RoadSegmentCriteriaImpl criteria = new RoadSegmentCriteriaImpl();

        @Override
        public @NotNull RoadSegmentQuery.RoadSegmentCriteriaBuilder within(@NotNull Envelope envelope) {
            criteria.envelope = envelope;
            return this;
        }

        @Override
        public @NotNull RoadSegmentQuery.RoadSegmentCriteriaBuilder closeTo(@NotNull Point point) {
            criteria.point = point;
            return this;
        }

        @Override
        public @NotNull RoadSegmentQuery.RoadSegmentCriteriaBuilder within(@NotNull Municipality municipality) {
            criteria.municipality = municipality;
            return this;
        }

        @Override
        public @NotNull RoadSegmentQuery.RoadSegmentCriteriaBuilder byName(@NotNull String name) {
            criteria.name = name;
            return this;
        }

        @Override
        public @NotNull RoadSegmentQuery.RoadSegmentCriteriaBuilder byRoadNumber(@NotNull Integer roadNumber) {
            criteria.roadNumber = roadNumber;
            return this;
        }

        @Override
        public @NotNull RoadSegmentQuery.RoadSegmentCriteriaBuilder byAddressNumber(@NotNull Integer addressNumber) {
            criteria.addressNumber = addressNumber;
            return this;
        }

        @Override
        public @NotNull RoadSegmentQuery.RoadSegmentCriteria build() {
            return criteria;
        }
    }
}
