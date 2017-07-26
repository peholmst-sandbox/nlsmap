package net.pkhapps.nlsmap.jdbc.raster;

/**
 * TODO Document me
 */
public enum H2gisZoomLevel {

    // The scales have been taken directly from the raw map material. They are unlikely to change, but
    // to be safe, it is a good idea to verify the information when new data is imported into the database.

    ZL320000("bg_raster_1_320000", 0, "1:320000", 256, 305, 64.0, -64.0),
    ZL160000("bg_raster_1_160000", 1, "1:160000", 200, 200, 32.0, -32.0),
    ZL80000("bg_raster_1_80000", 2, "1:80000", 200, 200, 16.0, -16.0),
    ZL40000("bg_raster_1_40000", 3, "1:40000", 200, 200, 8.0, -8.0),
    ZL20000("bg_raster_1_20000", 4, "1:20000", 200, 200, 4.0, -4.0),
    ZL10000("bg_raster_1_10000", 5, "1:10000", 200, 200, 2.0, -2.0),
    ZL5000("bg_raster_1_5000", 6, "1:5000", 200, 200, 0.5, -0.5);

    // Please note! If you add or change the zoom levels, please update these levels since they are used by the
    // H2gisMapTileProvider. There must be no gaps in the zoom levels between max and min.
    /**
     * The maximum zoom level (smallest scale, biggest level of detail)
     */
    public static H2gisZoomLevel MAX = ZL5000;

    /**
     * The minimum zoom level (largest scale, smallest level of detail)
     */
    public static H2gisZoomLevel MIN = ZL320000;

    private final String tableName;
    private final int zoomLevel;
    private final String displayName;
    private final int tileWidth;
    private final int tileHeight;
    private final double scaleX;
    private final double scaleY;

    H2gisZoomLevel(String tableName, int zoomLevel, String displayName, int tileWidth, int tileHeight, double scaleX, double scaleY) {
        this.tableName = tableName;
        this.zoomLevel = zoomLevel;
        this.displayName = displayName;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    /**
     * @return
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @return
     */
    public int getZoomLevel() {
        return zoomLevel;
    }

    /**
     * @return
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @return
     */
    public int getTileWidth() {
        return tileWidth;
    }

    /**
     * @return
     */
    public int getTileHeight() {
        return tileHeight;
    }

    /**
     * @return
     */
    public double getScaleX() {
        return scaleX;
    }

    /**
     * @return
     */
    public double getScaleY() {
        return scaleY;
    }

    /**
     * @param zoomLevel
     * @return
     */
    public static H2gisZoomLevel findByZoomLevel(int zoomLevel) {
        // TODO Optimize to calculate array index from zoom level instead
        for (H2gisZoomLevel level : values()) {
            if (level.getZoomLevel() == zoomLevel) {
                return level;
            }
        }
        throw new IllegalArgumentException("Illegal zoom level: " + zoomLevel);
    }

}
