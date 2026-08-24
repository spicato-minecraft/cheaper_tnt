package useful_tnt.mixin;

import useful_tnt.UsefulTntRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin {

	@ModifyVariable(method = "apply", at = @At("HEAD"), argsOnly = true)
	private RecipeMap cheaperTnt$modifyRecipes(RecipeMap original) {
		return UsefulTntRecipe.applyTo(original);
	}
}
