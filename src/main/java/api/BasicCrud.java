package api;

import model.Sample;
import model.SampleResult;

import java.util.List;

public interface BasicCrud {

    /**
     * Lists all samples from the repository that match the given resultType from {@link SampleResult}
     *
     * @param resultType acts as a filter criteria
     * @return a list of all samples with the designated {@link SampleResult}
     */
    List<Sample> listSamples(SampleResult resultType);

    /**
     * Lists all samples regardless of their result type
     * @return a list of all samples in the repository
     */
    List<Sample> listSamples();

    /**
     * Inserts a {@link Sample} object into the repository
     * @param sample The Sample you wish to add
     * @return true, if the Sample was added.
     */
    boolean insertSample(Sample sample);

    /**
     * Inserts a List of {@link Sample}s into the repository.
     * @param samples The {@link List} you wish to add.
     * @return true, if all Samples were added.
     */
    boolean insertSamples(List<Sample> samples);

    /**
     * updates the Sample with the given updatedSample if they
     * match their UUID.
     *
     * @param updatedSample The new copy you wish to replace the original with
     * @return true, if the Sample was updated
     */
    boolean updateSample(Sample updatedSample);

    /**
     * If the given Sample exists, it will be updated otherwise it will be inserted.
     *
     * @param sample The given Sample you wish to update/insert.
     * @return true, if a write-operation happened
     */
    boolean upsertSample(Sample sample);

    /**
     * Deletes a sample from the repository
     * @param sample the Sample you wish to delete
     * @return true, if the Sample existed and was deleted
     */
    boolean deleteSample(Sample sample);

    /**
     * Retrieves a Sample by its UUID.
     *
     * @param uid The UUID of a sample
     * @return The Sample with matching UUID -or- {@code null}, if no such Sample exists.
     */
    Sample retrieve(String uid);

}
