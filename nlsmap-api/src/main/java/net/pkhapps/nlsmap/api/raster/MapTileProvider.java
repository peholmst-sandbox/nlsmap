package net.pkhapps.nlsmap.api.raster;

import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * TODO Document me!
 */
public interface MapTileProvider {

    int getMaxZoomLevel();

    int getMinZoomLevel();

    CoordinateReferenceSystem getCRS();

    double getScaleX(int zoomLevel);

    double getScaleY(int zoomLevel);

    // TODO Method for getting recommended zoom level for different objects (cities, streets, buildings, etc.)

    Optional<MapTileIdentifier> getTileIdentifier(int zoomLevel, DirectPosition position);

    Optional<MapTile> getTile(MapTileIdentifier tileIdentifier);

    Map<RelativeLocation, MapTileIdentifier> getAdjoiningTileIdentifiers(MapTileIdentifier pivot, RelativeLocation... relativeLocations);

    Collection<MapTile> getMapTiles(Iterable<MapTileIdentifier> mapTileIdentifiers);

    /**
     * TODO Document me!
     */
    enum RelativeLocation {
        NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST
    }
}
