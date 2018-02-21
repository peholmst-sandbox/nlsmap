package net.pkhapps.nlsmap.api.codes;

import net.pkhapps.nlsmap.api.types.Language;
import net.pkhapps.nlsmap.api.types.LocalizedString;
import org.jetbrains.annotations.NotNull;

/**
 * Enumeration of completeness states ("how complete is the road?").
 *
 * @see <a href="http://xml.nls.fi/XML/Schema/Maastotietojarjestelma/MTK/201405/Koodistot/Valmiusaste.xsd">Valmiusaste.xsd</a>
 */
public enum CompletenessState implements Code<String> {

    IN_USE("0", "Käytössä", "I bruk"),
    UNDER_CONSTRUCTION("1", "Rakenteilla", "Under konstruktion"),
    IN_PLANNING("3", "Suunnitteilla", "Under planering");

    final String code;
    final LocalizedString description;

    CompletenessState(String code, String descriptionFin, String descriptionSwe) {
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
