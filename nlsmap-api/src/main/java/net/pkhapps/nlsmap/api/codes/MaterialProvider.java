package net.pkhapps.nlsmap.api.codes;

import net.pkhapps.nlsmap.api.types.Language;
import net.pkhapps.nlsmap.api.types.LocalizedString;
import org.jetbrains.annotations.NotNull;

/**
 * Enumeration of map material providers ("where does this data come from?").
 *
 * @see <a href="http://xml.nls.fi/XML/Schema/Maastotietojarjestelma/MTK/201405/Koodistot/Aineistolahde.xsd">Aineistolahde.xsd</a>
 */
public enum MaterialProvider implements Code<String> {

    OTHER("0", "Muu", "Annan"),
    MML("1", "Maanmittauslaitos", "Lantmäteriverket"),
    SYKE("2", "Suomen ympäristökeskus", "Finlands miljöcentral"),
    MKL("3", "Merenkulkulaitos", "Sjöfartsverket"),
    MH("4", "Metsähallitus", "Forststyrelsen"),
    PV("5", "Puolustusvoimat", "Försvarsmakten"),
    MUNICIPALITY("6", "Kunta", "Kommun");

    final String code;
    final LocalizedString description;

    MaterialProvider(String code, String descriptionFin, String descriptionSwe) {
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
