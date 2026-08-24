package useful_tnt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class UsefulTntConfig {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	public static final String CONFIG_FILE = "useful_tnt.json";
	public static final String LEGACY_CONFIG_FILE = "cheaper_tnt.json";

	private static boolean cheaperTntEnabled = true;
	private static boolean dropProtectionEnabled = true;

	public static Path defaultConfigDir() {
		return FabricLoader.getInstance().getConfigDir();
	}

	public static Path defaultConfigPath() {
		return defaultConfigDir().resolve(CONFIG_FILE);
	}

	public static void load() {
		loadFromConfigDir(defaultConfigDir());
	}

	/**
	 * Loads {@code useful_tnt.json} if present. Otherwise migrates
	 * {@code cheaper_tnt.json} and writes the new file. If neither exists,
	 * writes defaults to {@code useful_tnt.json}.
	 */
	public static void loadFromConfigDir(Path configDir) {
		Path current = configDir.resolve(CONFIG_FILE);
		Path legacy = configDir.resolve(LEGACY_CONFIG_FILE);
		try {
			if (Files.exists(current)) {
				applyFile(current);
				return;
			}
			if (Files.exists(legacy)) {
				applyFile(legacy);
				saveTo(current);
				UsefulTnt.LOGGER.info("Migrated config {} -> {}", legacy.getFileName(), current.getFileName());
				return;
			}
			cheaperTntEnabled = true;
			dropProtectionEnabled = true;
			saveTo(current);
		} catch (IOException e) {
			UsefulTnt.LOGGER.warn("Failed to load config, using defaults: {}", e.getMessage());
		}
	}

	public static void save() {
		saveTo(defaultConfigPath());
	}

	public static void saveTo(Path configPath) {
		try {
			Files.createDirectories(configPath.getParent());
			ConfigData data = new ConfigData(cheaperTntEnabled, dropProtectionEnabled);
			Files.writeString(configPath, GSON.toJson(data));
		} catch (IOException e) {
			UsefulTnt.LOGGER.error("Failed to save config: {}", e.getMessage());
		}
	}

	private static void applyFile(Path configPath) throws IOException {
		String content = Files.readString(configPath);
		ConfigData data = GSON.fromJson(content, ConfigData.class);
		if (data != null) {
			cheaperTntEnabled = data.cheaperTntEnabled;
			dropProtectionEnabled = data.dropProtectionEnabled;
		}
	}

	public static boolean isEnabled() {
		return cheaperTntEnabled;
	}

	public static boolean isDropProtectionEnabled() {
		return dropProtectionEnabled;
	}

	/** Test hook. Does not persist. */
	public static void setDropProtectionEnabled(boolean enabled) {
		dropProtectionEnabled = enabled;
	}

	/** Test hook. Does not persist. */
	public static void setCheaperTntEnabled(boolean enabled) {
		cheaperTntEnabled = enabled;
	}

	private static class ConfigData {
		boolean cheaperTntEnabled = true;
		boolean dropProtectionEnabled = true;

		ConfigData() {}

		ConfigData(boolean cheaperTntEnabled, boolean dropProtectionEnabled) {
			this.cheaperTntEnabled = cheaperTntEnabled;
			this.dropProtectionEnabled = dropProtectionEnabled;
		}
	}
}
