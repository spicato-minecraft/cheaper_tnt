package useful_tnt.gametest;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.Items;

public class DropProtectionOffRestoresCullGameTest {

	@GameTest(maxTicks = 100)
	public void disabledProtectionLetsTntDestroyItems(GameTestHelper context) {
		BlockPos drop = new BlockPos(2, 1, 2);
		BlockPos blast = new BlockPos(3, 1, 2);
		context.spawnItem(Items.GOLD_INGOT, drop);

		TntGameTestHelper.withDropProtection(false, () -> TntGameTestHelper.detonatePrimedTnt(context, blast));

		context.assertItemEntityNotPresent(Items.GOLD_INGOT);
		context.succeed();
	}
}
