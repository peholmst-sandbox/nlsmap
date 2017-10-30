package net.pkhapps.nlsmap.importer.ui;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.h2.util.StringUtils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Main entry point into the NLS Map Import Application (a JavaFX UI).
 */
public class ImporterApp extends Application {

    private final StringProperty databaseDirectory = new SimpleStringProperty("");
    private final StringProperty databaseName = new SimpleStringProperty("");
    private final StringBinding jdbcUrl = Bindings.createStringBinding(this::getJdbcUrl,
            databaseDirectory, databaseName);
    private final BooleanProperty connected = new SimpleBooleanProperty(false);
    private final ObjectProperty<Connection> connection = new SimpleObjectProperty<>(null);

    private Stage primaryStage;

    private String getJdbcUrl() {
        String dirName = databaseDirectory.get();
        String dbName = databaseName.get();
        if (StringUtils.isNullOrEmpty(dirName) || StringUtils.isNullOrEmpty(dbName)) {
            return "";
        } else {
            return String.format("jdbc:h2:%s%s%s", dirName, File.separator, dbName);
        }
    }

    public ImporterApp() {
        String homeDir = System.getProperty("user.home");
        if (homeDir != null) {
            databaseDirectory.set(homeDir);
        }
        databaseName.set("nlsmap");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        primaryStage.setTitle("NLS Map Importer");

        BorderPane borderPane = new BorderPane();
        // Header
        {
            VBox headerLayout = new VBox();

            {
                HBox header = new HBox();
                header.setPadding(new Insets(10));
                header.setSpacing(10);
                header.getStyleClass().add("app-header");
                headerLayout.getChildren().add(header);

                Text title = new Text("NLS Map Importer");
                title.getStyleClass().add("app-header-title");
                header.getChildren().add(title);
            }

            {
                HBox fileSelection = new HBox();
                fileSelection.setPadding(new Insets(10));
                fileSelection.setSpacing(10);
                fileSelection.setAlignment(Pos.CENTER_LEFT);
                headerLayout.getChildren().add(fileSelection);

                fileSelection.getChildren().add(new Text("Directory:"));

                TextField databaseDirectory = new TextField();
                databaseDirectory.textProperty().bindBidirectional(this.databaseDirectory);
                databaseDirectory.disableProperty().bind(this.connected);

                fileSelection.getChildren().add(databaseDirectory);

                Button selectDirectory = new Button("Browse...");
                selectDirectory.setOnAction(this::onSelectDirectory);
                selectDirectory.disableProperty().bind(databaseDirectory.disabledProperty());
                fileSelection.getChildren().add(selectDirectory);

                fileSelection.getChildren().add(new Text("Database Name:"));

                TextField databaseName = new TextField();
                databaseName.textProperty().bindBidirectional(this.databaseName);
                databaseName.disableProperty().bind(this.connected);
                fileSelection.getChildren().add(databaseName);

                Button connect = new Button("Connect");
                connect.setOnAction(this::onConnect);
                connect.disableProperty().bind(this.connected.or(this.jdbcUrl.isEmpty()));
                fileSelection.getChildren().add(connect);

                Button disconnect = new Button("Disconnect");
                disconnect.setOnAction(this::onDisconnect);
                disconnect.disableProperty().bind(connected.not());
                fileSelection.getChildren().add(disconnect);
            }

            borderPane.setTop(headerLayout);
        }

        // Tabs
        {
            TabPane tabs = new TabPane();
            tabs.getTabs().add(new Tab("Municipalities", new MunicipalitiesTab(connection)));
            // TODO Add additional importer tabs here

            borderPane.setCenter(tabs);
        }

        Scene scene = new Scene(borderPane);
        scene.getStylesheets().add("/net/pkhapps/nlsmap/importer/ui/styles.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void onSelectDirectory(ActionEvent e) {
        DirectoryChooser chooser = new DirectoryChooser();
        File selectedDirectory = chooser.showDialog(primaryStage);
        databaseDirectory.setValue(selectedDirectory == null ? "" : selectedDirectory.getPath());
    }

    private void onConnect(ActionEvent e) {
        Properties props = new Properties();
        try {
            connection.set(DriverManager.getConnection(jdbcUrl.get(), props));
            connected.set(true);
        } catch (SQLException ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
        }
    }

    private void onDisconnect(ActionEvent e) {
        Connection connection = this.connection.get();
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
        } finally {
            this.connection.set(null);
            this.connected.set(false);
        }
    }
}
