package net.pkhapps.nlsmap.api.query;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Point;
import net.pkhapps.nlsmap.api.codes.Municipality;
import net.pkhapps.nlsmap.api.features.RoadSegment;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Base criteria interface for geographical feature queries.
 *
 * @see CriteriaBuilder
 */
public interface Criteria {

    /**
     * The envelope that the features must either overlap or be completely enclosed in.
     *
     * @see RoadSegment#getLocation()
     */
    @NotNull Optional<Envelope> getEnvelope();

    /**
     * A point that the features must be close to. The implementation is free to decide how close is close
     * enough.
     */
    @NotNull Optional<Point> getPoint();

    /**
     * The municipality that the features must belong to.
     */
    @NotNull Optional<Municipality> getMunicipality();
}
