package net.pkhapps.nlsmap.importer.ui;

import javafx.beans.property.*;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import net.pkhapps.nlsmap.importer.workers.MunicipalityImportTask;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

/**
 * UI for {@link MunicipalityImportTask}.
 */
class MunicipalitiesTab extends BorderPane {

    private final ObjectProperty<Connection> connection;
    private final BooleanProperty running = new SimpleBooleanProperty(false);
    private final StringProperty status = new SimpleStringProperty();

    MunicipalitiesTab(@NotNull ObjectProperty<Connection> connection) {
        this.connection = connection;

        setPadding(new Insets(10));

        VBox header = new VBox();
        header.setSpacing(10);
        header.setPadding(new Insets(0, 0, 10, 0));
        setTop(header);

        header.getChildren().add(new Text(
                "This importer will download the list of municipalities from the NLS web service and import it into " +
                "the database."));

        Button downloadAndImport = new Button("Download and Import");
        downloadAndImport.setOnAction(event -> start());
        downloadAndImport.disableProperty().bind(connection.isNull().or(running));
        header.getChildren().add(downloadAndImport);

        TextArea status = new TextArea();
        status.setEditable(false);
        status.textProperty().bind(this.status);
        setCenter(status);
    }

    private void start() {
        MunicipalityImportTask task = new MunicipalityImportTask(connection);
        StringBuffer statusBuilder = new StringBuffer();
        task.messageProperty().addListener((v, oldValue, newValue) -> {
            if (newValue != null) {
                statusBuilder.append(newValue).append("\n");
                status.set(statusBuilder.toString());
            }
        });
        status.set("");
        task.exceptionProperty().addListener((v, oldValue, newValue) -> {
            if (newValue != null) {
                statusBuilder.append("Error: ");
                statusBuilder.append(newValue.getMessage());
                status.set(statusBuilder.toString());
            }
        });
        running.bind(task.runningProperty());
        new Thread(task).start();
    }
}
