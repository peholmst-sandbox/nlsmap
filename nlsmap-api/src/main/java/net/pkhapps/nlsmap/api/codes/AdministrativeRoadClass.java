package net.pkhapps.nlsmap.api.codes;

import net.pkhapps.nlsmap.api.types.Language;
import net.pkhapps.nlsmap.api.types.LocalizedString;

/**
 * Enumeration of administrative road classes ("who owns/maintains the road?").
 *
 * @see <a href="http://xml.nls.fi/XML/Schema/Maastotietojarjestelma/MTK/201405/Koodistot/MtjHallinnollinenTieluokka.xsd">MtjHallinnollinenTieluokka.xsd</a>
 */
public enum AdministrativeRoadClass implements Code {

    STATE(1, "Valtio", "Stat"),
    MUNICIPALITY(2, "Kunta", "Kommun"),
    PRIVATE(3, "Yksityinen", "Enskild");

    final int code;
    final LocalizedString description;

    AdministrativeRoadClass(int code, String descriptionFin, String descriptionSwe) {
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
