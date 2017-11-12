package net.pkhapps.nlsmap.sample.javafx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import net.pkhapps.nlsmap.api.query.MunicipalityQuery;
import net.pkhapps.nlsmap.jdbc.SimpleConnectionSupplier;
import net.pkhapps.nlsmap.jdbc.query.JdbcMunicipalityQuery;
import net.pkhapps.nlsmap.ui.javafx.MunicipalityField;

import java.io.File;

public class MunicipalityFieldDemo extends Application {

    private SimpleConnectionSupplier connectionSupplier;

    @Override
    public void start(Stage primaryStage) throws Exception {
        connectionSupplier = new SimpleConnectionSupplier();
        connectionSupplier.setDatabaseFile(new File(System.getProperty("user.home")), "nlsmap");
        connectionSupplier.open();

        MunicipalityQuery query = new JdbcMunicipalityQuery(connectionSupplier);
        MunicipalityField municipalityField = new MunicipalityField();
        municipalityField.setMunicipalityQuery(query);

        Label municipalityLabel = new Label();
        municipalityLabel.textProperty()
                .bindBidirectional(municipalityField.valueProperty(), municipalityField.getConverter());

        HBox layout = new HBox();
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(municipalityField, municipalityLabel);

        Scene scene = new Scene(layout, 300, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("MunicipalityField Demo");
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        connectionSupplier.close();
    }
}
