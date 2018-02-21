package net.pkhapps.nlsmap.api.features;

import com.vividsolutions.jts.geom.LineString;

/**
 * Interface for features that have a {@link LineString} location.
 */
public interface HasLineStringLocation extends HasLocation<LineString> {
}
