package net.pkhapps.nlsmap.api.tiles.query;

import java.io.Serializable;

/**
 * Interface representing the meta data for a zoom level.
 */
public interface ZoomLevelMetaData extends Serializable {

    /**
     * The zoom level. This number is implementation specific.
     */
    int getZoomLevel();

    /**
     * The scaling factor of the X axis. By multiplying the tile width in pixels with this value, you will get the width
     * of the tile in geographical units.
     */
    double getScaleX();

    /**
     * The scaling factor of the Y axis. By multiplying the tile height in pixels with this value, you will get the
     * height of the tile in geographical units.
     */
    double getScaleY();

    /**
     * The width of the tiles on this zoom level in pixels.
     */
    int getTileWidth();

    /**
     * The height of the tiles on this zoom level in pixels.
     */
    int getTileHeight();
}
