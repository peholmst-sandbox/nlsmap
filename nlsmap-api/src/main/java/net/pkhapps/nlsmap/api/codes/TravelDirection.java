package net.pkhapps.nlsmap.api.codes;

import net.pkhapps.nlsmap.api.types.Language;
import net.pkhapps.nlsmap.api.types.LocalizedString;
import org.jetbrains.annotations.NotNull;

/**
 * Enumeration of travel directions ("in which directions can I travel on the road?").
 *
 * @see <a href="http://xml.nls.fi/XML/Schema/Maastotietojarjestelma/MTK/201405/Koodistot/Yksisuuntaisuus.xsd">Yksisuuntaisuus.xsd</a>
 */
public enum TravelDirection implements Code<String> {

    TWO_WAY("0", "Kaksisuuntainen", "Dubbelriktad"),
    IN_DIGITIZATION_DIRECTION("1", "Digitointisuunnassa", "I digitaliseringsriktningen"),
    AGAINST_DIGITIZATION_DIRECTION("2", "Digitointisuuntaa vastaan", "Mot digitaliseringsriktningen");

    final String code;
    final LocalizedString description;

    TravelDirection(String code, String descriptionFin, String descriptionSwe) {
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
