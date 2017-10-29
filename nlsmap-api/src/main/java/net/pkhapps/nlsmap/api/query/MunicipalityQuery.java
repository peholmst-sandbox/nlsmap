package net.pkhapps.nlsmap.api.query;

import net.pkhapps.nlsmap.api.codes.Municipality;
import net.pkhapps.nlsmap.api.types.SearchMode;
import org.geotools.geometry.DirectPosition2D;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * Query interface for retrieving {@link Municipality} objects and related information from the database.
 */
public interface MunicipalityQuery {

    /**
     * Returns the municipality of the specified point.
     *
     * @param point the point to search for.
     * @return the municipality if found in the database.
     */
    @NotNull Optional<Municipality> findByPoint(@NotNull DirectPosition2D point);

    /**
     * Returns all municipalities whose names in any language match the specified name and search mode. The search is
     * case insensitive.
     *
     * @param name the name (or part of the name) to look for.
     * @param mode the mode to use when looking for matches.
     * @return a list of matching municipalities.
     */
    @NotNull List<Municipality> findByName(@NotNull String name, @NotNull SearchMode mode);

    /**
     * Returns the center point of the specified municipality. This function is intended for centering a map over the
     * municipality after selecting it from a list. The returned position need not necessarily be the exact center of
     * the municipality.
     *
     * @param municipality the municipality to search for.
     * @return the center point if found in the database.
     */
    @NotNull Optional<DirectPosition2D> findMapCenterOf(@NotNull Municipality municipality);

    /**
     * Returns all municipalities in Finland. There are only around 300 of them, so this list is not
     * paginated.
     *
     * @return a list of municipalities.
     */
    @NotNull List<Municipality> findAll();
}
