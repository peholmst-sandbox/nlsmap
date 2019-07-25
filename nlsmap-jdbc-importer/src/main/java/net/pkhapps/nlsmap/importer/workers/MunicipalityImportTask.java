package net.pkhapps.nlsmap.importer.workers;

import javafx.beans.property.ObjectProperty;
import javafx.concurrent.Task;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicInteger;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

/**
 * Task that imports the Finnish municipality names from an NLS web service and inserts them into the database.
 */
public class MunicipalityImportTask extends Task<Void> {

    private static final String SOURCE = "http://xml.nls.fi/Nimisto/Nimistorekisteri/kunta.xsd";

    private static final String DROP_DDL = "DROP TABLE IF EXISTS municipality";
    private static final String CREATE_DDL = "CREATE TABLE municipality (" +
                                             "code INTEGER NOT NULL PRIMARY KEY," +
                                             "name_fi VARCHAR(255) NOT NULL," +
                                             "name_sv VARCHAR(255) NOT NULL)";
    private static final String INSERT_DML = "INSERT INTO municipality (code,name_fi,name_sv) VALUES (?,?,?)";

    private final ObjectProperty<Connection> connection;

    public MunicipalityImportTask(ObjectProperty<Connection> connection) {
        this.connection = connection;
    }

    @Override
    protected Void call() throws Exception {
        final Connection connection = this.connection.get();

        try (Statement statement = connection.createStatement()) {
            updateMessage("Dropping existing table if it exists");
            statement.execute(DROP_DDL);
            updateMessage("Creating new table");
            statement.execute(CREATE_DDL);
        }

        AtomicInteger count = new AtomicInteger();
        try (PreparedStatement insertStatement = connection.prepareStatement(INSERT_DML)) {
            updateMessage("Reading data from " + SOURCE);
            XMLInputFactory inputFactory = XMLInputFactory.newFactory();
            try (InputStream is = new URL(SOURCE).openStream()) {
                XMLStreamReader reader = inputFactory.createXMLStreamReader(is);
                updateMessage("Starting importer");
                while (reader.hasNext() && !isCancelled()) {
                    int eventType = reader.next();
                    if (eventType == START_ELEMENT && reader.getLocalName().equals("enumeration")) {
                        readEnumeration(reader, insertStatement, count);
                    }
                }
            }
        }

        updateMessage(String.format("Finished, imported %d municipalities", count.get()));
        return null;
    }

    private void readEnumeration(XMLStreamReader reader, PreparedStatement insertStatement, AtomicInteger count)
            throws Exception {
        int id = Integer.parseInt(reader.getAttributeValue(null, "value"));
        String nameFi = "";
        String nameSv = "";
        while (reader.hasNext()) {
            int eventType = reader.next();
            if (eventType == START_ELEMENT && reader.getLocalName().equals("documentation")) {
                String lang = reader.getAttributeValue(null, "lang");
                String value = reader.getElementText();
                if ("fin".equals(lang)) {
                    nameFi = value;
                } else if ("swe".equals(lang)) {
                    nameSv = value;
                }
            } else if (eventType == END_ELEMENT && reader.getLocalName().equals("enumeration")) {
                insertStatement.setInt(1, id);
                insertStatement.setString(2, nameFi);
                insertStatement.setString(3, nameSv);
                insertStatement.executeUpdate();
                count.incrementAndGet();
                return;
            }
        }
    }
}
