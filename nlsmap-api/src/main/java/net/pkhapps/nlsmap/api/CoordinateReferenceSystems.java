package net.pkhapps.nlsmap.api;

import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * TODO Document me!
 */
public final class CoordinateReferenceSystems {

    public static final CoordinateReferenceSystem ETRS89_TM35FIN;
    public static final CoordinateReferenceSystem WGS84;

    static {
        try {
            // Source: http://spatialreference.org/ref/epsg/etrs89-etrs-tm35fin/prettywkt/
            ETRS89_TM35FIN = CRS.parseWKT("PROJCS[\"ETRS89 / ETRS-TM35FIN\",\n" +
                    "    GEOGCS[\"ETRS89\",\n" +
                    "        DATUM[\"European_Terrestrial_Reference_System_1989\",\n" +
                    "            SPHEROID[\"GRS 1980\",6378137,298.257222101,\n" +
                    "                AUTHORITY[\"EPSG\",\"7019\"]],\n" +
                    "            AUTHORITY[\"EPSG\",\"6258\"]],\n" +
                    "        PRIMEM[\"Greenwich\",0,\n" +
                    "            AUTHORITY[\"EPSG\",\"8901\"]],\n" +
                    "        UNIT[\"degree\",0.01745329251994328,\n" +
                    "            AUTHORITY[\"EPSG\",\"9122\"]],\n" +
                    "        AUTHORITY[\"EPSG\",\"4258\"]],\n" +
                    "    UNIT[\"metre\",1,\n" +
                    "        AUTHORITY[\"EPSG\",\"9001\"]],\n" +
                    "    PROJECTION[\"Transverse_Mercator\"],\n" +
                    "    PARAMETER[\"latitude_of_origin\",0],\n" +
                    "    PARAMETER[\"central_meridian\",27],\n" +
                    "    PARAMETER[\"scale_factor\",0.9996],\n" +
                    "    PARAMETER[\"false_easting\",500000],\n" +
                    "    PARAMETER[\"false_northing\",0],\n" +
                    "    AUTHORITY[\"EPSG\",\"3067\"],\n" +
                    "    AXIS[\"Easting\",EAST],\n" +
                    "    AXIS[\"Northing\",NORTH]]");
        } catch (FactoryException ex) {
            throw new IllegalStateException("Could not decode CRS EPSG:3067 (ETRS89 / TM35FIN)", ex);
        }

        try {
            // Source: http://spatialreference.org/ref/epsg/wgs-84/prettywkt/
            WGS84 = CRS.parseWKT("GEOGCS[\"WGS 84\",\n" +
                    "    DATUM[\"WGS_1984\",\n" +
                    "        SPHEROID[\"WGS 84\",6378137,298.257223563,\n" +
                    "            AUTHORITY[\"EPSG\",\"7030\"]],\n" +
                    "        AUTHORITY[\"EPSG\",\"6326\"]],\n" +
                    "    PRIMEM[\"Greenwich\",0,\n" +
                    "        AUTHORITY[\"EPSG\",\"8901\"]],\n" +
                    "    UNIT[\"degree\",0.01745329251994328,\n" +
                    "        AUTHORITY[\"EPSG\",\"9122\"]],\n" +
                    "    AUTHORITY[\"EPSG\",\"4326\"]]");
        } catch (FactoryException ex) {
            throw new IllegalStateException("Could not decode CRS EPSG:4326 (WGS84)");
        }
    }

    private CoordinateReferenceSystems() {
        // NOP
    }
}
