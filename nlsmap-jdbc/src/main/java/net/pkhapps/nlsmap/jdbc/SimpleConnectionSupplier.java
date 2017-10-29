package net.pkhapps.nlsmap.jdbc;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Simple implementation of {@link ConnectionSupplier} that creates a single {@link Connection} that is cached. This
 * class is not thread safe.
 *
 * @see #setDatabaseUrl(String)
 * @see #setDatabaseFile(File, String)
 * @see #open()
 * @see #close()
 */
public class SimpleConnectionSupplier implements ConnectionSupplier, AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleConnectionSupplier.class);

    private Connection connection;
    private String jdbcUrl;

    /**
     * Sets the file of the H2 database that contains the NLS map information.
     *
     * @param directory    the directory containing the database.
     * @param databaseName the name of the database.
     * @see #setDatabaseUrl(String)
     */
    public void setDatabaseFile(@NotNull File directory, @NotNull String databaseName) {
        Objects.requireNonNull(directory, "directory must not be null");
        Objects.requireNonNull(databaseName, "databaseName must not be null");
        setDatabaseUrl(String.format("jdbc:h2:%s%s%s", directory.getAbsolutePath(), File.separator, databaseName));
    }

    /**
     * Sets the JDBC URL of the database that contains the NLS map information.
     *
     * @param url the JDBC URL.
     * @see #setDatabaseFile(File, String)
     */
    protected void setDatabaseUrl(@NotNull String url) {
        this.jdbcUrl = Objects.requireNonNull(url, "url must not be null");
        LOGGER.info("Using JDBC URL {} for map information", jdbcUrl);
    }

    /**
     * Checks if the connection supplier is currently connected to the database or not.
     *
     * @return true if connected, false if not.
     */
    public boolean isConnected() {
        return connection != null;
    }

    /**
     * Opens a connection to the database. Remember to close it when no longer needed!
     *
     * @throws SQLException if no database URL has been specified or if a connection using the URL could not be
     *                      established.
     * @see #setDatabaseUrl(String)
     * @see #setDatabaseFile(File, String)
     * @see #close()
     */
    public void open() throws SQLException {
        if (jdbcUrl == null) {
            throw new SQLException("No database URL has been specified");
        }
        LOGGER.info("Opening connection to map database");
        try {
            connection = DriverManager.getConnection(jdbcUrl);
        } catch (SQLException ex) {
            LOGGER.error("Error opening connection to map database", ex);
            throw ex;
        }
    }

    /**
     * Closes the connection to the database. If no connection has been opened, nothing happens.
     *
     * @throws Exception if something goes wrong while closing the connection.
     */
    @Override
    public void close() throws Exception {
        if (connection != null) {
            LOGGER.info("Closing connection to map database");
            try {
                connection.close();
            } catch (SQLException ex) {
                LOGGER.error("Error closing connection to map database", ex);
                throw ex;
            } finally {
                connection = null;
            }
        }
    }

    @Override
    public @NotNull Connection get() throws SQLException {
        if (connection != null) {
            return connection;
        } else {
            throw new SQLException("Not connected to the database");
        }
    }
}
