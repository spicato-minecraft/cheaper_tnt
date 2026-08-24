package useful_tnt;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsefulTnt implements ModInitializer {
	public static final String MOD_ID = "useful_tnt";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		UsefulTntConfig.load();
		LOGGER.info("Cheaper TNT recipe is {}", UsefulTntConfig.isEnabled() ? "enabled" : "disabled");
		LOGGER.info("TNT item drop protection is {}", UsefulTntConfig.isDropProtectionEnabled() ? "enabled" : "disabled");
	}
}