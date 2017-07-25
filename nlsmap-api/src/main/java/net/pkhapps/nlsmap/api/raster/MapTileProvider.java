package net.pkhapps.nlsmap.api.raster;

import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.util.Collection;
import java.util.Optional;

/**
 * TODO Document me!
 */
public interface MapTileProvider {

    int getMaxZoomLevel();

    int getMinZoomLevel();

    // TODO Method for getting recommended zoom level for different objects (cities, streets, buildings, etc.)

    CoordinateReferenceSystem getCRS();

    double getScaleX(int zoomLevel);

    double getScaleY(int zoomLevel);

    int getTileWidth(int zoomLevel);

    int getTileHeight(int zoomLevel);

    Collection<MapTileIdentifier> getTileIdentifiers(int zoomLevel, Envelope envelope);

    Optional<MapTile> getTile(MapTileIdentifier tileIdentifier);
}
