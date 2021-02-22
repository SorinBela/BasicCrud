package api.db_mongodb;

import model.Sample;
import model.SampleResult;
import org.bson.Document;

public class MongoDbSample extends Sample {

    public static final String P_ID = "_id";
    public static final String P_VALUE = "sampleValue";
    public static final String P_RESULT = "sampleResult";
    public static final String P_MEASUREMENT_DATE = "sampleDate";

    public static Sample documentToSample(Document document) {
        String id = document.getString(P_ID);
        Double value = document.getDouble(P_VALUE);
        String date = document.getString(P_MEASUREMENT_DATE);
        SampleResult result = SampleResult.valueOf(document.getString(P_RESULT));
        return new Sample(id, value, date, result);
    }

    public static Document sampleToDocument(Sample sample) {
        Document document = new Document();
        document.put(P_ID, sample.getUID());
        document.put(P_VALUE, sample.getSampleValue());
        document.put(P_RESULT, sample.getSampleResult().name());
        document.put(P_MEASUREMENT_DATE, sample.getSampleDate());
        return document;
    }
}
