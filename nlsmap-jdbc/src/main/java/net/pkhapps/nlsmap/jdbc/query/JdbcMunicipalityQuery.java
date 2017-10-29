package net.pkhapps.nlsmap.jdbc.query;

import net.pkhapps.nlsmap.api.codes.Municipality;
import net.pkhapps.nlsmap.api.query.MunicipalityQuery;
import net.pkhapps.nlsmap.api.types.Language;
import net.pkhapps.nlsmap.api.types.LocalizedString;
import net.pkhapps.nlsmap.api.types.SearchMode;
import net.pkhapps.nlsmap.jdbc.ConnectionSupplier;
import org.geotools.geometry.DirectPosition2D;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * JDBC implementation of {@link MunicipalityQuery}. This query is stateless and does not maintain any local caches.
 */
public class JdbcMunicipalityQuery implements MunicipalityQuery {

    /**
     * Limit the query just in case, there should only be around 300 records in this table.
     */
    private static final int DEFAULT_LIMIT = 1000;
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcMunicipalityQuery.class);

    private final ConnectionSupplier connectionSupplier;

    /**
     * Creates a new {@code JdbcMunicipalityQuery}.
     *
     * @param connectionSupplier a connection supplier used to communicate with the database.
     */
    public JdbcMunicipalityQuery(@NotNull ConnectionSupplier connectionSupplier) {
        this.connectionSupplier = Objects.requireNonNull(connectionSupplier);
    }

    @Override
    public @NotNull Optional<Municipality> findByPoint(@NotNull DirectPosition2D point) {
        return Optional.empty(); // TODO Implement findByPoint
    }

    @Override
    public @NotNull List<Municipality> findByName(@NotNull String name, @NotNull SearchMode mode) {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(mode, "mode must not be null");
        if (name.isEmpty()) {
            return Collections.emptyList();
        }

        StringBuilder queryBuilder = new StringBuilder("SELECT code, name_fi, name_sv FROM municipality WHERE ");
        switch (mode) {
            case CONTAINS:
            case STARTS_WITH:
                queryBuilder.append("LOWER(name_fi) LIKE ? OR LOWER(name_sv) LIKE ?");
                break;
            case EXACT_MATCH:
                queryBuilder.append("LOWER(name_fi) = ? OR LOWER(name_sv) = ?");
        }
        queryBuilder.append(" ORDER BY code LIMIT ");
        queryBuilder.append(DEFAULT_LIMIT);

        LOGGER.trace("Using query to find municipalities by name: [{}]", queryBuilder);

        String searchTerm = name.toLowerCase().trim();
        if (mode == SearchMode.CONTAINS) {
            searchTerm = String.format("%%%s%%", searchTerm);
        } else if (mode == SearchMode.STARTS_WITH) {
            searchTerm = String.format("%s%%", searchTerm);
        }

        LOGGER.trace("Using search term to find municipalities by name: [{}]", searchTerm);

        try (PreparedStatement stmt = connectionSupplier.get().prepareStatement(queryBuilder.toString())) {
            stmt.setString(1, searchTerm);
            stmt.setString(2, searchTerm);
            try (ResultSet result = stmt.executeQuery()) {
                return mapMunicipalities(result);
            }
        } catch (SQLException ex) {
            LOGGER.error("Error while finding municipalities by name");
        }
        return Collections.emptyList();
    }

    @Override
    public @NotNull Optional<DirectPosition2D> findMapCenterOf(@NotNull Municipality municipality) {
        return Optional.empty(); // TODO Implement findMapCenterOf
    }

    @Override
    public @NotNull List<Municipality> findAll() {
        try (Statement stmt = connectionSupplier.get().createStatement()) {
            try (ResultSet result = stmt
                    .executeQuery(
                            "SELECT code, name_fi, name_sv FROM municipality ORDER BY code LIMIT " + DEFAULT_LIMIT)) {
                return mapMunicipalities(result);
            }
        } catch (SQLException ex) {
            LOGGER.error("Error while finding all municipalities", ex);
        }
        return Collections.emptyList();
    }

    private List<Municipality> mapMunicipalities(ResultSet resultSet) throws SQLException {
        List<Municipality> municipalities = new ArrayList<>();
        while (resultSet.next()) {
            municipalities.add(mapMunicipality(resultSet));
        }
        return municipalities;
    }

    private Municipality mapMunicipality(ResultSet resultSet) throws SQLException {
        final int code = resultSet.getInt("code");
        final LocalizedString name = new LocalizedString.Builder()
                .withValue(Language.FINNISH, resultSet.getString("name_fi"))
                .withValue(Language.SWEDISH, resultSet.getString("name_sv"))
                .build();

        return new Municipality(code, name);
    }

}
