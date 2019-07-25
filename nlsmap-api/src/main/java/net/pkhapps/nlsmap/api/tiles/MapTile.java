package net.pkhapps.nlsmap.api.tiles;

import org.jetbrains.annotations.NotNull;
import org.opengis.geometry.BoundingBox;

import java.io.Serializable;

/**
 * Interface defining a map tile.
 */
public interface MapTile extends Serializable {

    /**
     * The unique identifier of the tile.
     */
    @NotNull MapTileId getIdentifier();

    /**
     * The zoom level of the tile. The zooming scale is implementation specific.
     */
    int getZoomLevel();

    /**
     * A copy of the bounds of the tile.
     */
    BoundingBox getEnvelope();

    /**
     * The width of the tile in pixels.
     */
    int getWidth();

    /**
     * The height of the tile in pixels.
     */
    int getHeight();
}
