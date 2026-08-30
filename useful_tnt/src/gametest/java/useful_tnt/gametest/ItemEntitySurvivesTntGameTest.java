package useful_tnt.gametest;

import useful_tnt.UsefulTntConfig;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.Items;

public class ItemEntitySurvivesTntGameTest {

	@GameTest(maxTicks = 100)
	public void itemSurvivesChainedTntBlasts(GameTestHelper context) {
		TntGameTestHelper.assertThat(context, UsefulTntConfig.isDropProtectionEnabled(), "dropProtectionEnabled should default on");

		BlockPos drop = new BlockPos(2, 1, 2);
		BlockPos blast = new BlockPos(4, 1, 2);
		context.spawnItem(Items.DIAMOND, drop);

		TntGameTestHelper.detonatePrimedTnt(context, blast);
		TntGameTestHelper.detonatePrimedTnt(context, blast);

		context.assertItemEntityPresent(Items.DIAMOND);
		context.succeed();
	}
}
