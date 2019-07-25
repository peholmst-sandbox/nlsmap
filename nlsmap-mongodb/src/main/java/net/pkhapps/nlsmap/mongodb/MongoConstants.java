package net.pkhapps.nlsmap.mongodb;

/**
 * TODO Document me!
 */
public final class MongoConstants {

    public static final String DOCUMENT_ID = "_id";

    public static final String TERRAIN_DATABASE = "maastotietokanta";
    public static final String ROAD_SEGMENTS_COLLECTION = "tieviivat";

    private MongoConstants() {
    }

    public static final class RoadSegmentProperties {

        public static final String COMPLETENESS_STATE = "valmiusaste";
        public static final String END_DATE = "loppupvm";
        public static final String FEATURE_CLASS = "kohdeluokka";
        public static final String LOCATION = "sijainti";
        public static final String LOCATION_ACCURACY = "sijaintitarkkuus";
        public static final String MATERIAL_PROVIDER = "aineistolahde";
        public static final String MAX_ADDRESS_NUMBER_LEFT = "maxOsoitenumeroVasen";
        public static final String MAX_ADDRESS_NUMBER_RIGHT = "maxOsoitenumeroOikea";
        public static final String MIN_ADDRESS_NUMBER_LEFT = "minOsoitenumeroVasen";
        public static final String MIN_ADDRESS_NUMBER_RIGHT = "minOsoitenumeroOikea";
        public static final String MUNICIPALITY = "kuntatunnus";
        public static final String NAME_FIN = "nimi_suomi";
        public static final String NAME_SME = "nimi_pohjoissaame";
        public static final String NAME_SMN = "nimi_inarinsaame";
        public static final String NAME_SMS = "nimi_koltansaame";
        public static final String NAME_SWE = "nimi_ruotsi";
        public static final String RELATIVE_ELEVATION = "tasosijainti";
        public static final String ROAD_ADMINISTRATOR = "hallinnollinenLuokka";
        public static final String ROAD_NUMBER = "tienumero";
        public static final String ROAD_PART_NUMBER = "tieosanumero";
        public static final String START_DATE = "alkupvm";
        public static final String SURFACE = "paallyste";
        public static final String TRAVEL_DIRECTION = "yksisuuntaisuus";

        private RoadSegmentProperties() {
        }
    }
}
