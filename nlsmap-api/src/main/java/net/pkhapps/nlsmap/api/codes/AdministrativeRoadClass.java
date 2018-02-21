package net.pkhapps.nlsmap.api.codes;

import net.pkhapps.nlsmap.api.types.Language;
import net.pkhapps.nlsmap.api.types.LocalizedString;
import org.jetbrains.annotations.NotNull;

/**
 * Enumeration of administrative road classes ("who owns/maintains the road?").
 *
 * @see <a href="http://xml.nls.fi/XML/Schema/Maastotietojarjestelma/MTK/201405/Koodistot/MtjHallinnollinenTieluokka.xsd">MtjHallinnollinenTieluokka.xsd</a>
 */
public enum AdministrativeRoadClass implements Code<String> {

    STATE("1", "Valtio", "Stat"),
    MUNICIPALITY("2", "Kunta", "Kommun"),
    PRIVATE("3", "Yksityinen", "Enskild");

    final String code;
    final LocalizedString description;

    AdministrativeRoadClass(String code, String descriptionFin, String descriptionSwe) {
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
