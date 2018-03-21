package net.pkhapps.nlsmap.api.codes;

import net.pkhapps.nlsmap.api.types.Language;
import net.pkhapps.nlsmap.api.types.LocalizedString;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Enumeration of road administrators ("who owns/maintains the road?").
 *
 * @see <a href="http://xml.nls.fi/XML/Schema/Maastotietojarjestelma/MTK/201405/Koodistot/MtjHallinnollinenTieluokka.xsd">MtjHallinnollinenTieluokka.xsd</a>
 */
public enum RoadAdministrator implements Code<String> {

    STATE("1", "Valtio", "Stat"),
    MUNICIPALITY("2", "Kunta", "Kommun"),
    PRIVATE("3", "Yksityinen", "Enskild"),
    UNKNOWN("", "Tuntematon", "Okänd");

    private static final Map<String, RoadAdministrator> codeMap = new HashMap<>();

    static {
        Stream.of(values()).forEach(code -> codeMap.put(code.code, code));
    }

    final String code;
    final LocalizedString description;

    RoadAdministrator(String code, String descriptionFin, String descriptionSwe) {
        this.code = code;
        this.description = LocalizedString.builder()
                .withValue(Language.FINNISH, descriptionFin)
                .withValue(Language.SWEDISH, descriptionSwe)
                .build();
    }

    /**
     * Returns the road administrator with the given code.
     */
    public static @NotNull Optional<RoadAdministrator> findByCode(@NotNull String code) {
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
