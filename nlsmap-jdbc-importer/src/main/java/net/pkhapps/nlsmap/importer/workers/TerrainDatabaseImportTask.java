package net.pkhapps.nlsmap.importer.workers;

import javafx.beans.property.ObjectProperty;
import javafx.concurrent.Task;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;

public class TerrainDatabaseImportTask extends Task<Void> {

    private final ObjectProperty<Connection> connection;
    private final ObjectProperty<File> directory;

    public TerrainDatabaseImportTask(ObjectProperty<Connection> connection, ObjectProperty<File> directory) {
        this.connection = connection;
        this.directory = directory;
    }

    @Override
    protected Void call() throws Exception {
        final Connection connection = this.connection.get();
        final File directory = this.directory.get();

        try (Statement statement = connection.createStatement()) {
            updateMessage("Dropping existing tables if they exist");

        }
        return null;
    }
}
