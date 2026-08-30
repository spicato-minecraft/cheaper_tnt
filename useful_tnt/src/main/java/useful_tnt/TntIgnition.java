package useful_tnt;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;
import useful_tnt.mixin.TntBlockPrimeInvoker;

/**
 * Shared TNT prime + igniter cost. Vanilla {@code TntBlock.useItemOn} and trail-drop both call this
 * so a later fire-charge mod can patch consume in one place.
 */
public final class TntIgnition {

	private TntIgnition() {
	}

	public static boolean prime(Level level, BlockPos pos, @Nullable LivingEntity owner) {
		return TntBlockPrimeInvoker.invokePrime(level, pos, owner);
	}

	/**
	 * Flint durability or fire-charge consume, plus {@link Stats#ITEM_USED}.
	 * Matches {@code TntBlock.useItemOn} after a successful prime.
	 */
	public static void applyIgniterCost(ItemStack stack, Player player, InteractionHand hand) {
		Item item = stack.getItem();
		if (stack.is(Items.FLINT_AND_STEEL)) {
			stack.hurtAndBreak(1, player, hand.asEquipmentSlot());
		} else if (stack.is(Items.FIRE_CHARGE)) {
			stack.consume(1, player);
		}
		player.awardStat(Stats.ITEM_USED.get(item));
	}

	public static InteractionResult useIgniterOnPlacedTnt(ItemStack itemStack, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand) {
		if (prime(level, blockPos, player)) {
			level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 11);
			applyIgniterCost(itemStack, player, interactionHand);
		} else if (level instanceof ServerLevel serverLevel && !serverLevel.getGameRules().get(GameRules.TNT_EXPLODES)) {
			player.displayClientMessage(Component.translatable("block.minecraft.tnt.disabled"), true);
			return InteractionResult.PASS;
		}
		return InteractionResult.SUCCESS;
	}
}
