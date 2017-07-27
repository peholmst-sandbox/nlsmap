package net.pkhapps.nlsmap.jdbc.raster;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.vividsolutions.jts.geom.Geometry;
import net.pkhapps.nlsmap.api.CoordinateReferenceSystems;
import net.pkhapps.nlsmap.api.raster.MapTile;
import net.pkhapps.nlsmap.api.raster.MapTileIdentifier;
import net.pkhapps.nlsmap.api.raster.MapTileProvider;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.h2gis.utilities.SFSUtilities;
import org.h2gis.utilities.SpatialResultSet;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.function.Supplier;

/**
 * TODO Document me. NOT THREAD SAFE!
 */
public class H2gisMapTileProvider implements MapTileProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(H2gisMapTileProvider.class);
    private final Supplier<Connection> connectionSupplier;
    private final Cache<MapTileIdentifier, MapTile> tileCache = CacheBuilder.newBuilder()
            .maximumSize(200) // TODO Make cache size configurable
            .build();
    private final Map<H2gisZoomLevel, Envelope> lastQueriedEnvelopes = new HashMap<>();
    private final Map<H2gisZoomLevel, Collection<MapTileIdentifier>> lastQueryResults = new HashMap<>();

    /**
     * @param connectionSupplier
     */
    public H2gisMapTileProvider(Supplier<Connection> connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
    }

    @Override
    public int getMaxZoomLevel() {
        return H2gisZoomLevel.MAX.getZoomLevel();
    }

    @Override
    public int getMinZoomLevel() {
        return H2gisZoomLevel.MIN.getZoomLevel();
    }

    @Override
    public CoordinateReferenceSystem getCRS() {
        return CoordinateReferenceSystems.ETRS89_TM35FIN; // Always.
    }

    @Override
    public double getScaleX(int zoomLevel) {
        return H2gisZoomLevel.findByZoomLevel(zoomLevel).getScaleX();
    }

    @Override
    public double getScaleY(int zoomLevel) {
        return H2gisZoomLevel.findByZoomLevel(zoomLevel).getScaleY();
    }

    @Override
    public int getTileWidth(int zoomLevel) {
        return H2gisZoomLevel.findByZoomLevel(zoomLevel).getTileWidth();
    }

    @Override
    public int getTileHeight(int zoomLevel) {
        return H2gisZoomLevel.findByZoomLevel(zoomLevel).getTileHeight();
    }

    @Override
    public Collection<MapTileIdentifier> getTileIdentifiers(int zoomLevel, Envelope envelope) {
        final H2gisZoomLevel zl = H2gisZoomLevel.findByZoomLevel(zoomLevel);
        // Start by checking the cache
        if (isEnvelopeWithinLastQueriedEnvelope(zl, envelope)) {
            LOGGER.debug("Found needed tile identifiers in cache for envelope [{}] on zoom level {}", envelope,
                    zoomLevel);
            Collection<MapTileIdentifier> cachedResult = lastQueryResults.get(zl);
            if (cachedResult != null) {
                return new HashSet<>(cachedResult);
            }
        }
        // Then, check the database
        final String tableName = zl.getTableName();
        LOGGER.debug("Querying for tiles inside envelope [{}] on zoom level {}", envelope, zoomLevel);
        // Using the max/min columns is faster than using the geom column because of indexing limitations.
        try (PreparedStatement stmnt = getConnection().prepareStatement("SELECT DISTINCT id, min_x, min_y, max_x, max_y " +
                "FROM " + tableName + " WHERE (min_x >= ? AND max_x <= ? AND min_y >= ? AND max_y <= ?)")) {
            // TODO Cache prepared statement somewhere
            // TODO Take coordinate system axis into account instead of assuming 0 is X and 1 is Y

            // Add an extra margin around the envelope. We might end up fetching some tiles that are never visible,
            // but this makes the query faster.
            double marginX = getTileWidth(zoomLevel) * Math.abs(getScaleX(zoomLevel));
            double marginY = getTileHeight(zoomLevel) * Math.abs(getScaleY(zoomLevel));

            double minX = envelope.getMinimum(0) - marginX;
            double minY = envelope.getMinimum(1) - marginY;
            double maxX = envelope.getMaximum(0) + marginX;
            double maxY = envelope.getMaximum(1) + marginY;

            int parameterIx = 1;
            stmnt.setDouble(parameterIx++, minX);
            stmnt.setDouble(parameterIx++, maxX);
            stmnt.setDouble(parameterIx++, minY);
            stmnt.setDouble(parameterIx, maxY);

            // We need to collect these for the cache
            double actualMinX = Double.MAX_VALUE;
            double actualMinY = Double.MAX_VALUE;
            double actualMaxX = Double.MIN_VALUE;
            double actualMaxY = Double.MIN_VALUE;

            try (ResultSet resultSet = stmnt.executeQuery()) {
                Set<MapTileIdentifier> identifiers = new HashSet<>();
                while (resultSet.next()) {
                    identifiers.add(new H2gisMapTileIdentifier(resultSet.getString(1), zoomLevel));
                    actualMinX = Math.min(actualMinX, resultSet.getDouble(2));
                    actualMinY = Math.min(actualMinY, resultSet.getDouble(3));
                    actualMaxX = Math.max(actualMaxX, resultSet.getDouble(4));
                    actualMaxY = Math.max(actualMaxY, resultSet.getDouble(5));
                }
                LOGGER.debug("Found {} tile(s) on zoom level {} inside envelope [{}]", identifiers.size(), zoomLevel,
                        envelope);

                // Cache results
                if (identifiers.size() > 0) {
                    lastQueryResults.put(zl, new HashSet<>(identifiers));
                    lastQueriedEnvelopes.put(zl, new Envelope2D(
                            new DirectPosition2D(envelope.getCoordinateReferenceSystem(), actualMinX, actualMinY),
                            new DirectPosition2D(envelope.getCoordinateReferenceSystem(), actualMaxX, actualMaxY)));
                } else {
                    // We don't want to keep invalid data in the cache
                    lastQueryResults.remove(zl);
                    lastQueriedEnvelopes.remove(zl);
                }

                return identifiers;
            }
        } catch (Exception ex) {
            LOGGER.error("Error while retrieving tile identifiers, returning empty collection", ex);
        }
        return Collections.emptyList();
    }

    private boolean isEnvelopeWithinLastQueriedEnvelope(H2gisZoomLevel zoomLevel, Envelope envelope) {
        Envelope lastQueriedEnvelope = lastQueriedEnvelopes.get(zoomLevel);
        if (lastQueriedEnvelope == null) {
            return false;
        }
        ReferencedEnvelope lastQueriedReferencedEnvelope = new ReferencedEnvelope(lastQueriedEnvelope);
        return lastQueriedReferencedEnvelope.contains(envelope.getLowerCorner()) &&
                lastQueriedReferencedEnvelope.contains(envelope.getUpperCorner());
    }

    @Override
    public Optional<MapTile> getTile(MapTileIdentifier tileIdentifier) {
        MapTile tile = tileCache.getIfPresent(tileIdentifier);
        if (tile == null && tileIdentifier instanceof H2gisMapTileIdentifier) {
            final String tableName = H2gisZoomLevel.findByZoomLevel(tileIdentifier.getZoomLevel()).getTableName();
            try (PreparedStatement stmnt = getConnection().prepareStatement("SELECT a.image, a.geom FROM " + tableName + " a WHERE a.id = ?")) {
                stmnt.setString(1, ((H2gisMapTileIdentifier) tileIdentifier).id);
                try (SpatialResultSet resultSet = stmnt.executeQuery().unwrap(SpatialResultSet.class)) {
                    if (resultSet.next()) {
                        byte[] image = resultSet.getBytes(1);
                        Geometry geom = resultSet.getGeometry(2);
                        tile = new H2gisMapTile(image, geom, tileIdentifier);
                        tileCache.put(tileIdentifier, tile);
                    }
                }
            } catch (Exception ex) {
                LOGGER.error("Error while retrieving tile image", ex);
            }
        }
        return Optional.ofNullable(tile);
    }

    private Connection getConnection() {
        return SFSUtilities.wrapConnection(connectionSupplier.get());
    }

    private static class H2gisMapTileIdentifier implements MapTileIdentifier {

        private final String id;
        private final int zoomLevel;

        H2gisMapTileIdentifier(String id, int zoomLevel) {
            this.id = id;
            this.zoomLevel = zoomLevel;
        }

        @Override
        public int getZoomLevel() {
            return zoomLevel;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            H2gisMapTileIdentifier that = (H2gisMapTileIdentifier) o;

            if (zoomLevel != that.zoomLevel) return false;
            return id.equals(that.id);
        }

        @Override
        public int hashCode() {
            int result = id.hashCode();
            result = 31 * result + zoomLevel;
            return result;
        }
    }

    private static class H2gisMapTile implements MapTile {

        private final MapTileIdentifier identifier;
        private final BufferedImage image;
        private final Envelope2D envelope;

        private H2gisMapTile(byte[] image, Geometry geom, MapTileIdentifier identifier) throws IOException {
            this.identifier = identifier;
            this.image = ImageIO.read(new ByteArrayInputStream(image));
            // TODO Verify SRID of geom
            this.envelope = JTS.getEnvelope2D(geom.getEnvelopeInternal(), CoordinateReferenceSystems.ETRS89_TM35FIN);
        }

        @Override
        public MapTileIdentifier getIdentifier() {
            return identifier;
        }

        @Override
        public Envelope getEnvelope() {
            return envelope;
        }

        @Override
        public void paint(Object context, int x, int y) {
            if (context instanceof Graphics) {
                Graphics g = (Graphics) context;
                g.drawImage(image, x, y, null);
            } else {
                throw new IllegalArgumentException("Unsupported context: " + context);
            }
        }

        @Override
        public int getWidth() {
            return image.getWidth();
        }

        @Override
        public int getHeight() {
            return image.getHeight();
        }
    }
}
