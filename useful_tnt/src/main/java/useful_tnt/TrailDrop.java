package useful_tnt;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Air-use trail-drop: TNT main + flint/fire-charge offhand, hit miss, spawn primed TNT at feet.
 */
public final class TrailDrop {

	private static final Map<UUID, Long> LAST_USE_GAME_TIME = new ConcurrentHashMap<>();

	private TrailDrop() {
	}

	public static InteractionResult tryUse(Player player, Level level, InteractionHand hand) {
		if (hand != InteractionHand.MAIN_HAND) {
			return InteractionResult.PASS;
		}
		if (player.isSpectator()) {
			return InteractionResult.PASS;
		}

		ItemStack main = player.getMainHandItem();
		ItemStack offhand = player.getOffhandItem();
		boolean eligible = TrailDropEligibility.isEligible(
				UsefulTntConfig.isTrailDropEnabled(),
				main.is(Items.TNT),
				offhand.is(Items.FLINT_AND_STEEL),
				offhand.is(Items.FIRE_CHARGE)
		);
		if (!eligible) {
			return InteractionResult.PASS;
		}
		if (player.pick(player.blockInteractionRange(), 1.0F, false).getType() != HitResult.Type.MISS) {
			return InteractionResult.PASS;
		}

		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		}

		long now = level.getGameTime();
		Long last = LAST_USE_GAME_TIME.get(player.getUUID());
		if (last != null && now - last <= 1) {
			LAST_USE_GAME_TIME.put(player.getUUID(), now);
			return InteractionResult.PASS;
		}

		BlockPos feet = player.blockPosition();
		if (!TntIgnition.prime(level, feet, player)) {
			return InteractionResult.PASS;
		}

		LAST_USE_GAME_TIME.put(player.getUUID(), now);
		PrimedTnt dropped = findNewestPrimedTnt(level, feet);
		if (dropped != null) {
			dropped.setFuse(UsefulTntConfig.getTrailDropFuseTicks());
			dropped.setDeltaMovement(player.getRootVehicle().getDeltaMovement());
		}
		if (!isCreative(player)) {
			TntIgnition.applyIgniterCost(offhand, player, InteractionHand.OFF_HAND);
			main.shrink(1);
		}
		return InteractionResult.SUCCESS;
	}

	private static boolean isCreative(Player player) {
		return player.hasInfiniteMaterials() || player.gameMode() == GameType.CREATIVE;
	}

	public static void applyFuse(Level level, BlockPos pos, int fuseTicks) {
		PrimedTnt newest = findNewestPrimedTnt(level, pos);
		if (newest != null) {
			newest.setFuse(fuseTicks);
		}
	}

	static PrimedTnt findNewestPrimedTnt(Level level, BlockPos pos) {
		AABB box = new AABB(pos).inflate(0.75);
		PrimedTnt newest = null;
		for (PrimedTnt tnt : level.getEntitiesOfClass(PrimedTnt.class, box)) {
			if (newest == null || tnt.tickCount <= newest.tickCount) {
				newest = tnt;
			}
		}
		return newest;
	}

	/** Test hook. */
	public static void clearHoldTracking() {
		LAST_USE_GAME_TIME.clear();
	}
}
