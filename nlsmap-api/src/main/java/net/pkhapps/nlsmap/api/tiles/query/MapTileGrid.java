package net.pkhapps.nlsmap.api.tiles.query;

import com.vividsolutions.jts.geom.Envelope;
import net.pkhapps.nlsmap.api.tiles.MapTile;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public interface MapTileGrid<T extends MapTile> extends Serializable {

    @NotNull ZoomLevelMetaData getZoomLevel();

    /**
     * The number of columns in the grid.
     */
    int getColumns();

    /**
     * The number of rows in the grid.
     */
    int getRows();

    /**
     * Returns the tile at the specified cell. Indexes start from 0, in the "top-left" corner of the grid.
     *
     * @param column the column index.
     * @param row    the row index.
     * @return the map tile.
     * @throws IndexOutOfBoundsException if the column and/or row index is out of bounds.
     */
    @NotNull T getTile(int column, int row);

    /**
     * The envelope (bounds) of the grid.
     */
    @NotNull Envelope getEnvelope();
}
