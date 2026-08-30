package useful_tnt.gametest;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;

public class PlacedTntIgniteStillVanillaGameTest {

	@GameTest
	public void flintOnPlacedTntPrimesAndDamagesFlint(GameTestHelper context) {
		BlockPos tnt = new BlockPos(2, 1, 2);
		context.setBlock(tnt, Blocks.TNT);

		Player player = context.makeMockPlayer(GameType.SURVIVAL);
		player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.FLINT_AND_STEEL));
		context.useBlock(tnt, player);

		context.assertBlockNotPresent(Blocks.TNT, tnt);
		context.assertEntityPresent(EntityType.TNT, tnt, 1.5);
		TntGameTestHelper.assertEqual(context, 1, player.getItemInHand(InteractionHand.MAIN_HAND).getDamageValue(), "flint should take 1 durability");
		context.succeed();
	}

	@GameTest
	public void fireChargeOnPlacedTntPrimesAndConsumesCharge(GameTestHelper context) {
		BlockPos tnt = new BlockPos(2, 1, 2);
		context.setBlock(tnt, Blocks.TNT);

		Player player = context.makeMockPlayer(GameType.SURVIVAL);
		player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.FIRE_CHARGE, 3));
		context.useBlock(tnt, player);

		context.assertBlockNotPresent(Blocks.TNT, tnt);
		context.assertEntityPresent(EntityType.TNT, tnt, 1.5);
		TntGameTestHelper.assertEqual(context, 2, player.getItemInHand(InteractionHand.MAIN_HAND).getCount(), "one fire charge consumed");
		context.succeed();
	}
}
