package useful_tnt.gametest;

import useful_tnt.UsefulTntConfig;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.pig.Pig;

public class MobStillTakesTntDamageGameTest {

	@GameTest(maxTicks = 100)
	public void pigTakesTntDamageWithProtectionOn(GameTestHelper context) {
		TntGameTestHelper.assertThat(context, UsefulTntConfig.isDropProtectionEnabled(), "dropProtectionEnabled should be on");

		BlockPos pigPos = new BlockPos(3, 1, 3);
		Pig pig = (Pig) context.spawn(EntityType.PIG, pigPos);
		float before = pig.getHealth();

		TntGameTestHelper.detonatePrimedTnt(context, pigPos);

		TntGameTestHelper.assertThat(context, !pig.isAlive() || pig.getHealth() < before, "Mobs should still take TNT damage");
		context.succeed();
	}
}
