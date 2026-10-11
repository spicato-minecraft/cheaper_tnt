package useful_tnt.gametest;

import com.mojang.serialization.Lifecycle;
import useful_tnt.UsefulTntRecipe;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class ConfigDisabledVanillaRecipeGameTest {

	@GameTest(maxTicks = 100)
	public void disabledKeepsVanillaTntRecipe(GameTestHelper context) {
		MappedRegistry<Recipe<?>> registry = new MappedRegistry<>(Registries.RECIPE, Lifecycle.stable());
		registry.register(UsefulTntRecipe.tntRecipeKey(), UsefulTntRecipe.vanillaTntRecipe(), RegistrationInfo.BUILT_IN);
		HolderLookup<Recipe<?>> input = registry.freeze();

		TntGameTestHelper.withCheaperRecipe(false, () -> {
			RecipeMap result = RecipeMap.create(UsefulTntRecipe.applyTo(input));
			RecipeHolder<?> kept = result.byKey(UsefulTntRecipe.tntRecipeKey());
			TntGameTestHelper.assertThat(context, kept != null && kept.value() instanceof ShapedRecipe, "Disabled swap should keep a TNT shaped recipe");

			ShapedRecipe recipe = (ShapedRecipe) kept.value();
			CraftingInput vanillaGrid = TntGameTestHelper.vanillaTntGrid();
			TntGameTestHelper.assertThat(context, recipe.matches(vanillaGrid, context.getLevel()), "Vanilla 5G+4S grid should still match when cheaper recipe is off");

			ItemStack assembled = recipe.assemble(vanillaGrid);
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
