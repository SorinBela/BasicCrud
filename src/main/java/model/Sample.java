package model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * This class represents a Sample taken from some abstract process. A Sample is constituted 
 * by its UID, its measurement value, the date the Sample was obtained and a result which can be
 * TRUE, FALSE or UNCERTAIN.
 * 
 *  @author Soeren_T
 */
public class Sample implements Serializable, Comparable< Sample > {

	/**
	 * This is necessary for Jackson JSON.
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("uid")
	private String sampleUID;
	private double sampleValue;
	private String sampleDate;
	private SampleResult sampleResult;

	/** 
	 * Standard Constructor, needed by JSON.
	 */
	public Sample() {}
	
	/**
	 * 
	 * Constructor for a sample. Unique ID provided by Java {@code UUID} interface.
	 * 
	 * @param sampleValue - the measured value of a sample, expected to be a double value.
	 * @param sampleDate - the date on which a sample was obtained. Due to an illegal reflective access 
	 * by JSON when using ZonedDateTime, we will simply store the Date as a String. I deem this not optimal because
	 * we will thus outsource the validation of dates.
	 * @param sampleResult - the result of a sample, values provided by {@code SampleResult}.
	 */
	public Sample(double sampleValue, String sampleDate, SampleResult sampleResult) {
		this.sampleUID = UUID.randomUUID().toString();
		this.sampleValue = sampleValue;
		this.sampleDate = sampleDate;
		this.sampleResult = sampleResult;
	}
	
	/**
	 * This constructor constructs a sample with a given UID. In fact, this no longer guarantees the UID to be
	 * unique. However, this constructor is needed for reconstructing Sample objects with their UID from the database.
	 * @param uid - the UID you should retrieve from a database.
	 * @param sampleValue - the value of a sample you should retrieve from a database.
	 * @param sampleDate - the date on which a sample was obtained, which you should retrieve from a database.
	 * @param sampleResult - the result of a Sample which you should retrieve from a database.
	 */
	public Sample(String uid, double sampleValue, String sampleDate, SampleResult sampleResult) {
		this.sampleUID = uid;
		this.sampleValue = sampleValue;
		this.sampleDate = sampleDate;
		this.sampleResult = sampleResult;
	}
	
	// Comments for Standard-Getters and Setters omitted for brevity and clarity.
	
	@JsonProperty("uid")
	public String getUID() {
		return this.sampleUID;
	}

	public double getSampleValue() {
		return this.sampleValue;
	}
	
	public String getSampleDate() {
		return this.sampleDate;
	}

	public SampleResult getSampleResult() {
		return sampleResult;
	}

	public void setSampleValue(double val) {
		this.sampleValue = val;
	}
	
	public void setSampleResult(SampleResult result) {
		if(result == SampleResult.ALL) return;
		this.sampleResult = result;
	}
	
	public String toString() {
		return "Sample UID: " + this.getUID() + " "
				+"Value: " + this.getSampleValue() + " "
				+"Obtained on: " + this.getSampleDate() + " "
				+"Result: " + this.getSampleResult();
	}
	
	/**
	 * Prints the fields of the Sample object in a nicer manner.
	 * @return a prettified String.
	 */
	public String toPrettyString() {
		return "Sample UID: " + this.getUID() + System.lineSeparator() + "\t"
				+"Value: " + this.getSampleValue() + System.lineSeparator() + "\t"
				+"Obtained on: " + this.getSampleDate() + System.lineSeparator() + "\t"
				+"Result: " + this.getSampleResult() + System.lineSeparator();
	}
	
	public String toPrettyDataBaseString() {
		return this.getUID() + "\t\t"
				+ this.sampleValue + "\t\t"
				+ this.sampleResult + "\t\t"
				+ this.getSampleDate() + System.lineSeparator();
	}

	/**
	 * This method is mainly used for sorting {@code java.util.List}s containing Sample objects,
	 * ordering them according to their String-representation of the date. Mandatory for using Collections.sort();
	 */
	@Override
	public int compareTo(Sample comparedToSample) {
		return this.getSampleDate().compareTo(comparedToSample.getSampleDate());
	}
	
	/**
	 * This modified equals-method is needed for ordering Samples mainly by Collections.sort();
	 */
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null || obj.getClass() != this.getClass()) return false;
		
		Sample comparedSample = (Sample) obj;
		
		return (
					this.sampleUID.equals(comparedSample.getUID())
				&&	this.sampleDate.equals(comparedSample.getSampleDate())
				&&	this.sampleResult.equals(comparedSample.getSampleResult())
				&&	this.sampleValue == comparedSample.getSampleValue()
			);
	}

	@Override
	public int hashCode() {
		return Objects.hash(sampleUID, sampleValue, sampleDate, sampleResult);
	}
}
