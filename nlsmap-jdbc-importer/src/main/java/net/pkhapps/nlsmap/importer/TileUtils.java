package net.pkhapps.nlsmap.importer;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.geometry.Envelope2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * TODO Document me!
 */
public final class TileUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TileUtils.class.getName());

    private TileUtils() {
        // NOP
    }

    /**
     * @param path
     * @param tileWidth
     * @param tileHeight
     * @param consumer
     * @throws IOException
     */
    public static void createTiles(Path path, int tileWidth, int tileHeight, TileConsumer consumer) throws IOException {
        final File file = path.toFile();
        LOGGER.info("Reading raster from [{}]", file);
        final AbstractGridFormat format = GridFormatFinder.findFormat(file);
        final GridCoverage2DReader reader = format.getReader(file);
        try {
            final GridCoverage2D coverage = reader.read(null);
            final RenderedImage image = coverage.getRenderedImage();

            final int width = image.getWidth();
            final int height = image.getHeight();

            if (width % tileWidth != 0) {
                throw new IllegalArgumentException("Raster width is not evenly divisible by the tile width");
            }
            if (height % tileHeight != 0) {
                throw new IllegalArgumentException("Raster height is not evenly divisible by the tile height");
            }

            final Envelope2D envelope = coverage.getEnvelope2D();
            final double upperLeftX = envelope.getMinX();
            final double upperLeftY = envelope.getMaxY();
            final double scaleX = envelope.getWidth() / width;
            final double scaleY = -envelope.getHeight() / height;

            final int cols = width / tileWidth;
            final int rows = height / tileHeight;

            LOGGER.debug("Raster will be split up into {} tiles, scale X is {} and scale Y is {}", cols * rows, scaleX,
                    scaleY);

            final Raster raster = image.getData();
            // Process tiles
            for (int y = 0; y < rows; ++y) {
                for (int x = 0; x < cols; ++x) {
                    final Rectangle tileRect = new Rectangle(x * tileWidth, y * tileHeight,
                            tileWidth, tileHeight);
                    final Raster tileRaster = raster.createChild(tileRect.x, tileRect.y, tileRect.width,
                            tileRect.height, 0, 0, null);

                    consumer.consume(coverage.getName().toString(), x, y, tileRaster, image.getColorModel(),
                            new Envelope2D(envelope.getCoordinateReferenceSystem(), upperLeftX + scaleX * tileRect.x,
                                    upperLeftY + scaleY * tileRect.y,
                                    tileRect.width * scaleX, tileRect.height * scaleY));
                }
            }
        } finally {
            reader.dispose();
        }
    }

    /**
     *
     */
    @FunctionalInterface
    public interface TileConsumer {

        void consume(String name, int x, int y, Raster tile, ColorModel colorModel, Envelope2D envelope) throws IOException;
    }
}
