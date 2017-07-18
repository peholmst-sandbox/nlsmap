package net.pkhapps.nlsmap.api.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;

/**
 * Enumeration of the languages that place names can be stored in.
 */
public enum Language {

    FINNISH("fin"),

    SWEDISH("swe"),

    NORTHERN_SAMI("sme"),

    INARI_SAMI("smn"),

    SKOLT_SAMI("sms");

    private final String iso639_2;

    Language(String iso639_2) {
        this.iso639_2 = iso639_2;
    }

    /**
     * @return
     */
    @JsonValue
    public String getISO639alpha3() {
        return iso639_2;
    }

    /**
     * @return
     */
    public Locale getLocale() {
        return new Locale(iso639_2);
    }

    @JsonCreator
    private static Language fromISO639alpha3(String iso639_2) {
        for (Language language : Language.values()) {
            if (language.iso639_2.equals(iso639_2)) {
                return language;
            }
        }
        throw new IllegalArgumentException("Unsupported ISO-639-2 code: " + iso639_2);
    }
}
