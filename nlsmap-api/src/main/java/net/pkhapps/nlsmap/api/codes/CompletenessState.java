package net.pkhapps.nlsmap.api.codes;

import net.pkhapps.nlsmap.api.types.Language;
import net.pkhapps.nlsmap.api.types.LocalizedString;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Enumeration of completeness states ("how complete is the road?").
 *
 * @see <a href="http://xml.nls.fi/XML/Schema/Maastotietojarjestelma/MTK/201405/Koodistot/Valmiusaste.xsd">Valmiusaste.xsd</a>
 */
public enum CompletenessState implements Code<String> {

    IN_USE("0", "Käytössä", "I bruk"),
    UNDER_CONSTRUCTION("1", "Rakenteilla", "Under konstruktion"),
    IN_PLANNING("3", "Suunnitteilla", "Under planering"),
    UNKNOWN("", "Tuntematon", "Okänd");

    private static final Map<String, CompletenessState> codeMap = new HashMap<>();

    static {
        Stream.of(values()).forEach(code -> codeMap.put(code.code, code));
    }

    final String code;
    final LocalizedString description;

    CompletenessState(String code, String descriptionFin, String descriptionSwe) {
        this.code = code;
        this.description = LocalizedString.builder()
                .withValue(Language.FINNISH, descriptionFin)
                .withValue(Language.SWEDISH, descriptionSwe)
                .build();
    }

    /**
     * Returns the completeness state with the given code.
     */
    public static @NotNull Optional<CompletenessState> findByCode(@NotNull String code) {
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
