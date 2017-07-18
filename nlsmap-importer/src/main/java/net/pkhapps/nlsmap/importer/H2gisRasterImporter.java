package net.pkhapps.nlsmap.importer;

import org.geotools.geometry.Envelope2D;
import org.opengis.geometry.Envelope;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
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

    private final Connection connection;

    private static final String CONTENT_TYPE_PNG = "image/png";

    public H2gisRasterImporter(Connection connection) {
        this.connection = connection;
    }

    public void importRasterFiles(File directory, String tableName, int tileWidth, int tileHeight, int srid) throws Exception {
        try (Worker worker = new Worker(tableName, srid)) {
            // TODO Skip tiles that have already been imported based on the timestamp
            DirectoryVisitor.visit(directory, "*.png", png -> TileUtils.createTiles(png, tileWidth, tileHeight, worker::storeTile));
        }
    }

    private class Worker implements AutoCloseable {
        private final String tableName;
        private final int srid;
        private PreparedStatement insertTile;

        private static final int INSERT_TILE_SOURCE = 1;
        private static final int INSERT_TILE_X = 2;
        private static final int INSERT_TILE_Y = 3;
        private static final int INSERT_TILE_UPDATED = 4;
        private static final int INSERT_TILE_X_MIN = 5;
        private static final int INSERT_TILE_Y_MIN = 6;
        private static final int INSERT_TILE_X_MAX = 7;
        private static final int INSERT_TILE_Y_MAX = 8;
        private static final int INSERT_TILE_SRID = 9;
        private static final int INSERT_TILE_IMAGE = 10;
        private static final int INSERT_TILE_IMAGE_TYPE = 11;

        public Worker(String tableName, int srid) throws Exception {
            this.tableName = tableName;
            this.srid = srid;
            insertTile = connection.prepareStatement("INSERT INTO " + tableName + " (source, tile_x, tile_y, last_updated, geom, image, image_type) VALUES (?, ?, ?, ?, ST_MakeEnvelope(?, ?, ?, ?, ?), ?, ?)");
        }

        private void storeTile(String name, int x, int y, Raster tile, ColorModel colorModel, Envelope2D envelope) throws IOException {
            try {
                BufferedImage img = new BufferedImage(colorModel, tile.createCompatibleWritableRaster(),
                        colorModel.isAlphaPremultiplied(), null);
                // ByteArray streams do not need to be closed
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ImageIO.write(img, "png", bos);
                insertTile.setString(INSERT_TILE_SOURCE, name);
                insertTile.setInt(INSERT_TILE_X, x);
                insertTile.setInt(INSERT_TILE_Y, y);
                insertTile.setTimestamp(INSERT_TILE_UPDATED, Timestamp.from(Instant.now()));
                insertTile.setDouble(INSERT_TILE_X_MIN, envelope.getMinX());
                insertTile.setDouble(INSERT_TILE_Y_MIN, envelope.getMinY());
                insertTile.setDouble(INSERT_TILE_X_MAX, envelope.getMaxX());
                insertTile.setDouble(INSERT_TILE_Y_MAX, envelope.getMaxY());
                insertTile.setInt(INSERT_TILE_SRID, srid);
                insertTile.setBlob(INSERT_TILE_IMAGE, new ByteArrayInputStream(bos.toByteArray()));
                insertTile.setString(INSERT_TILE_IMAGE_TYPE, CONTENT_TYPE_PNG);
                System.out.println(envelope);
                //insertTile.execute(); // TODO Change to batch
            } catch (Exception ex) {
                throw new IOException(String.format("Error storing tile %s(%d, %d)", name, x, y), ex);
            }
        }

        @Override
        public void close() throws Exception {
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
            importer.importRasterFiles(new File("/Users/petterprivate/Google Drive/Maps/taustakartta_1_5000"),
                    "bg_raster_1_5000", 200, 200, 3067); // ETRS89 / TM35FIN
        }
    }
}
