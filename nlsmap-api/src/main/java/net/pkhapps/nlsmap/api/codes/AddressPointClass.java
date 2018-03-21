package net.pkhapps.nlsmap.api.codes;

import net.pkhapps.nlsmap.api.types.Language;
import net.pkhapps.nlsmap.api.types.LocalizedString;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Enumeration of address point classes.
 *
 * @see <a href="http://xml.nls.fi/XML/Schema/Maastotietojarjestelma/MTK/201405/Koodistot/OsoitepisteSymboliluokka.xsd">OsoitepisteSymboliluokka.xsd</a>
 */
public enum AddressPointClass implements Code<String> {

    ADDRESS("96001", "Lähiosoite", "Näradress"),
    POINT_OF_ENTRY("96002", "Kulkupaikka", "Passeringspunkt"),
    UNKNOWN("", "Tuntematon", "Okänd");

    private static final Map<String, AddressPointClass> codeMap = new HashMap<>();

    static {
        Stream.of(values()).forEach(code -> codeMap.put(code.code, code));
    }

    final String code;
    final LocalizedString description;

    AddressPointClass(String code, String descriptionFin, String descriptionSwe) {
        this.code = code;
        this.description = LocalizedString.builder()
                .withValue(Language.FINNISH, descriptionFin)
                .withValue(Language.SWEDISH, descriptionSwe)
                .build();
    }

    /**
     * Returns the address point class with the given code.
     */
    public static @NotNull Optional<AddressPointClass> findByCode(@NotNull String code) {
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
