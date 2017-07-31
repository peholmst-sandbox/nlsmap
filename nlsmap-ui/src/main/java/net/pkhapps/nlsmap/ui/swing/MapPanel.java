package net.pkhapps.nlsmap.ui.swing;

import net.pkhapps.nlsmap.api.CoordinateReferenceSystems;
import net.pkhapps.nlsmap.api.raster.MapTileIdentifier;
import net.pkhapps.nlsmap.api.raster.MapTileProvider;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.Envelope2D;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * TODO document me. NOT THREAD SAFE
 */
public class MapPanel extends JComponent {

    /**
     * TODO Document me
     */
    public static final String PROP_ANCHOR_POINT_POSITION = "anchorPointPosition";

    /**
     * TODO Document me
     */
    public abstract static class AnchorPoint {

        protected abstract DirectPosition toDirectPosition(DirectPosition anchorPointPosition, Rectangle bounds,
                                                           double scaleX, double scaleY, Point point);

        protected abstract DirectPosition toDirectPosition(DirectPosition anchorPointPosition, Rectangle bounds,
                                                           double scaleX, double scaleY, double offsetX,
                                                           double offsetY);

        protected abstract Envelope toEnvelope(DirectPosition anchorPointPosition, Rectangle bounds, double scaleX,
                                               double scaleY);

        protected abstract Point toPoint(DirectPosition anchorPointPosition, Rectangle bounds, double scaleX,
                                         double scaleY, DirectPosition position);
    }

    /**
     * TODO Document me
     */
    public static final AnchorPoint CENTER = new AnchorPoint() {

        @Override
        protected DirectPosition toDirectPosition(DirectPosition anchorPointPosition, Rectangle bounds, double scaleX,
                                                  double scaleY, Point point) {
            final double centerX = bounds.getWidth() / 2;
            final double centerY = bounds.getHeight() / 2;
            final double offsetX = point.getX() - centerX;
            final double offsetY = point.getY() - centerY;
            // TODO Take axises of CRS into account

            // Here, we are translating from raster to map coordinates, so we use the scale values directly.
            final double coordinateX = anchorPointPosition.getOrdinate(0) + scaleX * offsetX;
            final double coordinateY = anchorPointPosition.getOrdinate(1) + scaleY * offsetY;
            return new DirectPosition2D(anchorPointPosition.getCoordinateReferenceSystem(), coordinateX, coordinateY);
        }

        @Override
        protected DirectPosition toDirectPosition(DirectPosition anchorPointPosition, Rectangle bounds, double scaleX,
                                                  double scaleY, double offsetX, double offsetY) {
            final double centerX = bounds.getWidth() / 2;
            final double centerY = bounds.getHeight() / 2;
            // When we drag the map to the right, the center position moves to the left (west)
            // When we drag the map downwards, the center position moves upwards (north).
            final double x = centerX - offsetX;
            final double y = centerY - offsetY;
            return toDirectPosition(anchorPointPosition, bounds, scaleX, scaleY, new Point((int) x, (int) y));
        }

        @Override
        protected Envelope toEnvelope(DirectPosition anchorPointPosition, Rectangle bounds, double scaleX,
                                      double scaleY) {
            final double centerX = bounds.getWidth() / 2;
            final double centerY = bounds.getHeight() / 2;

            // We are not translating from raster to map coordinates, so we should use the absolute value of the scale.
            // The Y scale is most likely negative since (0,0) in the raster plane is in the top-left corner whereas
            // (0,0) on a map is in the bottom-left corner.

            final double minX = anchorPointPosition.getOrdinate(0) - Math.abs(centerX * scaleX);
            final double minY = anchorPointPosition.getOrdinate(1) - Math.abs(centerY * scaleY);

            return new Envelope2D(anchorPointPosition.getCoordinateReferenceSystem(), minX, minY,
                    Math.abs(bounds.getWidth() * scaleX), Math.abs(bounds.getHeight() * scaleY));
        }

        @Override
        protected Point toPoint(DirectPosition anchorPointPosition, Rectangle bounds, double scaleX, double scaleY,
                                DirectPosition position) {
            final double centerX = bounds.getWidth() / 2;
            final double centerY = bounds.getHeight() / 2;
            // TODO Take axises of CRS into account
            final double coordinateOffsetX = position.getOrdinate(0) - anchorPointPosition.getOrdinate(0);
            final double coordinateOffsetY = position.getOrdinate(1) - anchorPointPosition.getOrdinate(1);
            // Here, we are translating from map to raster coordinates, so we use the scale values directly.
            final double x = centerX + coordinateOffsetX / scaleX;
            final double y = centerY + coordinateOffsetY / scaleY;
            return new Point((int) x, (int) y);
        }

        @Override
        public String toString() {
            return "center";
        }
    };

