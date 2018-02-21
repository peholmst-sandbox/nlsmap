package net.pkhapps.nlsmap.api.features;

import com.vividsolutions.jts.geom.Point;

/**
 * Interface for features that have a {@link Point} location.
 */
public interface HasPointLocation extends HasLocation<Point> {
}
