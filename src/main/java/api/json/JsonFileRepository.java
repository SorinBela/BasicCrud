package api.json;

import api.BasicCrud;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Sample;
import model.SampleResult;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class JsonFileRepository implements BasicCrud {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JsonFileHandler fileHandler;

    public JsonFileRepository() {
        this.fileHandler = new JsonFileHandler(
                Paths.get(System.getProperty("user.home") + "/json-file-repository.json"));
    }

    @Override
    public List<Sample> listSamples() {
        return listSamples(SampleResult.ALL);
    }

    /**
     * This is the JSON based implementation of the Sample-API. And also the first
     * API-set to be developed.
     * <p>
     * This method will list all Samples found in the text file designated by
     * {@code filePath}. This API uses Jackson in order to wrap Java objects into
     * JSON strings and vice versa.
     *
     * @param resultType - lists all Samples with the result matching the
     *                   provided {@code SampleResult}. Further you can list
     *                   all samples by providing {@code SampleResult.ALL}.
     * @return An {@code java.lang.ArrayList} containing all matching Samples.
     */

    // [ST-05.12.19] * @throws FileNotFoundException if the provided {@code
    // filePath} doesn't yield a file.
    // ^- Since we would have to check every time we use these API calls that a file
    // is provided,
    // a better option seems to be to sanitize File/Path input one time, eg. via
    // Controller-part of an App
    // or provide one way for our API to do iot. However since we use static
    // methods, we would have to be sure
    // that we instantiate the API first and sanitize the File/Path once. Personally
    // and currently, I deem this
    // very context dependant, so I will omit this sanitization this time.
    @Override
    public List<Sample> listSamples(SampleResult resultType) {
        return this.loadSamples();
    }

    @Override
    public boolean insertSample(Sample sample) {
        if(sample == null) return false;

        List<Sample> list = this.listSamples();
        list.add(sample);
        try {
            // It is not always optimal to save the list with every insert.
            // Normally we would buffer the inserts and persist it at a designated point
            // depending on the Use-Case.
            this.fileHandler.save(this.objectMapper.writeValueAsString(list));
            return true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean insertSamples(List<Sample> samples) {
        List<Sample> sampleList = this.loadSamples();
        sampleList.addAll(samples);
        return this.saveSamples(sampleList);
    }

    private List<Sample> loadSamples() {
        String json = this.fileHandler.load();
        List<Sample> listOfSamples = Collections.emptyList();
        try {
            listOfSamples = this.objectMapper
                    .readValue(json, new TypeReference<List<Sample>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return listOfSamples;
    }

    private boolean saveSamples(List<Sample> samples) {
        try {
            this.fileHandler.save(this.objectMapper.writeValueAsString(samples));
            return true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateSample(Sample updatedSample) {
        boolean success = false;
        List<Sample> samples = this.loadSamples();
        // For this very basic example we assume that there can be no copies
        // in this list. However, a Set might be a better suited collection because
        // a Set doesn't allow copies in the first place.
        for(Sample s : samples) {
            if(s.getUID().equals(updatedSample.getUID())) {
                s.setSampleResult(updatedSample.getSampleResult());
                s.setSampleValue(updatedSample.getSampleValue());
                success = true;
                break; // b/c of our assumption about unique-ness.
            }
        }
        this.saveSamples(samples);
        return success;
    }

    @Override
    public boolean upsertSample(Sample sample) {
        return false;
    }

    @Override
    public boolean deleteSample(Sample sample) {
        List<Sample> samples = this.loadSamples();
        boolean success = samples.remove(sample);
        this.saveSamples(samples);
        return success;
    }

    @Override
    public Sample retrieve(String uid) {
        return null;
    }

    protected void close() {
        this.fileHandler.deleteJsonFile();
    }

}
