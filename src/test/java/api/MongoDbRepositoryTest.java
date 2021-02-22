package api;

import api.db_mongodb.MongoDbRepository;
import model.Sample;
import model.SampleResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MongoDbRepositoryTest {

    static BasicCrud api;

    @BeforeAll
    public static void setUp() {
        api = new MongoDbRepository();
    }

    @Test
    @Order(101)
    void whenSampleInserted_thenInsertOk() {
        Sample testSample = new Sample(1.5, "2021-01-10", SampleResult.TRUE);
        String uid = testSample.getUID();
        api.insertSample(testSample);
        List<Sample> sampleList = api.listSamples(SampleResult.TRUE);
        assertEquals(1, sampleList.size());
        assertEquals(uid, sampleList.get(0).getUID());
    }

    @Test
    @Order(102)
    void whenSampleDeleted_thenDeleteOk() {
        List<Sample> sampleList = api.listSamples(SampleResult.TRUE);
        Sample toDelete = sampleList.get(0);
        api.deleteSample(toDelete);
        sampleList = api.listSamples(SampleResult.TRUE);
        assertEquals(0, sampleList.size());
    }
}