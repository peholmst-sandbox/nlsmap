package net.pkhapps.nlsmap.api.codes;

import net.pkhapps.nlsmap.api.types.Language;
import net.pkhapps.nlsmap.api.types.LocalizedString;

/**
 * Enumeration of map material providers ("where does this data come from?").
 *
 * @see <a href="http://xml.nls.fi/XML/Schema/Maastotietojarjestelma/MTK/201405/Koodistot/Aineistolahde.xsd">Aineistolahde.xsd</a>
 */
public enum MaterialProvider implements Code {

    OTHER(0, "Muu", "Annan"),
    MML(1, "Maanmittauslaitos", "Lantmäteriverket"),
    SYKE(2, "Suomen ympäristökeskus", "Finlands miljöcentral"),
    MKL(3, "Merenkulkulaitos", "Sjöfartsverket"),
    MH(4, "Metsähallitus", "Forststyrelsen"),
    PV(5, "Puolustusvoimat", "Försvarsmakten"),
    MUNICIPALITY(6, "Kunta", "Kommun");

    final int code;
    final LocalizedString description;

    MaterialProvider(int code, String descriptionFin, String descriptionSwe) {
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
