import org.junit.Test;

import junit.framework.TestCase;
import project.TrainModel;

public class TrainModelUnitTest extends TestCase {
	TrainModel train;

	public TrainModelUnitTest(String name) {
		super(name);
		train = new TrainModel();
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@Test
	public void testSetAuthority() {
		train.setAuthority(100);
		assertEquals(100.0, train.getAuthority());
	}

	@Test
	public void testSetPower() {
		train.setPower(55);
		assertEquals(55.0, train.getPower());
	}

//	@Test
//	public void testCalculateLength() {
//		TrainModel.calculateLength();
//		assertEquals(1, train.getLength());
//	}

}
