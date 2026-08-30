package useful_tnt.gametest;

import useful_tnt.UsefulTntConfig;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ConfigEnabledCheaperRecipeGameTest {

	@GameTest(maxTicks = 100)
	public void cheaperPatternCraftsTntAndVanillaPatternDoesNot(GameTestHelper context) {
		TntGameTestHelper.assertThat(context, UsefulTntConfig.isEnabled(), "cheaperTntEnabled should default on for GameTests");

		ItemStack cheap = TntGameTestHelper.craft(context, TntGameTestHelper.cheaperTntGrid());
		TntGameTestHelper.assertThat(context, cheap.is(Items.TNT), "2 gunpowder + 2 sand criss-cross should craft TNT");
		TntGameTestHelper.assertEqual(context, 1, cheap.getCount(), "Cheaper recipe should yield 1 TNT");

		ItemStack vanilla = TntGameTestHelper.craft(context, TntGameTestHelper.vanillaTntGrid());
		TntGameTestHelper.assertThat(context, vanilla.isEmpty(), "Vanilla 5 gunpowder + 4 sand TNT recipe should be removed");

		context.succeed();
	}
}
