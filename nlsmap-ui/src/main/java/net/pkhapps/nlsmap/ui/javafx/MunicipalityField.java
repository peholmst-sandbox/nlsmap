package net.pkhapps.nlsmap.ui.javafx;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.util.StringConverter;
import net.pkhapps.nlsmap.api.codes.Municipality;
import net.pkhapps.nlsmap.api.features.query.MunicipalityQuery;
import net.pkhapps.nlsmap.api.types.Language;
import net.pkhapps.nlsmap.api.types.SearchMode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * A JavaFX control for selecting a single {@link Municipality}. The user selects the municipality from a list
 * returned by a {@link MunicipalityQuery}. The list is filtered as the user types into the text field.
 *
 * @see MunicipalityQuery#findByName(String, SearchMode)
 */
public class MunicipalityField extends Control {

    private static final Logger LOGGER = LoggerFactory.getLogger(MunicipalityField.class);

    private final ObjectProperty<MunicipalityQuery> municipalityQueryProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<StringConverter<Municipality>> converterProperty = new SimpleObjectProperty<>();
    private final StringConverter<Municipality> defaultConverter = new StringConverter<>() {
        @Override
        public String toString(Municipality object) {
            if (object == null) {
                return "";
            }
            String finnishName = object.getDescription().get(Language.FINNISH).orElse("");
            String swedishName = object.getDescription().get(Language.SWEDISH).orElse("");
            if (finnishName.equals(swedishName)) {
                return finnishName;
            } else if (swedishName.isEmpty()) {
                return finnishName;
            } else if (finnishName.isEmpty()) {
                return swedishName;
            } else {
                return String.format("%s - %s", finnishName, swedishName);
            }
        }

        @Override
        public Municipality fromString(String string) {
            return null; // Not supported, we're handling this case manually
        }
    };
    private final StringProperty searchTermProperty = new SimpleStringProperty();
    private final ObjectBinding<ObservableList<Municipality>> searchResultBinding =
            Bindings.createObjectBinding(this::computeSearchResults, searchTermProperty, municipalityQueryProperty);
    private final ReadOnlyListWrapper<Municipality> searchResultProperty = new ReadOnlyListWrapper<>();
    private final ObjectProperty<Municipality> valueProperty = new SimpleObjectProperty<>();
    private final StringBinding valueStringBinding =
            Bindings.createStringBinding(this::computeValueString, valueProperty, converterProperty);
    private final ReadOnlyStringWrapper valueStringProperty = new ReadOnlyStringWrapper();

    /**
     * Creates a new {@code MunicipalityField}.
     */
    public MunicipalityField() {
        getStyleClass().add("municipality-field");
        searchResultProperty.bind(searchResultBinding);
        valueStringProperty.bind(valueStringBinding);
    }

    private ObservableList<Municipality> computeSearchResults() {
        String searchTerm = getSearchTerm();
        MunicipalityQuery municipalityQuery = getMunicipalityQuery();

        if (searchTerm == null || searchTerm.isEmpty() || municipalityQuery == null) {
            LOGGER.trace("Search term is empty or no query has been set");
            return FXCollections.unmodifiableObservableList(FXCollections.emptyObservableList());
        } else {
            LOGGER.trace("Invoking query using term [{}]", searchTerm);
            List<Municipality> result = municipalityQuery.findByName(searchTerm, SearchMode.STARTS_WITH);
            return FXCollections.unmodifiableObservableList(FXCollections.observableList(result));
        }
    }

    private String computeValueString() {
        return getConverter().toString(getValue());
    }

    @Override
    public String getUserAgentStylesheet() {
        return MunicipalityField.class.getResource("municipalityfield.css").toExternalForm();
    }

    /**
     * Getter for {@link #municipalityQueryProperty()}.
     */
    public final @Nullable MunicipalityQuery getMunicipalityQuery() {
        return municipalityQueryProperty().get();
    }

    /**
     * Setter for {@link #municipalityQueryProperty()}.
     */
    public final void setMunicipalityQuery(@Nullable MunicipalityQuery municipalityQuery) {
        municipalityQueryProperty().set(municipalityQuery);
    }

    /**
     * The {@link MunicipalityQuery} to use when searching for potential municipalities to select from.
     *
     * @see #searchTermProperty()
     * @see #searchResultProperty()
     */
    public final @NotNull ObjectProperty<MunicipalityQuery> municipalityQueryProperty() {
        return municipalityQueryProperty;
    }

    /**
     * Getter for {@link #converterProperty()}.
     */
    public final @NotNull StringConverter<Municipality> getConverter() {
        StringConverter<Municipality> converter = converterProperty().get();
        if (converter != null) {
            return converter;
        } else {
            return defaultConverter;
        }
    }

    /**
     * Setter for {@link #converterProperty()}.
     */
    public final void setConverter(@Nullable StringConverter<Municipality> converter) {
        converterProperty().set(converter);
    }

    /**
     * Converter used to convert the currently selected {@link Municipality} to a string that is displayed to the user
     * in the text field. The converter must never return a {@code null} string and does not need to implement the
     * {@link StringConverter#fromString(String)} method. If no converter has been set, a default one will be used.
     *
     * @see #valueStringProperty()
     */
    public final @NotNull ObjectProperty<StringConverter<Municipality>> converterProperty() {
        return converterProperty;
    }

    /**
     * Getter for {@link #searchTermProperty()}.
     */
    public final @Nullable String getSearchTerm() {
        return searchTermProperty().get();
    }

    /**
     * Setter for {@link #searchTermProperty()}.
     */
    public final void setSearchTerm(@Nullable String searchTerm) {
        searchTermProperty().set(searchTerm);
    }

    /**
     * The search term that is passed to the {@link #municipalityQueryProperty() MunicipalityQuery} to find potential
     * municipalities to select from. The search is performed using {@link SearchMode#STARTS_WITH}.
     *
     * @see #searchResultProperty()
     * @see MunicipalityQuery#findByName(String, SearchMode)
     */
    public final @NotNull StringProperty searchTermProperty() {
        return searchTermProperty;
    }

    /**
     * Getter for {@link #searchResultProperty()}.
     */
    public final @NotNull ObservableList<Municipality> getSearchResult() {
        return searchResultProperty().get();
    }

    /**
     * The search result after the {@link #searchTermProperty() search term} has been changed. The list is unmodifiable.
     *
     * @see #municipalityQueryProperty()
     * @see MunicipalityQuery#findByName(String, SearchMode)
     */
    public final @NotNull ReadOnlyListProperty<Municipality> searchResultProperty() {
        return searchResultProperty.getReadOnlyProperty();
    }

    /**
     * Getter for {@link #valueProperty()}.
     */
    public final @Nullable Municipality getValue() {
        return valueProperty().get();
    }

    /**
     * Setter for {@link #valueProperty()}.
     */
    public final void setValue(@Nullable Municipality value) {
        valueProperty().set(value);
    }

    /**
     * The currently selected {@link Municipality} (i.e. the value of this field).
     */
    public final @NotNull ObjectProperty<Municipality> valueProperty() {
        return valueProperty;
    }

    /**
     * Getter for {@link #valueStringProperty()}.
     */
    public final @NotNull String getValueString() {
        return valueStringProperty().get();
    }

    /**
     * A human readable string representing the value of {@link #valueProperty()}, created by
     * the {@link StringConverter}.
     *
     * @see #converterProperty()
     */
    public final @NotNull ReadOnlyStringProperty valueStringProperty() {
        return valueStringProperty;
    }
}
