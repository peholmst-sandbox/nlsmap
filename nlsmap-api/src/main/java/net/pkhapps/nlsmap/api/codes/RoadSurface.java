package net.pkhapps.nlsmap.api.codes;

import net.pkhapps.nlsmap.api.types.Language;
import net.pkhapps.nlsmap.api.types.LocalizedString;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Enumeration of road surfaces ("what surface does the road have?").
 *
 * @see <a href="http://xml.nls.fi/XML/Schema/Maastotietojarjestelma/MTK/201405/Koodistot/Paallystetieto.xsd">Paallystetieto.xsd</a>
 */
public enum RoadSurface implements Code<String> {

    UNKNOWN("0", "Tuntematon", "Okänd"),
    NO_SURFACE("1", "Ei päällystettä", "Ingen beläggning"),
    DURABLE_SURFACE("2", "Kestopäällyste", "Beständig beläggning");

    private static final Map<String, RoadSurface> codeMap = new HashMap<>();

    static {
        Stream.of(values()).forEach(code -> codeMap.put(code.code, code));
    }

    final String code;
    final LocalizedString description;

    RoadSurface(String code, String descriptionFin, String descriptionSwe) {
        this.code = code;
        this.description = LocalizedString.builder()
                .withValue(Language.FINNISH, descriptionFin)
                .withValue(Language.SWEDISH, descriptionSwe)
                .build();
    }

    /**
     * Returns the road surface with the given code.
     */
    public static @NotNull Optional<RoadSurface> findByCode(@NotNull String code) {
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
