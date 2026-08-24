package useful_tnt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ModLoadsUnitTest {

	@Test
	void modIdIsUsefulTnt() {
		assertNotNull(UsefulTnt.MOD_ID);
		assertEquals("useful_tnt", UsefulTnt.MOD_ID);
	}
}
