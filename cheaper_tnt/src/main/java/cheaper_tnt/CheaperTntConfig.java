package cheaper_tnt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CheaperTntConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_FILE = "cheaper_tnt.json";

    private static boolean cheaperTntEnabled = true;

    public static void load() {
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve(CONFIG_FILE);

        try {
            if (Files.exists(configPath)) {
                String content = Files.readString(configPath);
                ConfigData data = GSON.fromJson(content, ConfigData.class);
                if (data != null) {
                    cheaperTntEnabled = data.cheaperTntEnabled;
                }
            } else {
                save();
            }
        } catch (IOException e) {
            Cheaper_tnt.LOGGER.warn("Failed to load config, using defaults: {}", e.getMessage());
        }
    }

    public static void save() {
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve(CONFIG_FILE);

        try {
            Files.createDirectories(configPath.getParent());
            ConfigData data = new ConfigData(cheaperTntEnabled);
            Files.writeString(configPath, GSON.toJson(data));
        } catch (IOException e) {
            Cheaper_tnt.LOGGER.error("Failed to save config: {}", e.getMessage());
        }
    }

    public static boolean isEnabled() {
        return cheaperTntEnabled;
    }

    private static class ConfigData {
        boolean cheaperTntEnabled = true;

        ConfigData() {}

        ConfigData(boolean cheaperTntEnabled) {
            this.cheaperTntEnabled = cheaperTntEnabled;
        }
    }
}
