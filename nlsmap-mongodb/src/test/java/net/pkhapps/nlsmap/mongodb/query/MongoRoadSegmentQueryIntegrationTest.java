package net.pkhapps.nlsmap.mongodb.query;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import net.pkhapps.nlsmap.api.features.RoadSegment;
import net.pkhapps.nlsmap.mongodb.MongoConstants;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

/**
 * TODO Document me!
 */
public class MongoRoadSegmentQueryIntegrationTest {

    private static MongoClient CLIENT;
    private static MongoDatabase DB;
    private MongoRoadSegmentQuery roadSegmentQuery;

    @BeforeClass
    public static void setUpConnection() {
        CLIENT = new MongoClient();
        DB = CLIENT.getDatabase(MongoConstants.TERRAIN_DATABASE);
    }

    @AfterClass
    public static void tearDownConnection() {
        if (CLIENT != null) {
            CLIENT.close();
        }
    }

    @Before
    public void setUp() {
        roadSegmentQuery = new MongoRoadSegmentQuery(DB);
    }

    @Test
    public void foo() {
        List<RoadSegment> result = roadSegmentQuery.findByCriteria(roadSegmentQuery.newCriteriaBuilder().byName("Köpmansgatan").build());
        System.out.println(result);
    }

}
