package net.pkhapps.nlsmap.api.tiles;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

/**
 * Interface for a renderer of a {@link RasterMapTile}.
 */
@FunctionalInterface
public interface RasterMapTileRenderer {

    /**
     * Renders the given map tile.
     *
     * @param mapTile           the map tile.
     * @param rasterContentType the content type of the raster image.
     * @param raster            an input stream containing the raster image. This stream will be automatically closed.
     * @throws IOException if there is an error reading from the input stream.
     */
    void render(@NotNull RasterMapTile mapTile, @NotNull String rasterContentType,
                @NotNull InputStream raster) throws IOException;
}
