package net.pkhapps.nlsmap.ui.swing;

import org.opengis.geometry.Envelope;

import java.awt.*;
import java.util.Optional;

/**
 * TODO Document me
 */
public abstract class MapPanelLayer {

    private MapPanel mapPanel;
    private boolean visible = true;
    private boolean dirty = false;

    /**
     * TODO Document me
     */
    protected void attach(MapPanel mapPanel) {
        this.mapPanel = mapPanel;
    }

    /**
     * TODO Document me
     */
    protected void detach() {
        this.mapPanel = null;
    }

    /**
     * TODO Document me
     */
    protected boolean isAttached() {
        return getMapPanel().isPresent();
    }

    /**
     * TODO Document me
     */
    public Optional<MapPanel> getMapPanel() {
        return Optional.ofNullable(mapPanel);
    }

    /**
     * TODO Document me
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * TODO Document me
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
        getMapPanel().ifPresent(MapPanel::repaint);
    }

    /**
     * TODO Document me
     */
    protected boolean isDirty() {
        return dirty;
    }

    /**
     * TODO Document me
     */
    protected void markAsDirty() {
        dirty = true;
        getMapPanel().ifPresent(MapPanel::repaint);
    }

    /**
     * TODO Document me
     */
    protected void paint(Graphics g) {
        if (mapPanel == null) {
            throw new IllegalStateException("Layer is not attached to a MapPanel");
        }
        doPaint(g, mapPanel.getBounds(), mapPanel.getEnvelope(), mapPanel.getZoomLevel());
        dirty = false;
    }

    /**
     * TODO Document me
     */
    protected abstract void doPaint(Graphics g, Rectangle bounds, Envelope envelope, int zoomLevel);
}
