package useful_tnt.gametest;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import useful_tnt.UsefulTntConfig;

/**
 * Respawn-anchor blasts are player-sourced block explosions. Wither blasts are mob explosions.
 * Neither source is primed TNT or a TNT minecart, so dropped items must still be destroyed.
 */
public class NonTntExplosionCullsItemsGameTest {

	@GameTest(maxTicks = 100)
	public void anchorAndWitherStillCullItems(GameTestHelper context) {
		TntGameTestHelper.assertThat(context, UsefulTntConfig.isDropProtectionEnabled(), "protection on must not cover these blasts");

		cullWithSource(context, new BlockPos(2, 1, 2), true);
		cullWithSource(context, new BlockPos(6, 1, 2), false);
		context.succeed();
	}

	private static void cullWithSource(GameTestHelper context, BlockPos drop, boolean wither) {
		context.spawnItem(Items.EMERALD, drop);
		ServerLevel level = context.getLevel();
		Vec3 pos = context.absoluteVec(Vec3.atCenterOf(drop));
		if (wither) {
			WitherBoss boss = (WitherBoss) context.spawn(EntityTypes.WITHER, drop);
			level.explode(boss, pos.x, pos.y, pos.z, 1.0F, Level.ExplosionInteraction.MOB);
			boss.discard();
		} else {
			Player player = context.makeMockPlayer(GameType.SURVIVAL);
			player.snapTo(pos.x, pos.y, pos.z, 0.0F, 0.0F);
			level.explode(player, pos.x, pos.y, pos.z, 1.0F, Level.ExplosionInteraction.BLOCK);
		}
		context.assertItemEntityNotPresent(Items.EMERALD);
	}
}
