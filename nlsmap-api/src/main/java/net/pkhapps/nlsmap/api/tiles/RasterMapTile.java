package net.pkhapps.nlsmap.api.tiles;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Map tile representing a raster tile that can be rendered using a {@link RasterMapTileRenderer}.
 */
public interface RasterMapTile extends MapTile {

    /**
     * Renders the tile using the given renderer.
     *
     * @throws IOException if there is a problem reading the map tile tiles image.
     */
    void render(@NotNull RasterMapTileRenderer renderer) throws IOException;
}
