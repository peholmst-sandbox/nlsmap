package net.pkhapps.nlsmap.api.codes;

import net.pkhapps.nlsmap.api.types.Language;
import net.pkhapps.nlsmap.api.types.LocalizedString;
import org.jetbrains.annotations.NotNull;

/**
 * Enumeration of road surfaces ("what surface does the road have?").
 *
 * @see <a href="http://xml.nls.fi/XML/Schema/Maastotietojarjestelma/MTK/201405/Koodistot/Paallystetieto.xsd">Paallystetieto.xsd</a>
 */
public enum RoadSurface implements Code<String> {

    UNKNOWN("0", "Tuntematon", "Okänd"),
    NO_SURFACE("1", "Ei päällystettä", "Ingen beläggning"),
    DURABLE_SURFACE("2", "Kestopäällyste", "Beständig beläggning");

    final String code;
    final LocalizedString description;

    RoadSurface(String code, String descriptionFin, String descriptionSwe) {
        this.code = code;
        this.description = new LocalizedString.Builder()
                .withValue(Language.FINNISH, descriptionFin)
                .withValue(Language.SWEDISH, descriptionSwe)
                .build();
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
