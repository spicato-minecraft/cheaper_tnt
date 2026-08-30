package useful_tnt;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsefulTnt implements ModInitializer {
	public static final String MOD_ID = "useful_tnt";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		UsefulTntConfig.load();
		UseItemCallback.EVENT.register(TrailDrop::tryUse);
		LOGGER.info("Cheaper TNT recipe is {}", UsefulTntConfig.isEnabled() ? "enabled" : "disabled");
		LOGGER.info("TNT item drop protection is {}", UsefulTntConfig.isDropProtectionEnabled() ? "enabled" : "disabled");
		LOGGER.info("TNT trail-drop is {} (fuse {} ticks)", UsefulTntConfig.isTrailDropEnabled() ? "enabled" : "disabled", UsefulTntConfig.getTrailDropFuseTicks());
	}
}
