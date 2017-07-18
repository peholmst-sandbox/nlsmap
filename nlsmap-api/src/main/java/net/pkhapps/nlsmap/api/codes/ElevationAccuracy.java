package net.pkhapps.nlsmap.api.codes;

import net.pkhapps.nlsmap.api.types.Language;
import net.pkhapps.nlsmap.api.types.LocalizedString;

/**
 * Enumeration of elevation accuracy classes ("how accurate is the given elevation?").
 *
 * @see <a href="http://xml.nls.fi/XML/Schema/Maastotietojarjestelma/MTK/201405/Koodistot/Korkeustarkkuusluokka.xsd">Korkeustarkkuusluokka.xsd</a>
 */
public enum ElevationAccuracy implements Code {

    UNDEFINED(1, "Ei määr.", "Odef."),
    KM2M(201, "KM 2 m"),
    MM500(500, "0,5 m"),
    MM800(800, "0,8 m"),
    MM1000(1000, "1 m"),
    MM2000(2000, "2 m"),
    MM3000(3000, "3 m"),
    MM4000(4000, "4 m"),
    MM5000(5000, "5 m"),
    MM7500(7500, "7,5 m"),
    MM8000(8000, "8 m"),
    MM10000(10000, "10 m"),
    MM12500(12500, "12,5 m"),
    MM15000(15000, "15 m"),
    MM20000(20000, "20 m"),
    MM25000(25000, "25 m"),
    MM30000(30000, "30 m"),
    MM40000(40000, "40 m"),
    MM80000(80000, "80 m"),
    MM100000(100000, "100 m"),
    KM100001(100001, "KM 10 m"),
    KM250001(250001, "KM 25 m");

    final int code;
    final LocalizedString description;

    ElevationAccuracy(int code, String descriptionFin, String descriptionSwe) {
        this.code = code;
        this.description = new LocalizedString.Builder()
                .withValue(Language.FINNISH, descriptionFin)
                .withValue(Language.SWEDISH, descriptionSwe)
                .build();
    }

    ElevationAccuracy(int code, String description) {
        this(code, description, description);
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
