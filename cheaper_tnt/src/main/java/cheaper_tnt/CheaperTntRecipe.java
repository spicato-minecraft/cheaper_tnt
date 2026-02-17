package cheaper_tnt;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

public final class CheaperTntRecipe {

    private static final ResourceLocation TNT_RECIPE_ID = ResourceLocation.fromNamespaceAndPath("minecraft", "tnt");

    public static ResourceKey<Recipe<?>> tntRecipeKey() {
        return ResourceKey.create(net.minecraft.core.registries.Registries.RECIPE, TNT_RECIPE_ID);
    }

    public static boolean isTntRecipe(RecipeHolder<?> holder) {
        return holder.id().location().equals(TNT_RECIPE_ID);
    }
}
