package net.pkhapps.nlsmap.api.codes;

import net.pkhapps.nlsmap.api.types.Language;
import net.pkhapps.nlsmap.api.types.LocalizedString;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Enumeration of location accuracy classes ("how accurate is the given set of coordinates?").
 *
 * @see <a href="http://xml.nls.fi/XML/Schema/Maastotietojarjestelma/MTK/201405/Koodistot/Sijaintitarkkuusluokka.xsd">Sijaintitarkkuusluokka.xsd</a>
 */
public enum LocationAccuracy implements Code<String> {

    UNDEFINED("0", "Ei määr.", "Odef."),
    MM500("500", "0,5 m"),
    MM800("800", "0,8 m"),
    MM1000("1000", "1 m"),
    MM2000("2000", "2 m"),
    MM3000("3000", "3 m"),
    MM4000("4000", "4 m"),
    MM5000("5000", "5 m"),
    MM7500("7500", "7,5 m"),
    MM8000("8000", "8 m"),
    MM10000("10000", "10 m"),
    MM12500("12500", "12,5 m"),
    MM15000("15000", "15 m"),
    MM20000("20000", "20 m"),
    MM25000("25000", "25 m"),
    MM30000("30000", "30 m"),
    MM40000("40000", "40 m"),
    MM80000("80000", "80 m"),
    MM100000("10000", "100 m");

    private static final Map<String, LocationAccuracy> codeMap = new HashMap<>();

    static {
        Stream.of(values()).forEach(code -> codeMap.put(code.code, code));
    }

    final String code;
    final LocalizedString description;

    LocationAccuracy(String code, String descriptionFin, String descriptionSwe) {
        this.code = code;
        this.description = LocalizedString.builder()
                .withValue(Language.FINNISH, descriptionFin)
                .withValue(Language.SWEDISH, descriptionSwe)
                .build();
    }

    LocationAccuracy(String code, String description) {
        this(code, description, description);
    }

    /**
     * Returns the location accuracy with the given code.
     */
    public static @NotNull Optional<LocationAccuracy> findByCode(@NotNull String code) {
        return Optional.ofNullable(codeMap.get(code));
    }

    @Override
    public @NotNull String getCode() {
        return code;
    }

    @Override
    public @NotNull LocalizedString getDescription() {
        return description;
    }
}
