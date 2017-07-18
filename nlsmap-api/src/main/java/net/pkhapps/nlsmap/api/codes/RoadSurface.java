package net.pkhapps.nlsmap.api.codes;

import net.pkhapps.nlsmap.api.types.Language;
import net.pkhapps.nlsmap.api.types.LocalizedString;

/**
 * Enumeration of road surfaces ("what surface does the road have?").
 *
 * @see <a href="http://xml.nls.fi/XML/Schema/Maastotietojarjestelma/MTK/201405/Koodistot/Paallystetieto.xsd">Paallystetieto.xsd</a>
 */
public enum RoadSurface implements Code {

    UNKNOWN(0, "Tuntematon", "Okänd"),
    NO_SURFACE(1, "Ei päällystettä", "Ingen beläggning"),
    DURABLE_SURFACE(2, "Kestopäällyste", "Beständig beläggning");

    final int code;
    final LocalizedString description;

    RoadSurface(int code, String descriptionFin, String descriptionSwe) {
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
