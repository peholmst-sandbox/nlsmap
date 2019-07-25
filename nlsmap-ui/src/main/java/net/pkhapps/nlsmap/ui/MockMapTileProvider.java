package net.pkhapps.nlsmap.ui;

import net.pkhapps.nlsmap.api.CoordinateReferenceSystems;
import net.pkhapps.nlsmap.api.tiles.MapTile;
import net.pkhapps.nlsmap.api.tiles.MapTileIdentifier;
import net.pkhapps.nlsmap.api.tiles.MapTileProvider;
import org.geotools.geometry.Envelope2D;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mock implementation of {@link MapTileProvider} to be used for UI component testing.
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

    private int getMaxX(int zoomLevel) {
        return (int) (BOUNDS.getWidth() / (TILE_WIDTH_PX * getScaleX(zoomLevel)));
    }

    private int getMaxY(int zoomLevel) {
        return (int) (BOUNDS.getHeight() / (TILE_HEIGHT_PX * -getScaleY(zoomLevel)));
    }

    private boolean isValidTileCoordinates(int zoomLevel, int x, int y) {
        return zoomLevel >= MIN_ZOOM_LEVEL && zoomLevel <= MAX_ZOOM_LEVEL
                && x >= 0 && y >= 0
                && x < getMaxX(zoomLevel) && y < getMaxY(zoomLevel);
    }

    @Override
    public int getTileHeight(int zoomLevel) {
        return TILE_HEIGHT_PX;
    }

    @Override
    public int getTileWidth(int zoomLevel) {
        return TILE_WIDTH_PX;
    }

    @Override
    public Collection<MapTileIdentifier> getTileIdentifiers(int zoomLevel, Envelope envelope) {
        if (zoomLevel < MIN_ZOOM_LEVEL || zoomLevel > MAX_ZOOM_LEVEL) {
            LOGGER.warn("Zoom level {} is out of bounds ([{},{}])", zoomLevel, MIN_ZOOM_LEVEL, MAX_ZOOM_LEVEL);
            return Collections.emptySet();
        }

        final DirectPosition bottomLeft = envelope.getLowerCorner();
        // We can calculate which tile the bottom left is in
        double offsetX = bottomLeft.getOrdinate(0) - BOUNDS.getMinX();
        double offsetY = bottomLeft.getOrdinate(1) - BOUNDS.getMinY();
        double scale = SCALES[zoomLevel - 1];
        int x = (int) (offsetX / (scale * TILE_WIDTH_PX));
        int y = (int) (offsetY / (scale * TILE_HEIGHT_PX));

        final List<MockMapTileIdentifier> tileIdentifierList = new ArrayList<>();

        // Then we know which tiles to the right and upwards we need to cover the entire envelope.
        // We can do the calculations in either pixels or map units. Let's use map units.

        double envelopeWidth = envelope.getSpan(0);
        double envelopeHeight = envelope.getSpan(1);

        LOGGER.debug("Visible area is {} (w) x {} (h) map units, bottom-left corner is {}", envelopeWidth,
                envelopeHeight, envelope.getLowerCorner());

        // TODO Take into account how much of the bottom-left tile is visible, we may get away with fetching one less tile
        int tilesToTheRight = (int) Math.ceil(envelopeWidth / (scale * TILE_WIDTH_PX));
        int tilesUpwards = (int) Math.ceil(envelopeHeight / (scale * TILE_HEIGHT_PX));

        LOGGER.debug("Bottom-left tile is ({},{}), need to get {} tiles to the right and {} tiles upwards", x, y,
                tilesToTheRight, tilesUpwards);

        for (int x1 = x; x1 <= x + tilesToTheRight; ++x1) {
            for (int y1 = y; y1 <= y + tilesUpwards; ++y1) {
                tileIdentifierList.add(new MockMapTileIdentifier(zoomLevel, x1, y1));
            }
        }

        return tileIdentifierList.stream().filter(t -> isValidTileCoordinates(t.getZoomLevel(), t.x, t.y))
                .collect(Collectors.toList());
    }

    @Deprecated
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

        int x = (int) (offsetX / (scale * TILE_WIDTH_PX));
        int y = (int) (offsetY / (scale * TILE_HEIGHT_PX));

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
            final double scale = SCALES[mapTileIdentifier.getZoomLevel() - 1];
            final double x = BOUNDS.getMinX() + mapTileIdentifier.x * scale * TILE_WIDTH_PX;
            final double y = BOUNDS.getMinY() + mapTileIdentifier.y * scale * TILE_HEIGHT_PX;

            this.envelope = new Envelope2D(CoordinateReferenceSystems.ETRS89_TM35FIN, x, y,
                    TILE_WIDTH_PX * scale, TILE_HEIGHT_PX * scale);
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
        public void paint(Object context, int x, int y) {
            if (context instanceof Graphics) {
                Graphics g = (Graphics) context;
                g.setColor(Color.WHITE);
                g.fillRect(x, y, TILE_WIDTH_PX, TILE_HEIGHT_PX);
                g.setColor(Color.BLUE);
                LOGGER.debug("Painting tile {} at ({},{})", this, x, y);
                g.drawRect(x, y, TILE_WIDTH_PX, TILE_HEIGHT_PX);
                g.drawString(String.format("(%d,%d)", mapTileIdentifier.getX(), mapTileIdentifier.getY()),
                        x + 10, y + 20);
                g.drawString(String.format("LX: %.2f", getEnvelope().getLowerCorner().getOrdinate(0)),
                        x + 10, y + 40);
                g.drawString(String.format("LY: %.2f", getEnvelope().getLowerCorner().getOrdinate(1)),
                        x + 10, y + 50);
                g.drawString(String.format("UX: %.2f", getEnvelope().getUpperCorner().getOrdinate(0)),
                        x + 10, y + 70);
                g.drawString(String.format("UY: %.2f", getEnvelope().getUpperCorner().getOrdinate(1)),
                        x + 10, y + 80);
            } else {
                throw new IllegalArgumentException("Unsupported context: " + context);
            }
        }

        @Override
        public int getWidth() {
            return TILE_WIDTH_PX;
        }

        @Override
        public int getHeight() {
            return TILE_HEIGHT_PX;
        }

        @Override
        public String toString() {
            return String.format("MockMapTile[id=%s,envelope=%s]", mapTileIdentifier, envelope);
        }
    }
}
