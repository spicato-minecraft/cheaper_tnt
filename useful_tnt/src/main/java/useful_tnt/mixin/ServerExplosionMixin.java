package useful_tnt.mixin;

import useful_tnt.TntDropProtection;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ServerExplosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerExplosion.class)
public abstract class ServerExplosionMixin {

	@WrapOperation(
			method = "hurtEntities",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/Entity;hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z"
			)
	)
	private boolean cheaperTnt$skipTntItemCull(
			Entity entity,
			ServerLevel level,
			DamageSource source,
			float amount,
			Operation<Boolean> original
	) {
		if (TntDropProtection.shouldProtectDroppedItem(entity, source)) {
			return false;
		}
		return original.call(entity, level, source, amount);
	}
}
