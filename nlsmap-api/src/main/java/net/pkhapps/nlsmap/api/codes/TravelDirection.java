package net.pkhapps.nlsmap.api.codes;

import net.pkhapps.nlsmap.api.types.Language;
import net.pkhapps.nlsmap.api.types.LocalizedString;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Enumeration of travel directions ("in which directions can I travel on the road?").
 *
 * @see <a href="http://xml.nls.fi/XML/Schema/Maastotietojarjestelma/MTK/201405/Koodistot/Yksisuuntaisuus.xsd">Yksisuuntaisuus.xsd</a>
 */
public enum TravelDirection implements Code<String> {

    TWO_WAY("0", "Kaksisuuntainen", "Dubbelriktad"),
    IN_DIGITIZATION_DIRECTION("1", "Digitointisuunnassa", "I digitaliseringsriktningen"),
    AGAINST_DIGITIZATION_DIRECTION("2", "Digitointisuuntaa vastaan", "Mot digitaliseringsriktningen"),
    UNKNOWN("", "Tuntematon", "Okänd");

    private static final Map<String, TravelDirection> codeMap = new HashMap<>();

    static {
        Stream.of(values()).forEach(code -> codeMap.put(code.code, code));
    }

    final String code;
    final LocalizedString description;

    TravelDirection(String code, String descriptionFin, String descriptionSwe) {
        this.code = code;
        this.description = LocalizedString.builder()
                .withValue(Language.FINNISH, descriptionFin)
                .withValue(Language.SWEDISH, descriptionSwe)
                .build();
    }

    /**
     * Returns the travel direction with the given code.
     */
    public static @NotNull Optional<TravelDirection> findByCode(@NotNull String code) {
        return Optional.ofNullable(codeMap.get(code));
    }

    @Override
    public @NotNull String getCode() {
        return code;
    }

    @Override
    public @NotNull LocalizedString getDescription() {
        return description;
    }
}
