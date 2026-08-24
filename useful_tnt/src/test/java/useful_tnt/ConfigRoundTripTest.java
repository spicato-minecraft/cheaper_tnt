package useful_tnt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
		assertTrue(UsefulTntConfig.isEnabled());
		assertTrue(UsefulTntConfig.isDropProtectionEnabled());
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
		assertFalse(UsefulTntConfig.isEnabled());
		assertFalse(UsefulTntConfig.isDropProtectionEnabled());

		UsefulTntConfig.setCheaperTntEnabled(true);
		UsefulTntConfig.setDropProtectionEnabled(true);
		UsefulTntConfig.loadFromConfigDir(tempDir);
		assertFalse(UsefulTntConfig.isEnabled(), "Existing useful_tnt.json wins over leftover legacy file");
		assertFalse(UsefulTntConfig.isDropProtectionEnabled());
	}
}