    // TODO Create additional anchor points

    // TODO Prevent dragging the map off bounds

    private class MouseHandler implements MouseListener, MouseMotionListener {

        private Point dragStartMousePosition;
        private DirectPosition dragStartAnchorPointPosition;

        @Override
        public void mouseClicked(MouseEvent e) {
            // NOP
        }

        @Override
        public void mousePressed(MouseEvent e) {
            dragStartMousePosition = e.getPoint();
            dragStartAnchorPointPosition = getAnchorPointPosition();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            dragStartAnchorPointPosition = null;
            dragStartMousePosition = null;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            // NOP
        }

        @Override
        public void mouseExited(MouseEvent e) {
            // NOP
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            final double offsetX = e.getX() - dragStartMousePosition.getX();
            final double offsetY = e.getY() - dragStartMousePosition.getY();
            doSetAnchorPointPosition(anchorPoint.toDirectPosition(dragStartAnchorPointPosition, getBounds(),
                    getScaleX(), getScaleY(), offsetX, offsetY), true);
            repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            // NOP
        }
    }

    private MapTileProvider mapTileProvider;
    private int zoomLevel = 0;
    // Initial anchorPointPosition is a roundabout in my home town :-)
    private DirectPosition anchorPointPosition = new DirectPosition2D(CoordinateReferenceSystems.ETRS89_TM35FIN,
            240474.500, 6694820.500);
    private AnchorPoint anchorPoint = CENTER;
    private final List<MapPanelLayer> layers = new ArrayList<>();

    /**
     * Default constructor for the {@code MapPanel}. Remember to set a
     * {@link #setMapTileProvider(MapTileProvider) MapTileProvider} before you start using the component.
     */
    public MapPanel() {
        final MouseHandler mouseHandler = new MouseHandler();
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (mapTileProvider == null) {
            g.drawString("Please attach a MapTileProvider", 10, 20);
        } else {
            // TODO Cache background and layers as images, so that we don't need to repaint everything
            // Draw the background
            g.setColor(Color.GRAY);
            g.fillRect(0, 0, getBounds().width, getBounds().height);
            mapTileProvider.getTileIdentifiers(zoomLevel, getEnvelope()).forEach(tileIdentifier ->
                    drawTile(tileIdentifier, g));
            // Draw the layers
            layers.stream().filter(MapPanelLayer::isVisible).forEach(l -> l.paint(g));
        }
    }

    private void drawTile(MapTileIdentifier tileIdentifier, Graphics g) {
        mapTileProvider.getTile(tileIdentifier).ifPresent(tile -> {
            final Point bottomLeft = getAnchorPoint().toPoint(getAnchorPointPosition(), getBounds(), getScaleX(),
                    getScaleY(), tile.getEnvelope().getLowerCorner());
            if (bottomLeft.x + tile.getWidth() > 0 && bottomLeft.y > 0) {
                tile.paint(g, bottomLeft.x, bottomLeft.y - tile.getHeight());
            } // No need to paint something that is outside the clipping area
        });
    }

