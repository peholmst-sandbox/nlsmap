package net.pkhapps.nlsmap.importer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * TODO Document me
 */
public final class H2gisDatabaseInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(H2gisDatabaseInitializer.class.getName());

    private H2gisDatabaseInitializer() {
        // NOP
    }

    public static void initialize(Connection connection) throws SQLException {
        LOGGER.info("Executing DDL statements using connection [{}]", connection);
        final InputStream ddl = H2gisDatabaseInitializer.class.getResourceAsStream("create-db.h2gis.sql");
        final String ddlString = new Scanner(ddl).useDelimiter("\\A").next();
        LOGGER.trace("DDL:\n{}", ddlString);
        try (Statement statement = connection.createStatement()) {
            statement.execute(ddlString);
        }
    }
}
