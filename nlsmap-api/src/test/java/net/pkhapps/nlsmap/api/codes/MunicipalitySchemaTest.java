package net.pkhapps.nlsmap.api.codes;

import net.pkhapps.nlsmap.api.types.Language;
import net.pkhapps.nlsmap.api.types.LocalizedString;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test case that makes sure that the {@link Municipality} enum is up to date with the XML schema.
 *
 * @see <a href="http://xml.nls.fi/Nimisto/Nimistorekisteri/kunta.xsd">kunta.xsd</a>
 */
public class MunicipalitySchemaTest {

    private @NotNull Document getSchema() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.parse(new URL("http://xml.nls.fi/Nimisto/Nimistorekisteri/kunta.xsd").openStream());
    }

    @Test
    public void verifyThatEnumContainsEverythingInRemoteSchema() throws Exception {
        Document document = getSchema();
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        XPathExpression findEnumerations = xPath.compile("schema/simpleType/restriction/enumeration");
        XPathExpression findDocumentation = xPath.compile("annotation/documentation");
        NodeList enumerations = (NodeList) findEnumerations.evaluate(document, XPathConstants.NODESET);
        List<Runnable> tests = new ArrayList();
        for (int i = 0; i < enumerations.getLength(); ++i) {
            Node item = enumerations.item(i);
            String id = item.getAttributes().getNamedItem("value").getTextContent();
            NodeList documentations = (NodeList) findDocumentation.evaluate(item, XPathConstants.NODESET);
            String nameFin = documentations.item(0).getTextContent();
            String nameSwe = documentations.item(1).getTextContent();
            // Generate some output that can be copy-pasted into the Municipality enum
            System.out.println(String.format("%s(\"%s\",\"%s\",\"%s\"),", stripScandinavianCharacters(nameFin.toUpperCase()), id, nameFin, nameSwe));
            // Store the test case to be run at a later time
            tests.add(() -> {
                Optional<Municipality> municipality = Municipality.findByCode(id);
                assertThat(municipality).isPresent();
                assertThat(municipality).map(Municipality::getDescription).hasValue(
                        new LocalizedString.Builder()
                                .withValue(Language.FINNISH, nameFin)
                                .withValue(Language.SWEDISH, nameSwe)
                                .build());
            });
        }
        // Run the tests. That way, if there is a failure, the programmer can just copy-paste the output into the enum
        // to fix any missing municipalities.
        tests.forEach(Runnable::run);
    }

    private String stripScandinavianCharacters(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            switch (c) {
                case 'Å':
                case 'Ä':
                    sb.append('A');
                    break;
                case 'Ö':
                    sb.append('O');
                    break;
                case 'å':
                case 'ä':
                    sb.append('a');
                    break;
                case 'ö':
                    sb.append('o');
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }
}
