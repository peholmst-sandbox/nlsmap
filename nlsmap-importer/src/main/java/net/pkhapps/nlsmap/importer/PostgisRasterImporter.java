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
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

/**
 * TODO Implement me
 */
public class PostgisRasterImporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostgisRasterImporter.class);
    private final File directory;
    private final Connection connection;
    private final String tableName;
    private final int srid;
    private final short tileWidth;
    private final short tileHeight;

    @Deprecated
    private PreparedStatement makeEmptyRaster;
    @Deprecated
    private PreparedStatement addBand;
    @Deprecated
    private PreparedStatement setValues;
    @Deprecated
    private PreparedStatement setColorMap;

    private PreparedStatement importTile;
    private PreparedStatement importTileAsWKB;

    /**
     * @param directory
     * @param connection
     * @param tableName
     * @param srid
     * @param tileWidth
     * @param tileHeight
     */
    public PostgisRasterImporter(File directory, Connection connection, String tableName, int srid, short tileWidth, short tileHeight) {
        this.directory = Objects.requireNonNull(directory, "directory must not be null");
        this.connection = Objects.requireNonNull(connection, "connection must not be null");
        this.tableName = Objects.requireNonNull(tableName, "tableName must not be null");
        this.srid = srid;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    private static final int MAKE_EMPTY_RASTER_PARAM_TILE_ID = 1;
    private static final int MAKE_EMPTY_RASTER_PARAM_WIDTH = 2;
    private static final int MAKE_EMPTY_RASTER_PARAM_HEIGHT = 3;
    private static final int MAKE_EMPTY_RASTER_PARAM_UPPER_LEFT_X = 4;
    private static final int MAKE_EMPTY_RASTER_PARAM_UPPER_LEFT_Y = 5;
    private static final int MAKE_EMPTY_RASTER_PARAM_SCALE_X = 6;
    private static final int MAKE_EMPTY_RASTER_PARAM_SCALE_Y = 7;
    private static final int MAKE_EMPTY_RASTER_PARAM_SKEW_X = 8;
    private static final int MAKE_EMPTY_RASTER_PARAM_SKEW_Y = 9;
    private static final int MAKE_EMPTY_RASTER_PARAM_SRID = 10;
    private static final int ADD_BAND_PARAM_TILE_ID = 1;
    private static final int SET_VALUES_PARAM_ROWS = 1;
    private static final int SET_VALUES_PARAM_TILE_ID = 2;
    private static final int SET_COLOR_MAP_PARAM_COLOR_MAP = 1;
    private static final int SET_COLOR_MAP_PARAM_TILE_ID = 2;

    private static final int IMPORT_TILE_PARAM_TILE_ID = 1;
    private static final int IMPORT_TILE_PARAM_WIDTH = 2;
    private static final int IMPORT_TILE_PARAM_HEIGHT = 3;
    private static final int IMPORT_TILE_PARAM_UPPER_LEFT_X = 4;
    private static final int IMPORT_TILE_PARAM_UPPER_LEFT_Y = 5;
    private static final int IMPORT_TILE_PARAM_SCALE_X = 6;
    private static final int IMPORT_TILE_PARAM_SCALE_Y = 7;
    private static final int IMPORT_TILE_PARAM_SKEW_X = 8;
    private static final int IMPORT_TILE_PARAM_SKEW_Y = 9;
    private static final int IMPORT_TILE_PARAM_SRID = 10;
    private static final int IMPORT_TILE_PARAM_ROWS = 11;
    private static final int IMPORT_TILE_PARAM_COLOR_MAP = 12;

    /**
     * @throws IOException
     * @throws SQLException
     */
    public void importFiles() throws IOException, SQLException {
        // TODO Timestamp for when the tile was inserted or updated
        makeEmptyRaster = connection.prepareStatement("INSERT INTO " + tableName + " (tile_id, rast) VALUES (?, ST_MakeEmptyRaster(?, ?, ?, ?, ?, ?, ?, ?, ?))");
        addBand = connection.prepareStatement("UPDATE " + tableName + " SET rast = ST_AddBand(rast,'8BUI'::text,0) WHERE tile_id = ?");
        setValues = connection.prepareStatement("UPDATE " + tableName + " SET rast = ST_SetValues(rast, 1, 1, 1, ?::double precision[][]) WHERE tile_id = ?");
        setColorMap = connection.prepareStatement("UPDATE " + tableName + " SET rast = ST_ColorMap(rast, ?) WHERE tile_id = ?");

        importTile = connection.prepareStatement("INSERT INTO " + tableName + " (tile_id, rast) VALUES (?, ST_ColorMap(ST_SetValues(ST_AddBand(ST_MakeEmptyRaster(?, ?, ?, ?, ?, ?, ?, ?, ?), '8BUI'::text, 0), 1, 1, 1, ?::double precision[][]), ?))");

        importTileAsWKB = connection.prepareStatement("INSERT INTO " + tableName + " (tile_id, rast) VALUES (?, ?::raster)");

        try {
            for (Path path : Files.newDirectoryStream(Paths.get(directory.toURI()), "*.png")) {
                importFile(path);
                break; // TODO Remove me once the import is working properly
            }
        } finally {
            importTileAsWKB.close();
            importTile.close();
            setColorMap.close();
            setValues.close();
            addBand.close();
            makeEmptyRaster.close();
        }
    }

    private void importFile(Path path) throws IOException, SQLException {
        final File file = path.toFile();
        LOGGER.debug("Importing grid frame from file [{}]", file);
        final AbstractGridFormat format = GridFormatFinder.findFormat(file);
        final GridCoverage2DReader reader = format.getReader(file);
        try {
            final GridCoverage2D coverage = reader.read(null);
            LOGGER.debug("Frame name is [{}]", coverage.getName());
            final RenderedImage image = coverage.getRenderedImage();

            final int width = image.getWidth();
            final int height = image.getHeight();

            if (width % tileWidth != 0) {
                throw new IllegalArgumentException("Frame width is not evenly divisible by the tile width");
            }
            if (height % tileHeight != 0) {
                throw new IllegalArgumentException("Frame height is not evenly divisible by the tile height");
            }

            if (!(image.getColorModel() instanceof IndexColorModel)) {
                throw new IllegalArgumentException("Color model is not indexed");
            }

            final Envelope2D envelope = coverage.getEnvelope2D();
            final double upperLeftX = envelope.getMaxX();
            final double upperLeftY = envelope.getMaxY();
            final double scaleX = envelope.getWidth() / width;
            final double scaleY = -envelope.getHeight() / height;

            final int cols = width / tileWidth;
            final int rows = height / tileHeight;

            LOGGER.debug("Frame will be split up into {} tiles", cols * rows);

            final Raster raster = image.getData();

            // Build color map
            IndexColorModel colorModel = (IndexColorModel) image.getColorModel();

            // Import tiles
            for (int y = 0; y < rows; ++y) {
                for (int x = 0; x < cols; ++x) {
                    final Rectangle tile = new Rectangle(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
                    ByteBuffer wfb = buildWFB(tileWidth, tileHeight,
                            upperLeftX + scaleX * tile.x, upperLeftY + scaleY * tile.y,
                            scaleX, scaleY, srid, raster.createChild(tile.x, tile.y, tile.width, tile.height,
                                    0, 0, null), colorModel);
                    importTileAsWKB.setString(1, makeTileId(coverage.getName().toString(), x, y));
                    importTileAsWKB.setBlob(2, new ByteArrayInputStream(wfb.array()));
                    importTileAsWKB.execute();
                }
            }
            reader.dispose();

            LOGGER.debug("Frame [{}] imported successfully", coverage.getName());
        } catch (IOException ex) {
            LOGGER.error("I/O exception while reading from file [{}]", file);
            throw ex;
        } catch (SQLException ex) {
            LOGGER.error("SQL exception while writing tiles from file [{}]", file);
            throw ex;
        }
    }

    private ByteBuffer buildWFB(short width, short height, double upperLeftX,
                                double upperLeftY, double scaleX, double scaleY, int srid,
                                Raster raster, ColorModel colorModel) throws SQLException {
        final ByteBuffer buf = ByteBuffer.allocate(61 + 3 * (2 + width * height));

        // Header (61 bytes)
        buf.put((byte) 0); // Big endian
        buf.putShort((short) 0); // Version
        buf.putShort((short) 3); // Bands
        buf.putDouble(scaleX);
        buf.putDouble(scaleY);
        buf.putDouble(upperLeftX);
        buf.putDouble(upperLeftY);
        buf.putDouble(0f); // Skew X
        buf.putDouble(0f); // Skew Y
        buf.putInt(srid);
        buf.putShort(width);
        buf.putShort(height);

        // Bands (2 + w*h bytes)
        for (int band = 0; band < 3; ++band) {
            buf.put((byte) 0b00000100); // 8 bit unsigned integer pixtype, no nodata
            buf.put((byte) 0); // Nodata, not used

            for (int y = 0; y < height; ++y) {
                for (int x = 0; x < width; ++x) {
                    int pixel = raster.getSample(x, y, 0);
                    if (band == 0) {
                        buf.put((byte) colorModel.getRed(pixel));
                    } else if (band == 1) {
                        buf.put((byte) colorModel.getGreen(pixel));
                    } else if (band == 2) {
                        buf.put((byte) colorModel.getBlue(pixel));
                    }
                }
            }
        }

        return buf;
    }

    private void importTile(String frame, int tileX, int tileY, int width, int height, double upperLeftX,
                            double upperLeftY, double scaleX, double scaleY, int srid,
                            Raster raster, String colorMap) throws SQLException {
        LOGGER.debug("Importing tile xy({},{}) from {}", tileX, tileY, frame);
        // Raster parameters
        importTile.setString(IMPORT_TILE_PARAM_TILE_ID, makeTileId(frame, tileX, tileY));
        importTile.setInt(IMPORT_TILE_PARAM_WIDTH, width);
        importTile.setInt(IMPORT_TILE_PARAM_HEIGHT, height);
        importTile.setDouble(IMPORT_TILE_PARAM_UPPER_LEFT_X, upperLeftX);
        importTile.setDouble(IMPORT_TILE_PARAM_UPPER_LEFT_Y, upperLeftY);
        importTile.setDouble(IMPORT_TILE_PARAM_SCALE_X, scaleX);
        importTile.setDouble(IMPORT_TILE_PARAM_SCALE_Y, scaleY);
        importTile.setDouble(IMPORT_TILE_PARAM_SKEW_X, 0);
        importTile.setDouble(IMPORT_TILE_PARAM_SKEW_Y, 0);
        importTile.setInt(IMPORT_TILE_PARAM_SRID, srid);

        // Pixels and color map
        final double rasterArray[][] = new double[tileHeight][tileWidth];
        for (int row = 0; row < tileHeight; ++row) {
            raster.getPixels(0, row, tileWidth, 1, rasterArray[row]);
        }
        final Array rows = importTile.getConnection().createArrayOf("float8", rasterArray);
        importTile.setArray(IMPORT_TILE_PARAM_ROWS, rows);
        importTile.setString(IMPORT_TILE_PARAM_COLOR_MAP, colorMap);

        // Insert into database and free resources
        importTile.execute();
        rows.free();
    }

    @Deprecated
    private void makeEmptyRaster(String frame, int tileX, int tileY, int width, int height, double upperLeftX,
                                 double upperLeftY, double scaleX, double scaleY, double skewX, double skewY, int srid)
            throws SQLException {
        // TODO Delete record if it exists
        makeEmptyRaster.setString(MAKE_EMPTY_RASTER_PARAM_TILE_ID, makeTileId(frame, tileX, tileY));
        makeEmptyRaster.setInt(MAKE_EMPTY_RASTER_PARAM_WIDTH, width);
        makeEmptyRaster.setInt(MAKE_EMPTY_RASTER_PARAM_HEIGHT, height);
        makeEmptyRaster.setDouble(MAKE_EMPTY_RASTER_PARAM_UPPER_LEFT_X, upperLeftX);
        makeEmptyRaster.setDouble(MAKE_EMPTY_RASTER_PARAM_UPPER_LEFT_Y, upperLeftY);
        makeEmptyRaster.setDouble(MAKE_EMPTY_RASTER_PARAM_SCALE_X, scaleX);
        makeEmptyRaster.setDouble(MAKE_EMPTY_RASTER_PARAM_SCALE_Y, scaleY);
        makeEmptyRaster.setDouble(MAKE_EMPTY_RASTER_PARAM_SKEW_X, skewX);
        makeEmptyRaster.setDouble(MAKE_EMPTY_RASTER_PARAM_SKEW_Y, skewY);
        makeEmptyRaster.setInt(MAKE_EMPTY_RASTER_PARAM_SRID, srid);
        makeEmptyRaster.execute();
    }

    @Deprecated
    private void addBand(String frame, int tileX, int tileY, Raster raster, IndexColorModel colorModel) throws SQLException {
        final String tileId = makeTileId(frame, tileX, tileY);
        addBand.setString(ADD_BAND_PARAM_TILE_ID, tileId);
        addBand.execute();

        final Set<Integer> usedPixels = new HashSet<>();
        final double rasterArray[][] = new double[tileHeight][tileWidth];
        for (int row = 0; row < tileHeight; ++row) {
            raster.getPixels(0, row, tileWidth, 1, rasterArray[row]);
            Arrays.stream(rasterArray[row]).mapToInt(value -> (int) value).forEach(usedPixels::add);
        }
        final Array rows = addBand.getConnection().createArrayOf("float8", rasterArray);
        setValues.setArray(SET_VALUES_PARAM_ROWS, rows);
        setValues.setString(SET_VALUES_PARAM_TILE_ID, tileId);
        setValues.execute();
        rows.free();

        StringBuilder colorMap = new StringBuilder();
        for (int px : usedPixels) {
            if (colorMap.length() > 0) {
                colorMap.append('\n');
            }
            colorMap.append(String.format("%d %d %d %d %d", px, colorModel.getRed(px), colorModel.getGreen(px), colorModel.getBlue(px), colorModel.getAlpha(px)));
        }
        setColorMap.setString(SET_COLOR_MAP_PARAM_COLOR_MAP, colorMap.toString());
        setColorMap.setString(SET_COLOR_MAP_PARAM_TILE_ID, tileId);
        setColorMap.execute();

        // TODO Merge everything into a single query
    }

    private static String makeTileId(String frame, int tileX, int tileY) {
        return String.format("%s_%d_%d", frame, tileX, tileY);
    }

    public static void main(String[] args) throws Exception {
        String url = "jdbc:postgresql://localhost/nlsmap";
        Properties props = new Properties();
        props.setProperty("user", "nlsmap");
        props.setProperty("password", "nlsmap");
        try (Connection conn = DriverManager.getConnection(url, props)) {
            PostgisRasterImporter importer = new PostgisRasterImporter(
                    new File("/Users/petterprivate/Google Drive/Maps/taustakartta_1_5000"), conn,
                    "raster_5000", 3067, (short) 100, (short) 100); // ETRS89 / TM35FIN
            importer.importFiles();
        }
    }
}
