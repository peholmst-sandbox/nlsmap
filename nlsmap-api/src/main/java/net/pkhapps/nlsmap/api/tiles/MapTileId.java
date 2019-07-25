package net.pkhapps.nlsmap.api.tiles;

import java.io.Serializable;

/**
 * Marker interface for an identifier of a {@link MapTile}. Implementations may decide what constitutes the ID
 * and what parts of it (if any) are exposed.
 */
public interface MapTileId extends Serializable {
}
