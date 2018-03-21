package net.pkhapps.nlsmap.api.types;

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
     * Returns the ISO-639-2 code of the language.
     */
    public String getISO639alpha3() {
        return iso639_2;
    }

    /**
     * Returns the locale of the language.
     */
    public Locale getLocale() {
        return new Locale(iso639_2);
    }
}
