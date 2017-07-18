package net.pkhapps.nlsmap.ui;

import net.pkhapps.nlsmap.api.CoordinateReferenceSystems;
import net.pkhapps.nlsmap.api.raster.MapTile;
import net.pkhapps.nlsmap.api.raster.MapTileIdentifier;
import net.pkhapps.nlsmap.api.raster.MapTileProvider;
import org.geotools.geometry.Envelope2D;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * Created by petterprivate on 16/07/2017.
 */
public class MockMapTileProvider implements MapTileProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockMapTileProvider.class);

    private static final int MAX_ZOOM_LEVEL = 5;
    private static final int MIN_ZOOM_LEVEL = 1;
    private static final Envelope2D BOUNDS = new Envelope2D(CoordinateReferenceSystems.ETRS89_TM35FIN,
            179000, 6650000.0, 70000, 70000); // (x,y) is bottom-left corner
    private static final int TILE_WIDTH_PX = 200;
    private static final int TILE_HEIGHT_PX = 200;
    private static final double[] SCALES = {16.0, 8.0, 4.0, 2.0, 0.5};


    @Override
    public int getMaxZoomLevel() {
        return MAX_ZOOM_LEVEL;
    }

    @Override
    public int getMinZoomLevel() {
        return MIN_ZOOM_LEVEL;
    }

    @Override
    public CoordinateReferenceSystem getCRS() {
        return CoordinateReferenceSystems.ETRS89_TM35FIN;
    }

    @Override
    public double getScaleX(int zoomLevel) {
        // TODO Validate zoomLevel
        return SCALES[zoomLevel - 1];
    }

    @Override
    public double getScaleY(int zoomLevel) {
        // TODO Validate zoomLevel
        return -SCALES[zoomLevel - 1];
    }

    @Override
    public Optional<MapTileIdentifier> getTileIdentifier(int zoomLevel, DirectPosition position) {
        if (zoomLevel < MIN_ZOOM_LEVEL || zoomLevel > MAX_ZOOM_LEVEL) {
            LOGGER.warn("Zoom level {} is out of bounds ([{},{}])", zoomLevel, MIN_ZOOM_LEVEL, MAX_ZOOM_LEVEL);
            return Optional.empty();
        }
        if (!BOUNDS.contains(position)) {
            LOGGER.warn("Position {} is out of bounds ({})", position, BOUNDS);
            return Optional.empty();
        }
        // We can calculate which tile the position is in
        double offsetX = position.getOrdinate(0) - BOUNDS.getMinX();
        double offsetY = position.getOrdinate(1) - BOUNDS.getMinY();
        double scale = SCALES[zoomLevel - 1];

        int x = new BigDecimal(offsetX / (scale * TILE_WIDTH_PX)).intValue();
        int y = new BigDecimal(offsetY / (scale * TILE_HEIGHT_PX)).intValue();

        return Optional.of(new MockMapTileIdentifier(zoomLevel, x, y));
    }

    @Override
    public Optional<MapTile> getTile(MapTileIdentifier tileIdentifier) {
        if (tileIdentifier instanceof MockMapTileIdentifier) {
            return Optional.of(new MockMapTile((MockMapTileIdentifier) tileIdentifier));
        } else {
            LOGGER.warn("Unsupported tile identifier: {}", tileIdentifier);
            return Optional.empty();
        }
    }

    @Override
    public Map<RelativeLocation, MapTileIdentifier> getAdjoiningTileIdentifiers(MapTileIdentifier pivot, RelativeLocation... relativeLocations) {

        return Collections.emptyMap();
    }

    @Override
    public Collection<MapTile> getMapTiles(Iterable<MapTileIdentifier> mapTileIdentifiers) {
        return Collections.emptyList();
    }

    private static class MockMapTileIdentifier implements MapTileIdentifier {

        private final int zoomLevel;
        private final int x;
        private final int y;

        MockMapTileIdentifier(int zoomLevel, int x, int y) {
            this.zoomLevel = zoomLevel;
            this.x = x;
            this.y = y;
        }

        @Override
        public int getZoomLevel() {
            return zoomLevel;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MockMapTileIdentifier that = (MockMapTileIdentifier) o;

            if (zoomLevel != that.zoomLevel) return false;
            if (x != that.x) return false;
            return y == that.y;
        }

        @Override
        public int hashCode() {
            int result = zoomLevel;
            result = 31 * result + x;
            result = 31 * result + y;
            return result;
        }

        @Override
        public String toString() {
            return String.format("MockMapTileProvider[zoomLevel=%d,x=%d,y=%d]", zoomLevel, x, y);
        }
    }

    private static class MockMapTile implements MapTile {

        private final MockMapTileIdentifier mapTileIdentifier;
        private final Envelope2D envelope;

        MockMapTile(MockMapTileIdentifier mapTileIdentifier) {
            this.mapTileIdentifier = mapTileIdentifier;
            double scale = SCALES[mapTileIdentifier.getZoomLevel() - 1];
            this.envelope = new Envelope2D(CoordinateReferenceSystems.ETRS89_TM35FIN, BOUNDS.getMinX() + mapTileIdentifier.x * scale,
                    BOUNDS.getMinY() + mapTileIdentifier.y * scale, TILE_WIDTH_PX * scale, TILE_HEIGHT_PX * scale);
        }

        @Override
        public MapTileIdentifier getIdentifier() {
            return mapTileIdentifier;
        }

        @Override
        public Envelope getEnvelope() {
            return envelope;
        }

        @Override
        public void paint(Object context, int offsetX, int offsetY) {
            if (context instanceof Graphics) {
                Graphics g = (Graphics) context;
                g.drawRect(offsetX, offsetY, TILE_WIDTH_PX, TILE_HEIGHT_PX);
            }
            throw new IllegalArgumentException("Unsupported context: " + context);
        }

        @Override
        public int getWidth() {
            return TILE_WIDTH_PX;
        }

        @Override
        public int getHeight() {
            return TILE_HEIGHT_PX;
        }
    }
}
