package net.pkhapps.nlsmap.sample;

import net.pkhapps.nlsmap.api.tiles.MapTileProvider;
import net.pkhapps.nlsmap.jdbc.raster.H2gisMapTileProvider;
import net.pkhapps.nlsmap.ui.MockMapTileProvider;
import net.pkhapps.nlsmap.ui.swing.MapPanel;
import org.opengis.geometry.DirectPosition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * TODO Document me
 */
public class JdbcAndSwingSampleApplication {

    public static void main(String[] args) throws Exception {
        String url = "jdbc:h2:~/nlsmap.test";
        Properties props = new Properties();
        Connection conn = DriverManager.getConnection(url, props);
        UIManager.setLookAndFeel(
                UIManager.getCrossPlatformLookAndFeelClassName());
        SwingUtilities.invokeLater(() -> {
            final MapTileProvider mapTileProvider = new H2gisMapTileProvider(() -> conn);
            final JFrame frame = new JFrame("MapPanel Demo");
            frame.setLayout(new BorderLayout());
            final MapPanel panel = new MapPanel();
            panel.setMapTileProvider(mapTileProvider);
            final JLabel statusBar = new JLabel();
            final JSlider zoom = new JSlider(mapTileProvider.getMinZoomLevel(), mapTileProvider.getMaxZoomLevel());
            zoom.setOrientation(JSlider.VERTICAL);
            zoom.setToolTipText("Zoom");
            zoom.setSnapToTicks(true);
            zoom.setPaintTicks(true);
            zoom.setMinorTickSpacing(1);
            zoom.setMajorTickSpacing(1);
            zoom.setValue(panel.getZoomLevel());
            zoom.setInverted(true);
            zoom.addChangeListener(e -> panel.setZoomLevel(zoom.getValue()));

            final Runnable updateStatusBar = () -> statusBar.setText(
                    String.format("Mouse cursor coordinates: %s, anchor point coordinates: %s, anchor point: %s",
                            panel.getMousePositionCoordinates().map(JdbcAndSwingSampleApplication::directPositionToString).orElse("(none)"),
                            directPositionToString(panel.getAnchorPointPosition()), panel.getAnchorPoint()));

            panel.addPropertyChangeListener(evt -> updateStatusBar.run());
            panel.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    updateStatusBar.run();
                }
            });
            frame.add(panel, BorderLayout.CENTER);
            frame.add(statusBar, BorderLayout.SOUTH);
            frame.add(zoom, BorderLayout.EAST);
            updateStatusBar.run();
            frame.pack();
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }

    private static String directPositionToString(DirectPosition directPosition) {
        if (directPosition == null) {
            return null;
        } else {
            return String.format("%s,%s", directPosition.getOrdinate(0),
                    directPosition.getOrdinate(1));
        }
    }
}
