package net.pkhapps.nlsmap.api.codes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.pkhapps.nlsmap.api.types.LocalizedString;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Data type representing a municipality of Finland. This is often used as a code (referred to by its numerical ID)
 * and so implements the {@link Code} interface even though it is not an enumeration but stored in some kind of
 * database.
 */
public class Municipality implements Code, Serializable {

    private final int code;
    private final LocalizedString name;

    @JsonCreator
    public Municipality(@JsonProperty("code") int code,
                        @JsonProperty("name") @NotNull LocalizedString name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Returns the name of the municipality.
     */
    public @NotNull LocalizedString getName() {
        return name;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public LocalizedString getDescription() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Municipality that = (Municipality) o;

        return code == that.code;
    }

    @Override
    public int hashCode() {
        return code;
    }
}
