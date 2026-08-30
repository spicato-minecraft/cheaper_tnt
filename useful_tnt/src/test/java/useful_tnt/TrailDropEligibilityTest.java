package useful_tnt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrailDropEligibilityTest {

	@Test
	void flintOffhandWithTntIsEligibleWhenEnabled() {
		assertTrue(TrailDropEligibility.isEligible(true, true, true, false));
	}

	@Test
	void fireChargeOffhandWithTntIsEligibleWhenEnabled() {
		assertTrue(TrailDropEligibility.isEligible(true, true, false, true));
	}

	@Test
	void disabledConfigRejectsValidLoadout() {
		assertFalse(TrailDropEligibility.isEligible(false, true, true, false));
		assertFalse(TrailDropEligibility.isEligible(false, true, false, true));
	}

	@Test
	void nonTntMainHandIsNotEligible() {
		assertFalse(TrailDropEligibility.isEligible(true, false, true, false));
	}

	@Test
	void emptyOrUnrelatedOffhandIsNotEligible() {
		assertFalse(TrailDropEligibility.isEligible(true, true, false, false));
	}

	@Test
	void bothIgnitersStillEligible() {
		assertTrue(TrailDropEligibility.isEligible(true, true, true, true));
	}
}