    /**
     * TODO Document me
     */
    public AnchorPoint getAnchorPoint() {
        return anchorPoint;
    }

    // TODO Setter for anchorPoint

    /**
     * TODO Document me
     */
    public DirectPosition getAnchorPointPosition() {
        return anchorPointPosition;
    }

    private void doSetAnchorPointPosition(DirectPosition anchorPointPosition, boolean fireEvent) {
        DirectPosition old = null;
        if (fireEvent) {
            old = new DirectPosition2D(this.anchorPointPosition);
        }
        this.anchorPointPosition = new DirectPosition2D(anchorPointPosition);
        if (fireEvent) {
            firePropertyChange(PROP_ANCHOR_POINT_POSITION, old, anchorPointPosition);
        }
    }

    private double getScaleX() {
        return requireMapTileProvider().getScaleX(getZoomLevel());
    }

    private double getScaleY() {
        return requireMapTileProvider().getScaleY(getZoomLevel());
    }

    /**
     * TODO Document me
     */
    public Optional<DirectPosition> getMousePositionCoordinates() {
        return Optional.ofNullable(getMousePosition()).map(point -> getAnchorPoint().toDirectPosition(
                getAnchorPointPosition(), getBounds(), getScaleX(), getScaleY(), point));
    }

    /**
     * TODO Document me
     */
    public Envelope getEnvelope() {
        return getAnchorPoint().toEnvelope(getAnchorPointPosition(), getBounds(), getScaleX(), getScaleY());
    }

    /**
     * TODO Document me
     */
    public DirectPosition getCoordinates(Point point) {
        Objects.requireNonNull(point, "point must not be null");
        return getAnchorPoint().toDirectPosition(getAnchorPointPosition(), getBounds(), getScaleX(), getScaleY(),
                point);
    }

    /**
     * TODO Document me
     */
    public Optional<MapTileProvider> getMapTileProvider() {
        return Optional.ofNullable(mapTileProvider);
    }

    /**
     * TODO Document me
     */
    public void setMapTileProvider(MapTileProvider mapTileProvider) {
        this.mapTileProvider = mapTileProvider;
        if (mapTileProvider != null) {
            zoomLevel = mapTileProvider.getMinZoomLevel();
        }
        repaint();
    }

    private MapTileProvider requireMapTileProvider() {
        if (mapTileProvider == null) {
            throw new IllegalStateException("No MapTileProvider has been set");
        }
        return mapTileProvider;
    }

    /**
     * TODO Document me
     */
    public int getZoomLevel() {
        return zoomLevel;
    }

    /**
     * TODO Document me
     */
    public void setZoomLevel(int zoomLevel) {
        if (zoomLevel != this.zoomLevel) {
            MapTileProvider mapTileProvider = requireMapTileProvider();
            if (zoomLevel < mapTileProvider.getMinZoomLevel() || zoomLevel > mapTileProvider.getMaxZoomLevel()) {
                throw new IllegalArgumentException("Illegal zoom level");
            }
            this.zoomLevel = zoomLevel;
            repaint();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, 300);
    }

    /**
     * TODO Document me
     */
    public void addLayer(MapPanelLayer layer) {
        Objects.requireNonNull(layer, "layer must not be null");
        if (layer.isAttached()) {
            throw new IllegalArgumentException("Layer is already attached to a MapPanel");
        }
        layer.attach(this);
        layers.add(layer);
        repaint();
    }

    /**
     * TODO Document me
     */
    public void removeLayer(MapPanelLayer layer) {
        Objects.requireNonNull(layer, "layer must not be null");
        if (layers.remove(layer)) {
            layer.detach();
            repaint();
        }
    }

    /**
     * TODO Document me
     */
    public Iterable<MapPanelLayer> getLayers() {
        return layers;
    }

    // TODO Method for reordering layers
}
