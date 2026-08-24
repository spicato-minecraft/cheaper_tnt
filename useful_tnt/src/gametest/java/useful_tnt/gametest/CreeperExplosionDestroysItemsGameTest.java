package useful_tnt.gametest;

import useful_tnt.UsefulTntConfig;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.Items;

public class CreeperExplosionDestroysItemsGameTest {

	@GameTest(maxTicks = 100)
	public void creeperStillCullsDroppedItems(GameTestHelper context) {
		TntGameTestHelper.assertThat(context, UsefulTntConfig.isDropProtectionEnabled(), "Protection on must not change creepers");

		BlockPos drop = new BlockPos(3, 1, 3);
		context.spawnItem(Items.DIAMOND, drop);
		Creeper creeper = (Creeper) context.spawn(EntityType.CREEPER, drop);
		TntGameTestHelper.explodeCreeper(creeper);

		context.assertItemEntityNotPresent(Items.DIAMOND);
		context.succeed();
	}
}
