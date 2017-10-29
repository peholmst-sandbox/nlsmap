package net.pkhapps.nlsmap.jdbc;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interface for suppliers that supply JDBC {@link Connection}s to query objects.
 */
@FunctionalInterface
public interface ConnectionSupplier {

    /**
     * Returns a working {@link Connection} to the database that contains the NLS map data imported by the NLS Map
     * Importer application. Connetions returned by this method should <b>not</b> be closed by the caller.
     *
     * @throws SQLException if a connection could not be obtained.
     */
    @NotNull Connection get() throws SQLException;
}
