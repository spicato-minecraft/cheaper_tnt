package useful_tnt.gametest;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;

public class NonTntRecipesUntouchedGameTest {

	@GameTest(maxTicks = 100)
	public void stickRecipeStillCraftsFromPlanks(GameTestHelper context) {
		CraftingInput sticks = TntGameTestHelper.grid3x3(
				Items.AIR, Items.AIR, Items.AIR,
				Items.AIR, Items.OAK_PLANKS, Items.AIR,
				Items.AIR, Items.OAK_PLANKS, Items.AIR
		);

		ItemStack result = TntGameTestHelper.craft(context, sticks);
		TntGameTestHelper.assertThat(context, result.is(Items.STICK), "Non-TNT recipes should still craft");
		TntGameTestHelper.assertEqual(context, 4, result.getCount(), "Two planks should still make 4 sticks");

		context.succeed();
	}
}
