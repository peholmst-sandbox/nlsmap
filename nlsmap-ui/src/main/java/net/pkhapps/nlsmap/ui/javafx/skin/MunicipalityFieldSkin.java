package net.pkhapps.nlsmap.ui.javafx.skin;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.BooleanBinding;
import javafx.css.Styleable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Duration;
import net.pkhapps.nlsmap.api.codes.Municipality;
import net.pkhapps.nlsmap.ui.javafx.MunicipalityField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * TODO Document me
 */
public class MunicipalityFieldSkin extends SkinBase<MunicipalityField> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MunicipalityFieldSkin.class);

    // TODO I18N

    private TextField textField;
    private TableView<Municipality> tableView;
    private PopupControl popup;

    private Timeline textFieldBindingTimeline;

    public MunicipalityFieldSkin(MunicipalityField municipalityField) {
        super(municipalityField);
        // Create the fields
        textField = new TextField();
        getChildren().add(textField);

        createTableView();

        // Bind properties where it is possible
        tableView.itemsProperty().bind(municipalityField.searchResultProperty());
        BooleanBinding textFieldDoesNotContainValueString =
                textField.textProperty().isNotEqualTo(municipalityField.valueString());

        // Register listeners
        registerChangeListener(municipalityField.searchResultProperty(), e -> {
            if (municipalityField.searchResultProperty().getValue().isEmpty()) {
                hideTableView();
            } else {
                showTableView();
            }
        });
        registerChangeListener(municipalityField.valueString(),
                e -> textField.setText(municipalityField.valueString().get()));

        registerChangeListener(textField.textProperty(), e -> {
            // We can't bind the textProperty directly to the search term since the textField can also
            // contain the valueString of the selected municipality. We don't want to use that for searching.
            // Also, we don't want to rerun the search for every single change to the text field (e.g. while the user
            // is typing the search term. Therefore we use a timeline to delay the actual searching.
            if (textFieldBindingTimeline != null) {
                textFieldBindingTimeline.stop();
            }
            textFieldBindingTimeline = new Timeline(new KeyFrame(Duration.millis(300), ae -> {
                if (textField.getText().isEmpty() || textFieldDoesNotContainValueString.get()) {
                    municipalityField.searchTermProperty().set(textField.getText());
                }
            }));
            textFieldBindingTimeline.play();
        });
    }

    private void createTableView() {
        tableView = new TableView<>();
        TableColumn<Municipality, String> nameFi = new TableColumn<>("Name (fi)"); // TODO i18n
        nameFi.setMinWidth(110);

        TableColumn<Municipality, String> nameSv = new TableColumn<>("Name (sv)"); // TODO i18n
        nameSv.setMinWidth(110);

        TableColumn<Municipality, Integer> code = new TableColumn<>("Code"); // TODO i18n
        code.setMinWidth(80);

        tableView.getColumns().addAll(nameFi, nameSv, code);
    }

    private PopupControl getPopup() {
        if (popup == null) {
            createPopup();
        }
        return popup;
    }

    private void createPopup() {
        // I actually don't know why I'm doing this anonymous subclass, but this is how it's done in
        // ComboBoxPopupControl so I just copied it from there.
        popup = new PopupControl() {
            @Override
            public Styleable getStyleableParent() {
                return MunicipalityFieldSkin.this.getSkinnable();
            }

            {
                setSkin(new Skin<Skinnable>() {
                    @Override
                    public Skinnable getSkinnable() {
                        return MunicipalityFieldSkin.this.getSkinnable();
                    }

                    @Override
                    public Node getNode() {
                        return tableView;
                    }

                    @Override
                    public void dispose() {
                        // NOP
                    }
                });
            }
        };
        // TODO Other settings and listeners
        popup.setHideOnEscape(true);
    }

    private void showTableView() {
        LOGGER.trace("Showing table with search results");

    }

    private void hideTableView() {
        LOGGER.trace("Hiding table with search results");
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        textField.resizeRelocate(contentX, contentY, contentWidth, contentHeight);
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset,
                                      double leftInset) {
        return textField.prefWidth(height);
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset,
                                       double leftInset) {
        return textField.prefHeight(width);
    }

    @Override
    protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset,
                                     double leftInset) {
        return textField.prefWidth(height);
    }

    @Override
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset,
                                      double leftInset) {
        return textField.prefHeight(width);
    }
}
