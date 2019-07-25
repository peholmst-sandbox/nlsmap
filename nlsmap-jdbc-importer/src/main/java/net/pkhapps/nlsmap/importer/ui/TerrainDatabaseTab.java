package net.pkhapps.nlsmap.importer.ui;

import javafx.beans.property.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

/**
 * UI for {@link net.pkhapps.nlsmap.importer.workers.TerrainDatabaseImportTask}.
 */
class TerrainDatabaseTab extends BorderPane {

    private final ObjectProperty<Connection> connection;
    private final BooleanProperty running = new SimpleBooleanProperty(false);
    private final StringProperty status = new SimpleStringProperty();
    private final StringProperty directory = new SimpleStringProperty();

    TerrainDatabaseTab(@NotNull ObjectProperty<Connection> connection) {
        this.connection = connection;

        setPadding(new Insets(10));

        VBox header = new VBox();
        header.setSpacing(10);
        header.setPadding(new Insets(0, 0, 10, 0));
        setTop(header);

        Label infoLabel = new Label("This importer will importer address information from the NLS terrain database " +
                "(maastotietokanta). You have to download the files yourself and uncompress them into a directory. " +
                "The importer will then scan the directory for files and importer the information it needs.");
        infoLabel.setWrapText(true);
        header.getChildren().add(infoLabel);

        HBox directorySelection = new HBox();
        directorySelection.setSpacing(10);
        directorySelection.setAlignment(Pos.CENTER_LEFT);
        header.getChildren().add(directorySelection);

        directorySelection.getChildren().add(new Text("Directory:"));

        TextField directory = new TextField();
        directory.textProperty().bindBidirectional(this.directory);
        directorySelection.getChildren().add(directory);

        Button selectDirectory = new Button("Browse...");
        //selectDirectory.setOnAction(this::onSelectDirectory);
        directorySelection.getChildren().add(selectDirectory);

        Button scanAndImport = new Button("Scan and Import");
        // TODO setOnAction
        scanAndImport.disableProperty().bind(connection.isNull().or(running).or(this.directory.isEmpty()));
        directorySelection.getChildren().add(scanAndImport);

        TextArea status = new TextArea();
        status.setEditable(false);
        setCenter(status);
    }
}
