package net.pkhapps.nlsmap.mongodb.query;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Point;
import net.pkhapps.nlsmap.api.codes.Municipality;
import net.pkhapps.nlsmap.api.features.RoadSegment;
import net.pkhapps.nlsmap.api.query.RoadSegmentQuery;
import net.pkhapps.nlsmap.mongodb.MongoConstants;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * TODO Document me!
 */
public class MongoRoadSegmentQuery implements RoadSegmentQuery {

    private MongoCollection<BsonDocument> mongoCollection;

    public MongoRoadSegmentQuery(@NotNull MongoDatabase mongoDatabase) {
        mongoCollection = mongoDatabase.getCollection(MongoConstants.ROAD_SEGMENTS_COLLECTION, BsonDocument.class);
    }

    @Override
    public @NotNull List<RoadSegment> findByCriteria(@NotNull RoadSegmentQuery.Criteria criteria) {
        return toBsonFilter(criteria)
                .map(mongoCollection::find)
                .map(this::toRoadSegments)
                .orElse(Collections.emptyList());
    }

    @Override
    public @NotNull RoadSegmentQuery.CriteriaBuilder newCriteriaBuilder() {
        return new CriteriaBuilderImpl();
    }

    private @NotNull Optional<Bson> toBsonFilter(@NotNull Criteria criteria) {
        final List<Bson> filterList = new ArrayList<>();

        criteria.getName().map(Filters::text).ifPresent(filterList::add);
        // TODO Implement the rest of the filters

        if (filterList.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(Filters.and(filterList));
        }
    }

    private @NotNull List<RoadSegment> toRoadSegments(@NotNull FindIterable<BsonDocument> queryResult) {
        List<RoadSegment> result = new ArrayList<>(); // TODO Limit result
        queryResult.forEach((Consumer<BsonDocument>) document -> result.add(toRoadSegment(document)));
        return result;
    }

    private @NotNull RoadSegment toRoadSegment(@NotNull BsonDocument document) {
        RoadSegmentImpl roadSegment = new RoadSegmentImpl();
        roadSegment.id = document.get(MongoConstants.DOCUMENT_ID).asInt64().getValue();

        return roadSegment;
    }

    static class CriteriaImpl implements Criteria {

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

    static class CriteriaBuilderImpl implements CriteriaBuilder {

        private CriteriaImpl criteria = new CriteriaImpl();

        @Override
        public @NotNull RoadSegmentQuery.CriteriaBuilder within(@NotNull Envelope envelope) {
            criteria.envelope = envelope;
            return this;
        }

        @Override
        public @NotNull RoadSegmentQuery.CriteriaBuilder closeTo(@NotNull Point point) {
            criteria.point = point;
            return this;
        }

        @Override
        public @NotNull RoadSegmentQuery.CriteriaBuilder within(@NotNull Municipality municipality) {
            criteria.municipality = municipality;
            return this;
        }

        @Override
        public @NotNull RoadSegmentQuery.CriteriaBuilder byName(@NotNull String name) {
            criteria.name = name;
            return this;
        }

        @Override
        public @NotNull RoadSegmentQuery.CriteriaBuilder byRoadNumber(@NotNull Integer roadNumber) {
            criteria.roadNumber = roadNumber;
            return this;
        }

        @Override
        public @NotNull RoadSegmentQuery.CriteriaBuilder byAddressNumber(@NotNull Integer addressNumber) {
            criteria.addressNumber = addressNumber;
            return this;
        }

        @Override
        public @NotNull RoadSegmentQuery.Criteria build() {
            return criteria;
        }
    }
}
