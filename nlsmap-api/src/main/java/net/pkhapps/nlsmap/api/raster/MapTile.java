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
     * TODO Redesign to make the painting separate from the actual tile
     *
     * @return
     */
    void paint(Object context, int x, int y);

    /**
     * Returns the width of the tile in pixels.
     */
    int getWidth();

    /**
     * Returns the height of the tile in pixels.
     */
    int getHeight();
}
