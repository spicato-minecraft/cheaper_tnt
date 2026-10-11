package useful_tnt.mixin;

import useful_tnt.UsefulTntRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin {

	@ModifyArg(
			method = "<init>",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/item/crafting/RecipeMap;create(Lnet/minecraft/core/HolderLookup;)Lnet/minecraft/world/item/crafting/RecipeMap;"
			)
	)
	private HolderLookup<Recipe<?>> cheaperTnt$modifyRecipes(HolderLookup<Recipe<?>> original) {
		return UsefulTntRecipe.applyTo(original);
	}
}
