package api;

import model.Sample;
import model.SampleResult;

import java.util.List;

public interface BasicCrud {

    List<Sample> listSamples(SampleResult resultType);

    List<Sample> listSamples();

    boolean insertSample(Sample sample);

    boolean insertSamples(List<Sample> samples);

    boolean updateSample(Sample updatedSample);

    boolean upsertSample(Sample sample);

    boolean deleteSample(Sample sample);

    Sample retrieve(String uid);

}
