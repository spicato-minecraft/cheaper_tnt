package cheaper_tnt.mixin;

import cheaper_tnt.CheaperTntConfig;
import cheaper_tnt.CheaperTntRecipe;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin {

    @ModifyVariable(method = "apply", at = @At("HEAD"), argsOnly = true)
    private RecipeMap cheaperTnt$modifyRecipes(RecipeMap original) {
        if (!CheaperTntConfig.isEnabled()) {
            return original;
        }

        List<RecipeHolder<?>> recipes = new ArrayList<>();
        StreamSupport.stream(original.values().spliterator(), false)
                .filter(holder -> !CheaperTntRecipe.isTntRecipe(holder))
                .forEach(recipes::add);

        ResourceKey<Recipe<?>> tntKey = CheaperTntRecipe.tntRecipeKey();
        ShapedRecipePattern pattern = ShapedRecipePattern.of(
                Map.of(
                        'G', Ingredient.of(Items.GUNPOWDER),
                        'S', Ingredient.of(Items.SAND)
                ),
                " G ",
                "S S",
                " G "
        );
        ShapedRecipe cheaperRecipe = new ShapedRecipe(
                "",
                CraftingBookCategory.REDSTONE,
                pattern,
                new ItemStack(Items.TNT),
                true
        );
        recipes.add(new RecipeHolder<>(tntKey, cheaperRecipe));

        return RecipeMap.create(recipes);
    }
}
