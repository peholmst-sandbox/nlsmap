package net.pkhapps.nlsmap.api.raster;

import org.opengis.geometry.Envelope;

/**
 * Interface defining a map tile...
 */
public interface MapTile {

    /**
     * Returns the identifier of the tile.
     */
    MapTileIdentifier getIdentifier();

    /**
     * Returns the envelope (bounds) of the tile.
     */
    Envelope getEnvelope();

    /**
     * TODO Document me
     *
     * @return
     */
    void paint(Object context, int offsetX, int offsetY);

    /**
     * Returns the width of the tile in pixels.
     */
    int getWidth();

    /**
     * Returns the height of the tile in pixels.
     */
    int getHeight();
}
