package useful_tnt;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.vehicle.minecart.MinecartTNT;

/**
 * Item-entity cull skip for TNT-sourced explosions only.
 * Mob and block-entity behavior stays vanilla.
 */
public final class TntDropProtection {

	private TntDropProtection() {
	}

	public static boolean shouldProtect(boolean dropProtectionEnabled, boolean isItemEntity, boolean isExplosion, boolean isTntSource) {
		return dropProtectionEnabled && isItemEntity && isExplosion && isTntSource;
	}

	public static boolean shouldProtectDroppedItem(Entity entity, DamageSource source) {
		return shouldProtect(
				UsefulTntConfig.isDropProtectionEnabled(),
				entity instanceof ItemEntity,
				source.is(DamageTypeTags.IS_EXPLOSION),
				isTntExplosionSource(source.getDirectEntity()) || isTntExplosionSource(source.getEntity())
		);
	}

	public static boolean isTntExplosionSource(Entity entity) {
		return entity instanceof PrimedTnt || entity instanceof MinecartTNT;
	}
}
