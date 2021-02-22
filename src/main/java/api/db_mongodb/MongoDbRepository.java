package api.db_mongodb;

import api.BasicCrud;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;
import com.mongodb.client.model.Filters;
import model.Sample;
import model.SampleResult;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MongoDbRepository implements BasicCrud {

    private static final String COLLECTION = "samples";

    MongoClient mongoClient = new MongoClient("localhost", 27017);
    MongoDatabase dataBase = mongoClient.getDatabase("BasicCrudDb");

    public MongoDbRepository() {
        boolean found = false;
        for (String dbName : dataBase.listCollectionNames()) {
            if (COLLECTION.equals(dbName)) {
                found = true;
                break;
            }
        }
        if(!found)
            dataBase.createCollection(COLLECTION);
    }

    @Override
    public List<Sample> listSamples(SampleResult resultType) {
        MongoCollection<Document> collection = dataBase.getCollection(COLLECTION);
        List<Sample> sampleList = new ArrayList<>();
        FindIterable<Document> result = collection.find(Filters.eq(MongoDbSample.P_RESULT, resultType.name()));
        try (MongoCursor<Document> cursor = result.iterator()) {
            while(cursor.hasNext()) {
                Sample sampleFromDocument = MongoDbSample.documentToSample(cursor.next());
                sampleList.add(sampleFromDocument);
            }
        }
        return sampleList;
    }

    @Override
    public List<Sample> listSamples() {
        return this.listSamples(SampleResult.ALL);
    }

    @Override
    public boolean insertSample(Sample sample) {
        MongoCollection<Document> collection = dataBase.getCollection(COLLECTION);
        Document document = MongoDbSample.sampleToDocument(sample);
        collection.insertOne(document);
        return true;
    }

    @Override
    public boolean insertSamples(List<Sample> samples) {
        boolean success = true;
        for (Sample sample : samples) {
            if (!this.insertSample(sample))
                success = false;
        }
        return success;
    }

    @Override
    public boolean updateSample(Sample updatedSample) {
        return this.insertSample(updatedSample);
    }

    @Override
    public boolean upsertSample(Sample sample) {
        return this.insertSample(sample);
    }

    @Override
    public boolean deleteSample(Sample sample) {
        MongoCollection<Document> collection = dataBase.getCollection(COLLECTION);
        collection.findOneAndDelete(MongoDbSample.sampleToDocument(sample));
        return true;
    }

    @Override
    public Sample retrieve(String uid) {
        return null;
    }
}
