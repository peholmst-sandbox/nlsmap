package net.pkhapps.nlsmap.importer;

import org.geotools.coverage.grid.GeneralGridEnvelope;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.gce.imagemosaic.jdbc.ImageMosaicJDBCFormat;
import org.geotools.gce.imagemosaic.jdbc.ImageMosaicJDBCReader;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;

import java.awt.*;
import java.net.URL;

/**
 * Created by petterprivate on 24/06/2017.
 */
public class MapBrowser {

    public static void main(String[] args) throws Exception {
        URL configUrl = MapBrowser.class.getResource("/pgraster.xml");
        AbstractGridFormat format = GridFormatFinder.findFormat(configUrl);
        ImageMosaicJDBCReader reader = (ImageMosaicJDBCReader) format.getReader(configUrl, null);


        ParameterValue<GridGeometry2D> gg = AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();

        GeneralEnvelope envelope = new GeneralEnvelope(new double[]{236000.0, 6672000.0}, new double[]{242000.0, 6678000.0});

        envelope.setCoordinateReferenceSystem(CRS.decode("EPSG:3067"));
        gg.setValue(new GridGeometry2D(new GeneralGridEnvelope(new Rectangle(0, 0, 1000, 1000)), envelope));

        final ParameterValue outTransp = ImageMosaicJDBCFormat.BACKGROUND_COLOR.createValue();
        outTransp.setValue(Color.WHITE);
        GridCoverage2D coverage = (GridCoverage2D) reader.read(new GeneralParameterValue[]{gg, outTransp});
        coverage.show("Hello world");
        /*
        MapContent content = new MapContent();
        content.addLayer(new GridReaderLayer(reader, null));
        // add map layers here
        JMapFrame.showMap(content);*/
    }
}
