package api.json;

import api.BasicCrud;
import model.Sample;
import model.SampleResult;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is the class testing the Sample JSON API methods.
 * @author Soeren_T
 *
 */
@TestMethodOrder(OrderAnnotation.class)
class JsonFileRepositoryTest {

	private static BasicCrud api;

	@BeforeAll
	private static void setUp() {
		api = new JsonFileRepository();
	}

	@Test
	@Order(101)
	void whenInsertSample_thenJsonFileIsWritten() {
		Sample testSample = new Sample(1.2, "today", SampleResult.TRUE);
		assertTrue(api.insertSample(testSample));
	}

	@Test
	@Order(102)
	void whenLoadingJsonRepository_thenListAndSampleCreatedCorrectly() {
		List<Sample> listFromFile = api.listSamples();
		assertEquals(1, listFromFile.size());
		Sample sampleFromFile = listFromFile.get(0);
		assertEquals(1.2, sampleFromFile.getSampleValue());
		assertEquals("today", sampleFromFile.getSampleDate());
		assertEquals(SampleResult.TRUE, sampleFromFile.getSampleResult());
	}

	@Test
	@Order(103)
	void whenUpdatingSample_thenFileIsStored() {
		List<Sample> listFromFile = api.listSamples();
		Sample sample = listFromFile.get(0);
		assertEquals(SampleResult.TRUE, sample.getSampleResult());
		sample.setSampleResult(SampleResult.FALSE);
		api.updateSample(sample);

		listFromFile = api.listSamples();
		assertEquals(1, listFromFile.size());
		assertEquals(SampleResult.FALSE, listFromFile.get(0).getSampleResult());
	}

	@Test
	@Order(104)
	void whenDeletingFromRepository_thenSampleIsDeleted() {
		List<Sample> listFromFile = api.listSamples();
		Sample sample = listFromFile.get(0);
		api.deleteSample(sample);
		listFromFile = api.listSamples();
		assertEquals(0, listFromFile.size());
	}

	@AfterAll
	private static void deleteTestFile() {
		JsonFileRepository repository = (JsonFileRepository) api;
		repository.close();
	}

}
