package useful_tnt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TntDropProtectionTest {

	@Test
	void protectsItemFromTntExplosionWhenEnabled() {
		assertTrue(TntDropProtection.shouldProtect(true, true, true, true));
	}

	@Test
	void disabledToggleRestoresCull() {
		assertFalse(TntDropProtection.shouldProtect(false, true, true, true));
	}

	@Test
	void doesNotProtectMobs() {
		assertFalse(TntDropProtection.shouldProtect(true, false, true, true));
	}

	@Test
	void doesNotProtectNonExplosionDamage() {
		assertFalse(TntDropProtection.shouldProtect(true, true, false, true));
	}

	@Test
	void doesNotProtectNonTntExplosions() {
		assertFalse(TntDropProtection.shouldProtect(true, true, true, false));
	}
}
