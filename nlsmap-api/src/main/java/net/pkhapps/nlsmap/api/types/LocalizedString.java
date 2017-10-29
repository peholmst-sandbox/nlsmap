package net.pkhapps.nlsmap.api.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * TODO document me
 */
public class LocalizedString implements Serializable {

    @JsonValue
    private final Map<Language, String> values;

    /**
     * Creates a new
     *
     * @param values
     */
    @JsonCreator
    public LocalizedString(Map<Language, String> values) {
        Objects.requireNonNull(values, "values must not be null");
        this.values = new HashMap<>(values);
    }

    /**
     * @param language
     * @return
     */
    public Optional<String> get(Language language) {
        Objects.requireNonNull(language, "language must not be null");
        return Optional.ofNullable(values.get(language));
    }

    /**
     * @param language
     * @return
     */
    public boolean containsLanguage(Language language) {
        return get(language).isPresent();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LocalizedString that = (LocalizedString) o;

        return values.equals(that.values);
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }

    /**
     *
     */
    public static class Builder {

        private Map<Language, String> values = new HashMap<>();

        /**
         * @param language
         * @param value
         * @return
         */
        public @NotNull Builder withValue(@NotNull Language language, @Nullable String value) {
            Objects.requireNonNull(language, "language must not be null");
            if (value == null) {
                values.remove(language);
            } else {
                values.put(language, value);
            }
            return this;
        }

        /**
         * @return
         */
        public @NotNull LocalizedString build() {
            return new LocalizedString(values);
        }
    }
}
