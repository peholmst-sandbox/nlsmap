package net.pkhapps.nlsmap.api.codes;

import net.pkhapps.nlsmap.api.types.Language;
import net.pkhapps.nlsmap.api.types.LocalizedString;
import org.jetbrains.annotations.NotNull;

/**
 * Enumeration of address point classes.
 *
 * @see <a href="http://xml.nls.fi/XML/Schema/Maastotietojarjestelma/MTK/201405/Koodistot/OsoitepisteSymboliluokka.xsd">OsoitepisteSymboliluokka.xsd</a>
 */
public enum AddressPointClass implements Code<String> {

    ADDRESS("96001", "Lähiosoite", "Näradress"),
    POINT_OF_ENTRY("96002", "Kulkupaikka", "Passeringspunkt");

    final String code;
    final LocalizedString description;

    AddressPointClass(String code, String descriptionFin, String descriptionSwe) {
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
