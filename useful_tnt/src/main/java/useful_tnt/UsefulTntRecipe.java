package useful_tnt;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

import java.util.Map;

public final class UsefulTntRecipe {

	private static final Identifier TNT_RECIPE_ID = Identifier.fromNamespaceAndPath("minecraft", "tnt");

	private UsefulTntRecipe() {
	}

	public static ResourceKey<Recipe<?>> tntRecipeKey() {
		return ResourceKey.create(net.minecraft.core.registries.Registries.RECIPE, TNT_RECIPE_ID);
	}

	public static boolean isTntRecipe(RecipeHolder<?> holder) {
		return holder.id().identifier().equals(TNT_RECIPE_ID);
	}

	public static ShapedRecipe cheaperTntRecipe() {
		ShapedRecipePattern pattern = ShapedRecipePattern.of(
				Map.of(
						'G', Ingredient.of(Items.GUNPOWDER),
						'S', Ingredient.of(Items.SAND)
				),
				" G ",
				"S S",
				" G "
		);
		return shapedTnt(pattern);
	}

	public static ShapedRecipe vanillaTntRecipe() {
		ShapedRecipePattern pattern = ShapedRecipePattern.of(
				Map.of(
						'G', Ingredient.of(Items.GUNPOWDER),
						'S', Ingredient.of(Items.SAND)
				),
				"GGG",
				"GSG",
				"SSS"
		);
		return shapedTnt(pattern);
	}

	private static ShapedRecipe shapedTnt(ShapedRecipePattern pattern) {
		return new ShapedRecipe(
				new Recipe.CommonInfo(true),
				new CraftingRecipe.CraftingBookInfo(CraftingBookCategory.REDSTONE, ""),
				pattern,
				new ItemStackTemplate(Items.TNT)
		);
	}

	public static HolderLookup<Recipe<?>> applyTo(HolderLookup<Recipe<?>> original) {
		if (!UsefulTntConfig.isEnabled()) {
			return original;
		}

		MappedRegistry<Recipe<?>> registry = new MappedRegistry<>(Registries.RECIPE, Lifecycle.stable());
		boolean sawTnt = false;
		for (Holder.Reference<Recipe<?>> holder : original.listElements().toList()) {
			ResourceKey<Recipe<?>> key = holder.key();
			Recipe<?> recipe = holder.value();
			if (key.equals(tntRecipeKey())) {
				sawTnt = true;
				recipe = cheaperTntRecipe();
			}
			registry.register(key, recipe, RegistrationInfo.BUILT_IN);
		}
		if (!sawTnt) {
			registry.register(tntRecipeKey(), cheaperTntRecipe(), RegistrationInfo.BUILT_IN);
		}
		return registry.freeze();
	}
}
