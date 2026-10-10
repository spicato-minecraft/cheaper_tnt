package useful_tnt.gametest;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.vehicle.minecart.MinecartTNT;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import useful_tnt.UsefulTntConfig;

public class MinecartTntKeepsItemsGameTest {

	@GameTest(maxTicks = 100)
	public void minecartTntKeepsDroppedItems(GameTestHelper context) {
		TntGameTestHelper.assertThat(context, UsefulTntConfig.isDropProtectionEnabled(), "drop protection should default on");

		BlockPos drop = new BlockPos(2, 1, 2);
		BlockPos blast = new BlockPos(3, 1, 2);
		context.spawnItem(Items.DIAMOND, drop);

		ServerLevel level = context.getLevel();
		Vec3 pos = context.absoluteVec(Vec3.atCenterOf(blast));
		MinecartTNT cart = new MinecartTNT(EntityTypes.TNT_MINECART, level);
		cart.setPos(pos.x, pos.y, pos.z);
		level.addFreshEntity(cart);
		level.explode(cart, pos.x, pos.y, pos.z, 4.0F, Level.ExplosionInteraction.TNT);
		cart.discard();

		context.assertItemEntityPresent(Items.DIAMOND);
		context.succeed();
	}
}
