import api.BasicCrud;
import api.InMemoryRepository;
import model.Sample;
import model.SampleResult;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@TestMethodOrder(OrderAnnotation.class)
class InMemoryRepositoryTest {

	private final BasicCrud api = new InMemoryRepository();

	/* Insert Method Tests */
	@Test
	@Order(101)
	void testInsertSample() {
		Sample testSample = spy(new Sample());
		doReturn("1").when(testSample).getUID();
		testSample.setSampleResult(SampleResult.TRUE);
		testSample.setSampleValue(2.1);
		assertTrue(this.api.insertSample(testSample)); // Add it once
		assertFalse(this.api.insertSample(testSample)); // Don't add it twice
	}

	@Test
	@Order(102)
	void testInsertSampleList() {
		this.api.insertSamples(List.of(
				new Sample(1.2, "2021-02-02", SampleResult.TRUE),
				new Sample(7.2, "2021-02-02", SampleResult.FALSE),
				new Sample(0.2, "2021-02-02", SampleResult.TRUE)
		));
		assertEquals(3, this.api.listSamples().size());
	}

	@Test
	@Order(103)
	void whenInsertSampleListWithDuplicates_thenDuplicatesAreSkipped() {
		Sample duplicate = new Sample(2.2, "2021-02-22", SampleResult.TRUE);
		assertFalse(this.api.insertSamples(
		List.of(
				new Sample(1.2, "2021-02-02", SampleResult.TRUE),
				new Sample(7.2, "2021-02-02", SampleResult.FALSE),
				duplicate,
				duplicate,
				new Sample(0.2, "2021-02-02", SampleResult.TRUE)
		)));
		assertEquals(4, this.api.listSamples().size());
	}
	
	/*
	 * Update Method Tests
	 */
	@Test
	@Order(200)
	void testUpdateSample() {

		Sample testSample = spy(new Sample());
		doReturn("1").when(testSample).getUID();
		testSample.setSampleResult(SampleResult.TRUE);
		testSample.setSampleValue(1.2);

		this.api.insertSample(testSample);

		testSample.setSampleResult(SampleResult.FALSE);
		testSample.setSampleValue(3.2);

		assertTrue(this.api.updateSample(testSample));

		Sample sampleFromPersistence = this.api.retrieve("1");
		assertEquals(SampleResult.FALSE, sampleFromPersistence.getSampleResult());
		assertEquals(3.2, sampleFromPersistence.getSampleValue());
	}
	
	@Test
	@Order(201)
	void whenProvidingFalseUID_thenUpdateReturnsFalse() {
		Sample testSample = spy(new Sample());
		doReturn("Does not exist").when(testSample).getUID();
		assertFalse(this.api.updateSample(testSample));
	}
	
	@Test
	@Order(202)
	void whenProvidingNullID_thenUpdateReturnsFalse() {
		Sample testSample = spy(new Sample());
		doReturn(null).when(testSample).getUID();
		assertFalse(this.api.updateSample(testSample));
	}
	
	/*
	 * Delete Method Tests
	 */
	@Test
	@Order(300)
	void testDeleteSample() {

		Sample testSample = spy(new Sample());
		doReturn("4").when(testSample).getUID();
		testSample.setSampleResult(SampleResult.FALSE);
		testSample.setSampleValue(1.0);

		assertTrue(this.api.insertSample(testSample));
		assertNotNull(this.api.retrieve("4"));
		assertTrue(this.api.deleteSample(testSample));
		assertNull(this.api.retrieve("4"));
	}
	
	/*
	 * List Method Tests
	 */
	@Test
	@Order(400)
	void testListSamples() {
		Sample testSample = spy(new Sample());
		doReturn("4").when(testSample).getUID();
		testSample.setSampleResult(SampleResult.FALSE);
		testSample.setSampleValue(1.0);

		this.api.insertSample(testSample);
		assertEquals(1, this.api.listSamples().size());
	}

}
