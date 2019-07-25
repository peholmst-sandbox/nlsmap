package net.pkhapps.nlsmap.api.tiles;

import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.util.Collection;
import java.util.Optional;

/**
 * TODO Document me!
 *
 * Can assume one provider per panel, to allow for optimization.
 */
@Deprecated
public interface MapTileProvider {

    int getMaxZoomLevel();

    int getMinZoomLevel();

    // TODO Method for getting recommended zoom level for different objects (cities, streets, buildings, etc.)

    CoordinateReferenceSystem getCRS();

    double getScaleX(int zoomLevel);

    double getScaleY(int zoomLevel);

    int getTileWidth(int zoomLevel);

    int getTileHeight(int zoomLevel);

    Collection<MapTileId> getTileIdentifiers(int zoomLevel, Envelope envelope);

    Optional<MapTile> getTile(MapTileId tileIdentifier);
}
