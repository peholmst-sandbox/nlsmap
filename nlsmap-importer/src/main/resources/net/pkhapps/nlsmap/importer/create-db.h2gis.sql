CREATE ALIAS IF NOT EXISTS H2GIS_EXTENSION FOR "org.h2gis.ext.H2GISExtension.load";
CALL H2GIS_EXTENSION();

CREATE TABLE IF NOT EXISTS bg_raster_1_5000 (
  source VARCHAR(20) NOT NULL,
  tile_x INTEGER NOT NULL,
  tile_y INTEGER NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  geom GEOMETRY,
  image BLOB,
  image_type VARCHAR(20) NOT NULL,
  PRIMARY KEY (source, tile_x, tile_y)
);

CREATE SPATIAL INDEX IF NOT EXISTS bg_raster_1_5000_geom_ix ON bg_raster_1_5000(geom);
