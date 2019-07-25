package net.pkhapps.nlsmap.api.features.query;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Point;
import net.pkhapps.nlsmap.api.codes.Municipality;
import org.jetbrains.annotations.NotNull;

/**
 * Base interface for builders of {@link Criteria} instances.
 */
public interface CriteriaBuilder<C extends Criteria, B extends CriteriaBuilder<C, B>> {

    /**
     * @see Criteria#getEnvelope()
     */
    @NotNull B boundedBy(@NotNull Envelope envelope);

    /**
     * @see Criteria#getPoint()
     */
    @NotNull B closeTo(@NotNull Point point);

    /**
     * @see Criteria#getMunicipality()
     */
    @NotNull B inMunicipality(@NotNull Municipality municipality);

    /**
     * Creates and returns a new criteria instance.
     */
    @NotNull C build();
}
