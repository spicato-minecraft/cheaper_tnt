package useful_tnt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigRoundTripTest {

	@Test
	void missingFileWritesUsefulTntDefaults(@TempDir Path tempDir) throws IOException {
		Path current = tempDir.resolve(UsefulTntConfig.CONFIG_FILE);
		assertFalse(Files.exists(current));

		UsefulTntConfig.loadFromConfigDir(tempDir);

		assertTrue(Files.exists(current), "Missing config should be created at useful_tnt.json");
		assertFalse(Files.exists(tempDir.resolve(UsefulTntConfig.LEGACY_CONFIG_FILE)));
		String json = Files.readString(current);
		assertTrue(json.contains("\"cheaperTntEnabled\": true"));
		assertTrue(json.contains("\"dropProtectionEnabled\": true"));
		assertTrue(json.contains("\"trailDropEnabled\": true"));
		assertTrue(json.contains("\"trailDropFuseTicks\": 100"));
		assertTrue(UsefulTntConfig.isEnabled());
		assertTrue(UsefulTntConfig.isDropProtectionEnabled());
		assertTrue(UsefulTntConfig.isTrailDropEnabled());
		assertEquals(100, UsefulTntConfig.getTrailDropFuseTicks());
	}

	@Test
	void legacyCheaperTntFileMigratesBothToggles(@TempDir Path tempDir) throws IOException {
		Path legacy = tempDir.resolve(UsefulTntConfig.LEGACY_CONFIG_FILE);
		Path current = tempDir.resolve(UsefulTntConfig.CONFIG_FILE);
		Files.writeString(legacy, """
				{
				  "cheaperTntEnabled": false,
				  "dropProtectionEnabled": false
				}
				""");

		UsefulTntConfig.loadFromConfigDir(tempDir);

		assertTrue(Files.exists(current), "Legacy cheaper_tnt.json should migrate to useful_tnt.json");
		assertTrue(Files.exists(legacy), "Legacy file is left in place; new file is used going forward");
		String json = Files.readString(current);
		assertTrue(json.contains("\"cheaperTntEnabled\": false"));
		assertTrue(json.contains("\"dropProtectionEnabled\": false"));
		assertTrue(json.contains("\"trailDropEnabled\": true"));
		assertTrue(json.contains("\"trailDropFuseTicks\": 100"));
		assertFalse(UsefulTntConfig.isEnabled());
		assertFalse(UsefulTntConfig.isDropProtectionEnabled());
		assertTrue(UsefulTntConfig.isTrailDropEnabled(), "Legacy files never had trail-drop keys; they default on");
		assertEquals(100, UsefulTntConfig.getTrailDropFuseTicks());

		UsefulTntConfig.setCheaperTntEnabled(true);
		UsefulTntConfig.setDropProtectionEnabled(true);
		UsefulTntConfig.setTrailDropEnabled(false);
		UsefulTntConfig.setTrailDropFuseTicks(1);
		UsefulTntConfig.loadFromConfigDir(tempDir);
		assertFalse(UsefulTntConfig.isEnabled(), "Existing useful_tnt.json wins over leftover legacy file");
		assertFalse(UsefulTntConfig.isDropProtectionEnabled());
		assertTrue(UsefulTntConfig.isTrailDropEnabled());
		assertEquals(100, UsefulTntConfig.getTrailDropFuseTicks());
	}

	@Test
	void missingTrailDropKeysKeepDefaults(@TempDir Path tempDir) throws IOException {
		Path current = tempDir.resolve(UsefulTntConfig.CONFIG_FILE);
		Files.writeString(current, """
				{
				  "cheaperTntEnabled": false,
				  "dropProtectionEnabled": false
				}
				""");

		UsefulTntConfig.setTrailDropEnabled(false);
		UsefulTntConfig.setTrailDropFuseTicks(1);
		UsefulTntConfig.loadFromConfigDir(tempDir);

		assertFalse(UsefulTntConfig.isEnabled());
		assertFalse(UsefulTntConfig.isDropProtectionEnabled());
		assertTrue(UsefulTntConfig.isTrailDropEnabled());
		assertEquals(100, UsefulTntConfig.getTrailDropFuseTicks());
	}

	@Test
	void explicitTrailDropKeysRoundTrip(@TempDir Path tempDir) throws IOException {
		Path current = tempDir.resolve(UsefulTntConfig.CONFIG_FILE);
		Files.writeString(current, """
				{
				  "cheaperTntEnabled": true,
				  "dropProtectionEnabled": true,
				  "trailDropEnabled": false,
				  "trailDropFuseTicks": 120
				}
				""");

		UsefulTntConfig.loadFromConfigDir(tempDir);

		assertFalse(UsefulTntConfig.isTrailDropEnabled());
		assertEquals(120, UsefulTntConfig.getTrailDropFuseTicks());

		UsefulTntConfig.saveTo(current);
		String json = Files.readString(current);
		assertTrue(json.contains("\"trailDropEnabled\": false"));
		assertTrue(json.contains("\"trailDropFuseTicks\": 120"));
	}
}
