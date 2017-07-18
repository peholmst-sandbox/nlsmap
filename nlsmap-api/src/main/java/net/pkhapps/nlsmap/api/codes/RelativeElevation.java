package net.pkhapps.nlsmap.api.codes;

import net.pkhapps.nlsmap.api.types.Language;
import net.pkhapps.nlsmap.api.types.LocalizedString;

/**
 * Enumeration of relative elevations ("how high or low is the object in relation to the surface of the earth?").
 *
 * @see <a href="http://xml.nls.fi/XML/Schema/Maastotietojarjestelma/MTK/201405/Koodistot/Vertikaalisuhde.xsd">Vertikaalisuhde.xsd</a>
 */
public enum RelativeElevation implements Code {

    IN_TUNNEL(-11, "Tunnelissa", "I tunnel"),
    BELOW_SURFACE(-1, "Pinnan alla", "Under ytan"),
    ON_SURFACE(0, "Pinnalla", "På ytan"),
    ABOVE_SURFACE_LEVEL_1(1, "Pinnan yllä taso 1", "Ovanför ytan nivå 1"),
    ABOVE_SURFACE_LEVEL_2(2, "Pinnan yllä taso 2", "Ovanför ytan nivå 2"),
    ABOVE_SURFACE_LEVEL_3(3, "Pinnan yllä taso 3", "Ovanför ytan nivå 3"),
    ABOVE_SURFACE_LEVEL_4(4, "Pinnan yllä taso 4", "Ovanför ytan nivå 4"),
    ABOVE_SURFACE_LEVEL_5(5, "Pinnan yllä taso 5", "Ovanför ytan nivå 5"),
    UNDEFINED(10, "Määrittelemätön", "Odefinierad");

    final int code;
    final LocalizedString description;

    RelativeElevation(int code, String descriptionFin, String descriptionSwe) {
        this.code = code;
        this.description = new LocalizedString.Builder()
                .withValue(Language.FINNISH, descriptionFin)
                .withValue(Language.SWEDISH, descriptionSwe)
                .build();
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public LocalizedString getDescription() {
        return description;
    }
}
