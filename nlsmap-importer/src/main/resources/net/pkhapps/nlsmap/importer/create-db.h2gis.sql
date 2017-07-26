CREATE ALIAS IF NOT EXISTS H2GIS_EXTENSION FOR "org.h2gis.ext.H2GISExtension.load";
CALL H2GIS_EXTENSION();


CREATE TABLE IF NOT EXISTS bg_raster_1_5000 (
  id VARCHAR(40) NOT NULL,
  map_sheet VARCHAR(20) NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  geom GEOMETRY,
  min_x DOUBLE NOT NULL,
  min_y DOUBLE NOT NULL,
  max_x DOUBLE NOT NULL,
  max_y DOUBLE NOT NULL,
  srid INTEGER NOT NULL,
  image BLOB NOT NULL,
  image_type VARCHAR(20) NOT NULL,
  PRIMARY KEY (id)
);

CREATE SPATIAL INDEX IF NOT EXISTS bg_raster_1_5000_geom_ix ON bg_raster_1_5000(geom);
CREATE INDEX IF NOT EXISTS bg_raster_1_5000_envelope_ix ON bg_raster_1_5000(min_x, min_y, max_x, max_y);


CREATE TABLE IF NOT EXISTS bg_raster_1_10000 (
  id VARCHAR(40) NOT NULL,
  map_sheet VARCHAR(20) NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  geom GEOMETRY,
  min_x DOUBLE NOT NULL,
  min_y DOUBLE NOT NULL,
  max_x DOUBLE NOT NULL,
  max_y DOUBLE NOT NULL,
  srid INTEGER NOT NULL,
  image BLOB NOT NULL,
  image_type VARCHAR(20) NOT NULL,
  PRIMARY KEY (id)
);

CREATE SPATIAL INDEX IF NOT EXISTS bg_raster_1_10000_geom_ix ON bg_raster_1_10000(geom);
CREATE INDEX IF NOT EXISTS bg_raster_1_10000_envelope_ix ON bg_raster_1_10000(min_x, min_y, max_x, max_y);


CREATE TABLE IF NOT EXISTS bg_raster_1_20000 (
  id VARCHAR(40) NOT NULL,
  map_sheet VARCHAR(20) NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  geom GEOMETRY,
  min_x DOUBLE NOT NULL,
  min_y DOUBLE NOT NULL,
  max_x DOUBLE NOT NULL,
  max_y DOUBLE NOT NULL,
  srid INTEGER NOT NULL,
  image BLOB NOT NULL,
  image_type VARCHAR(20) NOT NULL,
  PRIMARY KEY (id)
);

CREATE SPATIAL INDEX IF NOT EXISTS bg_raster_1_20000_geom_ix ON bg_raster_1_20000(geom);
CREATE INDEX IF NOT EXISTS bg_raster_1_20000_envelope_ix ON bg_raster_1_20000(min_x, min_y, max_x, max_y);


CREATE TABLE IF NOT EXISTS bg_raster_1_40000 (
  id VARCHAR(40) NOT NULL,
  map_sheet VARCHAR(20) NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  geom GEOMETRY,
  min_x DOUBLE NOT NULL,
  min_y DOUBLE NOT NULL,
  max_x DOUBLE NOT NULL,
  max_y DOUBLE NOT NULL,
  srid INTEGER NOT NULL,
  image BLOB NOT NULL,
  image_type VARCHAR(20) NOT NULL,
  PRIMARY KEY (id)
);

CREATE SPATIAL INDEX IF NOT EXISTS bg_raster_1_40000_geom_ix ON bg_raster_1_40000(geom);
CREATE INDEX IF NOT EXISTS bg_raster_1_40000_envelope_ix ON bg_raster_1_40000(min_x, min_y, max_x, max_y);


CREATE TABLE IF NOT EXISTS bg_raster_1_80000 (
  id VARCHAR(40) NOT NULL,
  map_sheet VARCHAR(20) NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  geom GEOMETRY,
  min_x DOUBLE NOT NULL,
  min_y DOUBLE NOT NULL,
  max_x DOUBLE NOT NULL,
  max_y DOUBLE NOT NULL,
  srid INTEGER NOT NULL,
  image BLOB NOT NULL,
  image_type VARCHAR(20) NOT NULL,
  PRIMARY KEY (id)
);

CREATE SPATIAL INDEX IF NOT EXISTS bg_raster_1_80000_geom_ix ON bg_raster_1_80000(geom);
CREATE INDEX IF NOT EXISTS bg_raster_1_80000_envelope_ix ON bg_raster_1_80000(min_x, min_y, max_x, max_y);


CREATE TABLE IF NOT EXISTS bg_raster_1_160000 (
  id VARCHAR(40) NOT NULL,
  map_sheet VARCHAR(20) NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  geom GEOMETRY,
  min_x DOUBLE NOT NULL,
  min_y DOUBLE NOT NULL,
  max_x DOUBLE NOT NULL,
  max_y DOUBLE NOT NULL,
  srid INTEGER NOT NULL,
  image BLOB NOT NULL,
  image_type VARCHAR(20) NOT NULL,
  PRIMARY KEY (id)
);

CREATE SPATIAL INDEX IF NOT EXISTS bg_raster_1_160000_geom_ix ON bg_raster_1_160000(geom);
CREATE INDEX IF NOT EXISTS bg_raster_1_160000_envelope_ix ON bg_raster_1_160000(min_x, min_y, max_x, max_y);


CREATE TABLE IF NOT EXISTS bg_raster_1_320000 (
  id VARCHAR(40) NOT NULL,
  map_sheet VARCHAR(20) NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  geom GEOMETRY,
  min_x DOUBLE NOT NULL,
  min_y DOUBLE NOT NULL,
  max_x DOUBLE NOT NULL,
  max_y DOUBLE NOT NULL,
  srid INTEGER NOT NULL,
  image BLOB NOT NULL,
  image_type VARCHAR(20) NOT NULL,
  PRIMARY KEY (id)
);

CREATE SPATIAL INDEX IF NOT EXISTS bg_raster_1_320000_geom_ix ON bg_raster_1_320000(geom);
CREATE INDEX IF NOT EXISTS bg_raster_1_320000_envelope_ix ON bg_raster_1_320000(min_x, min_y, max_x, max_y);
