package api;

import model.Sample;
import model.SampleResult;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryRepository implements BasicCrud {

	private Map<String, Sample> sampleMap = new HashMap<>();

	@Override
	public List<Sample> listSamples() {
		return new ArrayList<>(sampleMap.values());
	}

	@Override
	public List<Sample> listSamples(SampleResult resultType) {
		return sampleMap.values().stream()
				.filter(sampleInList -> sampleInList.getSampleResult() == resultType)
				.collect(Collectors.toList());
	}

	/**
	 * Inserts a Sample into the Map if the key is not yet in the map.
	 * 
	 * @param sample - the Sample you wish to insert.
	 * @return true, if the Sample was added, else false.
	 */
	@Override
	public boolean insertSample(Sample sample) {
		if (sample == null) return false;
		if (sample.getSampleResult() == SampleResult.ALL) return false;

		if (!sampleMap.containsKey(sample.getUID())) {
			sampleMap.put(sample.getUID(), sample);
			return true;
		}

		return false;
	}

	@Override
	public boolean insertSamples(List<Sample> samples) {
		boolean insertedAll = true;
		for(Sample sample : samples) {
			if(!this.insertSample(sample)) {
				insertedAll = false;
			}
		}
		return insertedAll;
	}

	@Override
	public boolean updateSample(Sample updatedSample) {
		if(!this.sampleMap.containsKey(updatedSample.getUID())) return false;
		this.sampleMap.put(updatedSample.getUID(), updatedSample);
		return true;
	}

	@Override
	public boolean upsertSample(Sample sample) {
		this.sampleMap.put(sample.getUID(), sample);
		return true;
	}

	@Override
	public boolean deleteSample(Sample sample) {
		return (null != this.sampleMap.remove(sample.getUID()));
	}

	@Override
	public Sample retrieve(String uid) {
		return this.sampleMap.get(uid);
	}

}
