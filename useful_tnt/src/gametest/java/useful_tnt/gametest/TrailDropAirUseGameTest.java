package useful_tnt.gametest;

import useful_tnt.TrailDrop;
import useful_tnt.UsefulTntConfig;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class TrailDropAirUseGameTest {

	@GameTest
	public void flintAirUseDropsPrimedTntAtFeetWithFuse100(GameTestHelper context) {
		TrailDrop.clearHoldTracking();
		Player player = trailPlayer(context, GameType.SURVIVAL, new ItemStack(Items.TNT, 4), new ItemStack(Items.FLINT_AND_STEEL));

		TntGameTestHelper.assertEqual(context, InteractionResult.SUCCESS, TrailDrop.tryUse(player, context.getLevel(), InteractionHand.MAIN_HAND), "trail-drop should succeed");
		assertOnePrimedAtFeet(context, player, 100);
		TntGameTestHelper.assertEqual(context, 3, player.getMainHandItem().getCount(), "survival consumes 1 TNT");
		TntGameTestHelper.assertEqual(context, 1, player.getOffhandItem().getDamageValue(), "flint takes 1 durability");
		context.succeed();
	}

	@GameTest
	public void fireChargeAirUseConsumesCharge(GameTestHelper context) {
		TrailDrop.clearHoldTracking();
		Player player = trailPlayer(context, GameType.SURVIVAL, new ItemStack(Items.TNT, 2), new ItemStack(Items.FIRE_CHARGE, 3));

		TrailDrop.tryUse(player, context.getLevel(), InteractionHand.MAIN_HAND);
		assertOnePrimedAtFeet(context, player, 100);
		TntGameTestHelper.assertEqual(context, 2, player.getOffhandItem().getCount(), "one fire charge consumed");
		context.succeed();
	}

	@GameTest
	public void heldUseDoesNotRepeatDrop(GameTestHelper context) {
		TrailDrop.clearHoldTracking();
		Player player = trailPlayer(context, GameType.SURVIVAL, new ItemStack(Items.TNT, 8), new ItemStack(Items.FLINT_AND_STEEL));

		TrailDrop.tryUse(player, context.getLevel(), InteractionHand.MAIN_HAND);
		TrailDrop.tryUse(player, context.getLevel(), InteractionHand.MAIN_HAND);
		List<PrimedTnt> spawned = context.getEntities(EntityType.TNT);
		TntGameTestHelper.assertEqual(context, 1, spawned.size(), "click-not-hold: one primed TNT");
		TntGameTestHelper.assertEqual(context, 7, player.getMainHandItem().getCount(), "only one TNT consumed");
		context.succeed();
	}

	@GameTest
	public void useOnBlockStillPlacesTnt(GameTestHelper context) {
		TrailDrop.clearHoldTracking();
		BlockPos stone = new BlockPos(5, 1, 5);
		context.setBlock(stone, Blocks.STONE);
		Player player = trailPlayer(context, GameType.SURVIVAL, new ItemStack(Items.TNT, 2), new ItemStack(Items.FLINT_AND_STEEL));

		context.useBlock(stone, player);

		context.assertBlockPresent(Blocks.TNT, stone.north());
		TntGameTestHelper.assertEqual(context, 0, context.getEntities(EntityType.TNT).size(), "placing TNT must not trail-drop");
		context.succeed();
	}

	@GameTest
	public void wrongOffhandDoesNothing(GameTestHelper context) {
		TrailDrop.clearHoldTracking();
		Player player = trailPlayer(context, GameType.SURVIVAL, new ItemStack(Items.TNT, 2), new ItemStack(Items.STICK));

		TntGameTestHelper.assertEqual(context, InteractionResult.PASS, TrailDrop.tryUse(player, context.getLevel(), InteractionHand.MAIN_HAND), "wrong offhand");
		TntGameTestHelper.assertEqual(context, 0, context.getEntities(EntityType.TNT).size(), "no primed TNT");
		TntGameTestHelper.assertEqual(context, 2, player.getMainHandItem().getCount(), "TNT not consumed");
		context.succeed();
	}

	@GameTest
	public void creativeDoesNotConsume(GameTestHelper context) {
		TrailDrop.clearHoldTracking();
		Player player = trailPlayer(context, GameType.CREATIVE, new ItemStack(Items.TNT, 1), new ItemStack(Items.FLINT_AND_STEEL));

		TrailDrop.tryUse(player, context.getLevel(), InteractionHand.MAIN_HAND);
		assertOnePrimedAtFeet(context, player, 100);
		TntGameTestHelper.assertEqual(context, 1, player.getMainHandItem().getCount(), "creative TNT stays");
		TntGameTestHelper.assertEqual(context, 0, player.getOffhandItem().getDamageValue(), "creative flint undamaged");
		context.succeed();
	}

	@GameTest
	public void adventureStillDrops(GameTestHelper context) {
		TrailDrop.clearHoldTracking();
		Player player = trailPlayer(context, GameType.ADVENTURE, new ItemStack(Items.TNT, 2), new ItemStack(Items.FLINT_AND_STEEL));

		TntGameTestHelper.assertEqual(context, InteractionResult.SUCCESS, TrailDrop.tryUse(player, context.getLevel(), InteractionHand.MAIN_HAND), "adventure allowed");
		assertOnePrimedAtFeet(context, player, 100);
		context.succeed();
	}

	@GameTest
	public void configOffDisablesTrailDrop(GameTestHelper context) {
		TrailDrop.clearHoldTracking();
		Player player = trailPlayer(context, GameType.SURVIVAL, new ItemStack(Items.TNT, 2), new ItemStack(Items.FLINT_AND_STEEL));
		synchronized (UsefulTntConfig.class) {
			boolean previous = UsefulTntConfig.isTrailDropEnabled();
			UsefulTntConfig.setTrailDropEnabled(false);
			try {
				TntGameTestHelper.assertEqual(context, InteractionResult.PASS, TrailDrop.tryUse(player, context.getLevel(), InteractionHand.MAIN_HAND), "config off");
				TntGameTestHelper.assertEqual(context, 0, context.getEntities(EntityType.TNT).size(), "no primed TNT");
			} finally {
				UsefulTntConfig.setTrailDropEnabled(previous);
			}
		}
		context.succeed();
	}

	@GameTest
	public void inheritsPlayerMotionIncludingVertical(GameTestHelper context) {
		TrailDrop.clearHoldTracking();
		Player player = trailPlayer(context, GameType.SURVIVAL, new ItemStack(Items.TNT, 2), new ItemStack(Items.FLINT_AND_STEEL));
		Vec3 motion = new Vec3(0.42, -0.18, 0.07);
		player.setDeltaMovement(motion);

		TrailDrop.tryUse(player, context.getLevel(), InteractionHand.MAIN_HAND);
		List<PrimedTnt> spawned = context.getEntities(EntityType.TNT);
		TntGameTestHelper.assertEqual(context, 1, spawned.size(), "one primed TNT");
		TntGameTestHelper.assertEqual(context, motion, spawned.get(0).getDeltaMovement(), "TNT copies player/vehicle velocity (replaces vanilla hop)");
		context.succeed();
	}

	@GameTest
	public void spectatorDoesNothing(GameTestHelper context) {
		TrailDrop.clearHoldTracking();
		Player player = trailPlayer(context, GameType.SPECTATOR, new ItemStack(Items.TNT, 2), new ItemStack(Items.FLINT_AND_STEEL));

		TntGameTestHelper.assertEqual(context, InteractionResult.PASS, TrailDrop.tryUse(player, context.getLevel(), InteractionHand.MAIN_HAND), "spectator");
		TntGameTestHelper.assertEqual(context, 0, context.getEntities(EntityType.TNT).size(), "no primed TNT");
		context.succeed();
	}

	private static Player trailPlayer(GameTestHelper context, GameType type, ItemStack main, ItemStack off) {
		Player player = context.makeMockPlayer(type);
		Vec3 pos = context.absoluteVec(Vec3.atBottomCenterOf(new BlockPos(2, 1, 2)));
		player.snapTo(pos.x, pos.y, pos.z, 0.0F, -90.0F);
		player.setItemInHand(InteractionHand.MAIN_HAND, main);
		player.setItemInHand(InteractionHand.OFF_HAND, off);
		return player;
	}

	private static void assertOnePrimedAtFeet(GameTestHelper context, Player player, int fuse) {
		List<PrimedTnt> spawned = context.getEntities(EntityType.TNT);
		TntGameTestHelper.assertEqual(context, 1, spawned.size(), "one primed TNT");
		PrimedTnt tnt = spawned.get(0);
		TntGameTestHelper.assertEqual(context, fuse, tnt.getFuse(), "trail-drop fuse");
		TntGameTestHelper.assertThat(context, tnt.blockPosition().equals(player.blockPosition()), "spawned at player feet");
	}
}
