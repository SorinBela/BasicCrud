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

/**
 * This API implementation initiates a File {@code json-file-repository.json} in your home directory
 * and stores {@link Sample}s inside this file. This implementation uses Jackson Json.
 *
 * It uses a very basic implementation of java.nio to handle the repository file. This is due to
 * the fact that file handling is heavily influenced by both File System and Context. Both are
 * however not important for this basic CRUD api implementation.
 *
 * This API class handles the CRUD operations and uses {@link JsonFileHandler} for the I/O operations
 * on the repository file.
 *
 * @author s.totzauer
 */
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
        throw new UnsupportedOperationException("Upserting not implemented yet.");
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
        throw new UnsupportedOperationException("Upserting not implemented yet.");
    }

    protected void close() {
        this.fileHandler.deleteJsonFile();
    }

    /**
     * Asks the {@link JsonFileHandler} to load the Json-Repository-File and transforms
     * the String content into a List of Sample objects
     * @return A List of Samples from the Json Repository.
     */
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

    /**
     * Transforms the List of Sample objects into a JSON String and asks the {@link JsonFileHandler}
     * to write it to the file on disk.
     *
     * @param samples The samples you wish to persist
     * @return true, if the samples were persisted
     */
    private boolean saveSamples(List<Sample> samples) {
        try {
            this.fileHandler.save(this.objectMapper.writeValueAsString(samples));
            return true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
