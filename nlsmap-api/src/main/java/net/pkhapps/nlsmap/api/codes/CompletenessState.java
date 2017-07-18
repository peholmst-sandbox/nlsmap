package net.pkhapps.nlsmap.api.codes;

import net.pkhapps.nlsmap.api.types.Language;
import net.pkhapps.nlsmap.api.types.LocalizedString;

/**
 * Enumeration of completeness states ("how complete is the road?").
 *
 * @see <a href="http://xml.nls.fi/XML/Schema/Maastotietojarjestelma/MTK/201405/Koodistot/Valmiusaste.xsd">Valmiusaste.xsd</a>
 */
public enum CompletenessState implements Code {

    IN_USE(0, "Käytössä", "I bruk"),
    UNDER_CONSTRUCTION(1, "Rakenteilla", "Under konstruktion"),
    IN_PLANNING(3, "Suunnitteilla", "Under planering");

    final int code;
    final LocalizedString description;

    CompletenessState(int code, String descriptionFin, String descriptionSwe) {
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
