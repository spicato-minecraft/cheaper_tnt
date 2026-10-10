package useful_tnt.gametest;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import useful_tnt.TrailDrop;
import useful_tnt.UsefulTntConfig;

import java.util.List;

public class CustomFuseTicksGameTest {

	@GameTest(maxTicks = 100)
	public void trailDropUsesConfiguredFuse(GameTestHelper context) {
		TrailDrop.clearHoldTracking();
		int previous = UsefulTntConfig.getTrailDropFuseTicks();
		UsefulTntConfig.setTrailDropFuseTicks(40);
		try {
			Player player = context.makeMockPlayer(GameType.SURVIVAL);
			Vec3 pos = context.absoluteVec(Vec3.atBottomCenterOf(new BlockPos(2, 1, 2)));
			player.snapTo(pos.x, pos.y, pos.z, 0.0F, -90.0F);
			player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.TNT, 2));
			player.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(Items.FLINT_AND_STEEL));

			TrailDrop.tryUse(player, context.getLevel(), InteractionHand.MAIN_HAND);

			List<PrimedTnt> spawned = context.getEntities(EntityTypes.TNT);
			TntGameTestHelper.assertEqual(context, 1, spawned.size(), "one primed TNT");
			TntGameTestHelper.assertEqual(context, 40, spawned.get(0).getFuse(), "configured fuse");
		} finally {
			UsefulTntConfig.setTrailDropFuseTicks(previous);
			TrailDrop.clearHoldTracking();
		}
		context.succeed();
	}
}
