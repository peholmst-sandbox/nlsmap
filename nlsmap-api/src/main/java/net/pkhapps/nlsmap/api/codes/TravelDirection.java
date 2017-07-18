package net.pkhapps.nlsmap.api.codes;

import net.pkhapps.nlsmap.api.types.Language;
import net.pkhapps.nlsmap.api.types.LocalizedString;

/**
 * Enumeration of travel directions ("in which directions can I travel on the road?").
 *
 * @see <a href="http://xml.nls.fi/XML/Schema/Maastotietojarjestelma/MTK/201405/Koodistot/Yksisuuntaisuus.xsd">Yksisuuntaisuus.xsd</a>
 */
public enum TravelDirection implements Code {

    TWO_WAY(0, "Kaksisuuntainen", "Dubbelriktad"),
    IN_DIGITIZATION_DIRECTION(1, "Digitointisuunnassa", "I digitaliseringsriktningen"),
    AGAINST_DIGITIZATION_DIRECTION(2, "Digitointisuuntaa vastaan", "Mot digitaliseringsriktningen");

    final int code;
    final LocalizedString description;

    TravelDirection(int code, String descriptionFin, String descriptionSwe) {
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
