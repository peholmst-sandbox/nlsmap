package net.pkhapps.nlsmap.api.codes;

import net.pkhapps.nlsmap.api.types.Language;
import net.pkhapps.nlsmap.api.types.LocalizedString;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Enumeration of relative elevations ("how high or low is the object in relation to the surface of the earth?").
 *
 * @see <a href="http://xml.nls.fi/XML/Schema/Maastotietojarjestelma/MTK/201405/Koodistot/Vertikaalisuhde.xsd">Vertikaalisuhde.xsd</a>
 */
public enum RelativeElevation implements Code<String> {

    IN_TUNNEL("-11", "Tunnelissa", "I tunnel"),
    BELOW_SURFACE_LEVEL_3("-3", "Pinnan alla taso 3", "Under ytan nivå 3"),
    BELOW_SURFACE_LEVEL_2("-2", "Pinnan alla taso 2", "Under ytan nivå 2"),
    BELOW_SURFACE_LEVEL_1("-1", "Pinnan alla taso 1", "Under ytan nivå 1"),
    ON_SURFACE("0", "Pinnalla", "På ytan"),
    ABOVE_SURFACE_LEVEL_1("1", "Pinnan yllä taso 1", "Ovanför ytan nivå 1"),
    ABOVE_SURFACE_LEVEL_2("2", "Pinnan yllä taso 2", "Ovanför ytan nivå 2"),
    ABOVE_SURFACE_LEVEL_3("3", "Pinnan yllä taso 3", "Ovanför ytan nivå 3"),
    ABOVE_SURFACE_LEVEL_4("4", "Pinnan yllä taso 4", "Ovanför ytan nivå 4"),
    ABOVE_SURFACE_LEVEL_5("5", "Pinnan yllä taso 5", "Ovanför ytan nivå 5"),
    UNDEFINED("10", "Määrittelemätön", "Odefinierad");

    private static final Map<String, RelativeElevation> codeMap = new HashMap<>();

    static {
        Stream.of(values()).forEach(code -> codeMap.put(code.code, code));
    }

    final String code;
    final LocalizedString description;

    RelativeElevation(String code, String descriptionFin, String descriptionSwe) {
        this.code = code;
        this.description = LocalizedString.builder()
                .withValue(Language.FINNISH, descriptionFin)
                .withValue(Language.SWEDISH, descriptionSwe)
                .build();
    }

    /**
     * Returns the relative elevation with the given code.
     */
    public static @NotNull Optional<RelativeElevation> findByCode(@NotNull String code) {
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
