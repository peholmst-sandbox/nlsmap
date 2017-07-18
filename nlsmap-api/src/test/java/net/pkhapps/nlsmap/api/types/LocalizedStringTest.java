package net.pkhapps.nlsmap.api.types;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link LocalizedString}.
 */
public class LocalizedStringTest {

    @Test
    public void jsonSerializationRoundTrip() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        LocalizedString str = new LocalizedString.Builder().withValue(Language.FINNISH, "finnish")
                .withValue(Language.SWEDISH, "swedish").build();
        String json = objectMapper.writeValueAsString(str);
        System.out.println(json);
        LocalizedString str2 = objectMapper.readValue(json, LocalizedString.class);
        assertThat(str).isEqualTo(str2);
    }
}
