package useful_tnt.gametest;

import useful_tnt.UsefulTntRecipe;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.item.crafting.ShapedRecipe;

import java.util.List;

public class ConfigDisabledVanillaRecipeGameTest {

	@GameTest(maxTicks = 100)
	public void disabledKeepsVanillaTntRecipe(GameTestHelper context) {
		RecipeHolder<?> vanillaHolder = new RecipeHolder<>(UsefulTntRecipe.tntRecipeKey(), UsefulTntRecipe.vanillaTntRecipe());
		RecipeMap input = RecipeMap.create(List.of(vanillaHolder));

		TntGameTestHelper.withCheaperRecipe(false, () -> {
			RecipeMap result = UsefulTntRecipe.applyTo(input);
			RecipeHolder<?> kept = result.byKey(UsefulTntRecipe.tntRecipeKey());
			TntGameTestHelper.assertThat(context, kept != null && kept.value() instanceof ShapedRecipe, "Disabled swap should keep a TNT shaped recipe");

			ShapedRecipe recipe = (ShapedRecipe) kept.value();
			CraftingInput vanillaGrid = TntGameTestHelper.vanillaTntGrid();
			TntGameTestHelper.assertThat(context, recipe.matches(vanillaGrid, context.getLevel()), "Vanilla 5G+4S grid should still match when cheaper recipe is off");

			ItemStack assembled = recipe.assemble(vanillaGrid, context.getLevel().registryAccess());
			TntGameTestHelper.assertThat(context, assembled.is(Items.TNT), "Vanilla TNT recipe should still produce TNT when cheaper recipe is off");

			TntGameTestHelper.assertNot(
					context,
					recipe.matches(TntGameTestHelper.cheaperTntGrid(), context.getLevel()),
					"Cheap criss-cross should not match the vanilla TNT recipe"
			);
		});

		context.succeed();
	}
}
