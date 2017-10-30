package net.pkhapps.nlsmap.importer;

import net.pkhapps.nlsmap.api.CoordinateReferenceSystems;
import net.pkhapps.nlsmap.jdbc.raster.H2gisZoomLevel;
import org.geotools.geometry.Envelope2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Properties;

/**
 * TODO Document me
 */
public class H2gisRasterImporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(H2gisRasterImporter.class);
    private final Connection connection;

    private static final String CONTENT_TYPE_PNG = "image/png";

    public H2gisRasterImporter(Connection connection) {
        this.connection = connection;
    }

    public void importRasterFiles(File directory, H2gisZoomLevel zoomLevel, int srid) throws Exception {
        LOGGER.info("Importing raster files from directory [{}] using zoom level [{}]", directory, zoomLevel);
        try (Worker worker = new Worker(zoomLevel.getTableName(), srid)) {
            // TODO Skip tiles that have already been imported based on the timestamp
            // TODO Verify scales and tile sizes of ZoomLevel with the ones coming from the files
            DirectoryVisitor.visit(directory, "*.png", png -> TileUtils.createTiles(png, zoomLevel.getTileWidth(),
                    zoomLevel.getTileHeight(), worker::storeTile));
        }
    }

    private class Worker implements AutoCloseable {
        private final int srid;
        private PreparedStatement insertTile;
        private int batchQueueCount = 0;

        public Worker(String tableName, int srid) throws Exception {
            this.srid = srid;
            insertTile = connection.prepareStatement("INSERT INTO " + tableName + " (id, map_sheet, " +
                    "last_updated, geom, min_x, min_y, max_x, max_y, srid, image, image_type) " +
                    "VALUES (?, ?, ?, ST_MakeEnvelope(?, ?, ?, ?, ?), ?, ?, ?, ?, ?, ?, ?)");
        }

        private void storeTile(String name, int x, int y, Raster tile, ColorModel colorModel, Envelope2D envelope)
                throws IOException {
            try {
                WritableRaster writableRaster = tile.createCompatibleWritableRaster();
                writableRaster.setDataElements(0, 0, tile);
                BufferedImage img = new BufferedImage(colorModel, writableRaster,
                        colorModel.isAlphaPremultiplied(), null);
                // ByteArray streams do not need to be closed
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ImageIO.write(img, "png", bos);
                int parameterIx = 1;
                insertTile.setString(parameterIx++, String.format("%s:%d:%d", name, x, y));
                insertTile.setString(parameterIx++, name);
                insertTile.setTimestamp(parameterIx++, Timestamp.from(Instant.now()));
                {
                    // Geom
                    insertTile.setDouble(parameterIx++, envelope.getMinX());
                    insertTile.setDouble(parameterIx++, envelope.getMinY());
                    insertTile.setDouble(parameterIx++, envelope.getMaxX());
                    insertTile.setDouble(parameterIx++, envelope.getMaxY());
                    insertTile.setInt(parameterIx++, srid);
                }
                {
                    // Ordinary columns
                    insertTile.setDouble(parameterIx++, envelope.getMinX());
                    insertTile.setDouble(parameterIx++, envelope.getMinY());
                    insertTile.setDouble(parameterIx++, envelope.getMaxX());
                    insertTile.setDouble(parameterIx++, envelope.getMaxY());
                    insertTile.setInt(parameterIx++, srid);
                }
                insertTile.setBlob(parameterIx++, new ByteArrayInputStream(bos.toByteArray()));
                insertTile.setString(parameterIx, CONTENT_TYPE_PNG);
                insertTile.addBatch();
                batchQueueCount++;

                if (batchQueueCount >= 100) {
                    insertTile.executeBatch();
                    batchQueueCount = 0;
                }
            } catch (Exception ex) {
                throw new IOException(String.format("Error storing tile %s(%d, %d)", name, x, y), ex);
            }
        }

        @Override
        public void close() throws Exception {
            if (batchQueueCount > 0) {
                insertTile.executeBatch();
            }
            insertTile.close();
        }
    }


    /**
     * TODO Delete me
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String url = "jdbc:h2:~/nlsmap.test";
        Properties props = new Properties();
        //props.setProperty("user", "nlsmap");
        //props.setProperty("password", "nlsmap");
        try (Connection conn = DriverManager.getConnection(url, props)) {
            H2gisDatabaseInitializer.initialize(conn);
            H2gisRasterImporter importer = new H2gisRasterImporter(conn);
            importer.importRasterFiles(
                    new File("/Users/peholmst/Google Drive/Maps/taustakartta_1_5000"),
                    H2gisZoomLevel.ZL5000, CoordinateReferenceSystems.ETRS89_TM35FIN_SRID);

            // TODO Make another zoom level between 1:5000 and 1:10000 that is based on the 1:5000

            importer.importRasterFiles(
                    new File("/Users/peholmst/Google Drive/Maps/taustakartta_1_10000"),
                    H2gisZoomLevel.ZL10000, CoordinateReferenceSystems.ETRS89_TM35FIN_SRID);
            importer.importRasterFiles(
                    new File("/Users/peholmst/Google Drive/Maps/taustakartta_1_20000"),
                    H2gisZoomLevel.ZL20000, CoordinateReferenceSystems.ETRS89_TM35FIN_SRID);
            importer.importRasterFiles(
                    new File("/Users/peholmst/Google Drive/Maps/taustakartta_1_40000"),
                    H2gisZoomLevel.ZL40000, CoordinateReferenceSystems.ETRS89_TM35FIN_SRID);
            importer.importRasterFiles(
                    new File("/Users/peholmst/Google Drive/Maps/taustakartta_1_80000"),
                    H2gisZoomLevel.ZL80000, CoordinateReferenceSystems.ETRS89_TM35FIN_SRID);
            importer.importRasterFiles(
                    new File("/Users/peholmst/Google Drive/Maps/taustakartta_1_160000"),
                    H2gisZoomLevel.ZL160000, CoordinateReferenceSystems.ETRS89_TM35FIN_SRID);
            importer.importRasterFiles(
                    new File("/Users/peholmst/Google Drive/Maps/taustakartta_1_320000"),
                    H2gisZoomLevel.ZL320000, CoordinateReferenceSystems.ETRS89_TM35FIN_SRID);
        }
    }
}
