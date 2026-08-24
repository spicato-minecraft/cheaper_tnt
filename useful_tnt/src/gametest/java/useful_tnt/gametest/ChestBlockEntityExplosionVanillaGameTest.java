package useful_tnt.gametest;

import useful_tnt.UsefulTntConfig;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

public class ChestBlockEntityExplosionVanillaGameTest {

	@GameTest(maxTicks = 100)
	public void chestBreaksAndSpilledItemsSurviveNextBlast(GameTestHelper context) {
		TntGameTestHelper.assertThat(context, UsefulTntConfig.isDropProtectionEnabled(), "dropProtectionEnabled should be on");

		BlockPos chestPos = new BlockPos(3, 1, 3);
		BlockPos blast = new BlockPos(4, 1, 3);
		context.setBlock(chestPos, Blocks.CHEST);
		ChestBlockEntity chest = (ChestBlockEntity) context.getBlockEntity(chestPos, ChestBlockEntity.class);
		chest.setItem(0, new ItemStack(Items.EMERALD, 3));

		TntGameTestHelper.detonatePrimedTnt(context, blast);

		context.assertBlockNotPresent(Blocks.CHEST, chestPos);
		context.assertItemEntityPresent(Items.EMERALD);

		TntGameTestHelper.detonatePrimedTnt(context, blast);
		context.assertItemEntityPresent(Items.EMERALD);

		context.succeed();
	}
}
