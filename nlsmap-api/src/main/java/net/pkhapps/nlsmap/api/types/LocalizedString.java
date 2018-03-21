package net.pkhapps.nlsmap.api.types;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Immutable type representing a string with values in many different {@link Language}s. Use the {@link #builder()}
 * method to create new instances.
 */
public final class LocalizedString implements Serializable {

    private final Map<Language, String> values;

    private LocalizedString(@NotNull Map<Language, String> values) {
        Objects.requireNonNull(values, "values must not be null");
        this.values = new HashMap<>(values);
    }

    /**
     * Creates and returns a new {@link Builder} for creating new localized strings.
     */
    public static @NotNull Builder builder() {
        return new Builder();
    }

    /**
     * Returns a representation of the localized string in the given language, if applicable.
     */
    public @NotNull Optional<String> get(@NotNull Language language) {
        Objects.requireNonNull(language, "language must not be null");
        return Optional.ofNullable(values.get(language));
    }

    /**
     * Checks whether the localized string contains a representation for the given language.
     */
    public boolean containsLanguage(@NotNull Language language) {
        return get(language).isPresent();
    }

    /**
     * Returns whether this localized string contains any representations at all.
     *
     * @see #containsLanguage(Language)
     */
    public boolean isEmpty() {
        return values.isEmpty();
    }

    @Override
    public String toString() {
        return String.format("%s[%s]", getClass().getSimpleName(), values);
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
        return Objects.hash(getClass(), values);
    }

    /**
     * Builder for creating new {@link LocalizedString}s.
     */
    public final static class Builder {

        private Map<Language, String> values = new HashMap<>();

        private Builder() {
        }

        /**
         * Adds or removes a representation of the localized string in a specific language.
         *
         * @param language the language of the representation.
         * @param value    the representation of the string in the given language or {@code null} to remove the language.
         * @return the builder instance, to allow for method chaining.
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
         * Creates a new localized string from the values in this builder.
         */
        public @NotNull LocalizedString build() {
            return new LocalizedString(values);
        }
    }
}
