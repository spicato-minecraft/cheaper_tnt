package useful_tnt.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import useful_tnt.TntIgnition;

@Mixin(TntBlock.class)
public abstract class TntBlockMixin {

	@WrapMethod(method = "useItemOn")
	private InteractionResult usefulTnt$useSharedIgniterPath(
			ItemStack itemStack,
			BlockState blockState,
			Level level,
			BlockPos blockPos,
			Player player,
			InteractionHand interactionHand,
			BlockHitResult blockHitResult,
			Operation<InteractionResult> original
	) {
		if (!itemStack.is(Items.FLINT_AND_STEEL) && !itemStack.is(Items.FIRE_CHARGE)) {
			return original.call(itemStack, blockState, level, blockPos, player, interactionHand, blockHitResult);
		}
		return TntIgnition.useIgniterOnPlacedTnt(itemStack, level, blockPos, player, interactionHand);
	}
}
