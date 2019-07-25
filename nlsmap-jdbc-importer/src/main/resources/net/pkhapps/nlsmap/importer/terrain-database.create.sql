CREATE TABLE road_name (
    id                  IDENTITY        NOT NULL,
    name_fi             VARCHAR(255),
    name_sv             VARCHAR(255),
    municipality_code   INTEGER         NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (municipality_code) REFERENCES municipality (code)
);

CREATE INDEX road_name_fi_index ON road_name (name_fi);
CREATE INDEX road_name_sv_index ON road_name (name_sv);

CREATE TABLE road_segment (
    gid                 BIGINT          NOT NULL,
    material_provider   INTEGER         NOT NULL,
    start_date          DATE            NOT NULL,
    dimensions          INTEGER         NOT NULL,
    location_accuracy   INTEGER         NOT NULL,
    location            GEOMETRY,
    elevation_accuracy  INTEGER         NOT NULL,
    elevation           INTEGER         NOT NULL,
    municipality_code   INTEGER         NOT NULL,
    road_number         INTEGER,
    road_name_id        BIGINT,
    road_class          INTEGER         NOT NULL,
    road_surface        INTEGER         NOT NULL,
    completeness_state  INTEGER         NOT NULL,
    travel_direction    INTEGER         NOT NULL,
    min_addr_no_left    INTEGER,
    max_addr_no_left    INTEGER,
    min_addr_no_right   INTEGER,
    max_addr_no_right   INTEGER,
    PRIMARY KEY (gid),
    FOREIGN KEY (road_name_id) REFERENCES road_name (id),
    FOREIGN KEY (municipality_code) REFERENCES municipality (code)
);

CREATE SPATIAL INDEX road_segment_location_index ON road_segment (location);

--CREATE TABLE address_point (
--);