package useful_tnt.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TntBlock.class)
public interface TntBlockPrimeInvoker {

	@Invoker("prime")
	static boolean invokePrime(Level level, BlockPos blockPos, @Nullable LivingEntity livingEntity) {
		throw new AssertionError();
	}
}
