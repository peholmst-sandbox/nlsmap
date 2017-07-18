package net.pkhapps.nlsmap.api.codes;

import net.pkhapps.nlsmap.api.types.Language;
import net.pkhapps.nlsmap.api.types.LocalizedString;

/**
 * Enumeration of road classes.
 *
 * @see <a href="http://xml.nls.fi/XML/Schema/Maastotietojarjestelma/MTK/201405/Koodistot/Tieluokka.xsd">Tieluokka.xsd</a>
 */
public enum RoadClass implements Code {

    MOTORABLE_ROAD_IA(12111, "Autotie Ia", "Bilväg Ia"),
    MOTORABLE_ROAD_IB(12112, "Autotie Ib", "Bilväg Ib"),
    MOTORABLE_ROAD_IIA(12121, "Autotie IIa", "Bilväg IIa"),
    MOTORABLE_ROAD_IIB(12122, "Autotie IIb", "Bilväg IIb"),
    MOTORABLE_ROAD_IIIA(12131, "Autotie IIIa", "Bilväg IIIa"),
    MOTORABLE_ROAD_IIIB(12132, "Autotie IIIb", "Bilväg IIIb"),
    ROADWAY(12141, "Ajotie", "Körväg"),
    FERRY(12151, "Lautta", "Färja"),
    CABLE_FERRY(12152, "Lossi", "Vajerfärja"),
    SERVICE_ROAD_WITHOUT_BOOM_BARRIER(12153, "Huoltoaukko ilman puomia", "Serviceväg utan bom"),
    SERVICE_ROAD_WITH_BOOM_BARRIER(12154, "Huoltoaukko puomilla", "Serviceväg med bom"),
    OLD_TRACK(12311, "Vanha ajopolku", "Gammal körstig"),
    WINTER_ROAD(12312, "Talvitie", "Vinterväg"),
    PATH(12313, "Polku", "Stig"),
    SIDEWALK(12314, "Kävely/pyörätie", "Gång/cykelväg"),
    TRACK(12316, "Ajopolku", "Körstig");

    final int code;
    final LocalizedString description;

    RoadClass(int code, String descriptionFin, String descriptionSwe) {
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
