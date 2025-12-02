package fun.wich.mixin;

import fun.wich.HoneySlimeEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.SlimeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SlimeEntity.class)
public class HoneySlimes_SlimeEntityMixin {
	@Inject(method="damage", at=@At(value="INVOKE", target="Lnet/minecraft/enchantment/EnchantmentHelper;onTargetDamaged(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;)V"))
	private void Mixin_HoneySlimeHealPoisonOnHit(LivingEntity target, CallbackInfo ci) {
		if ((Object)this instanceof HoneySlimeEntity) target.removeStatusEffect(StatusEffects.POISON);
	}
}
