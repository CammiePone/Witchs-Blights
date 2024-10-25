package dev.cammiescorner.witchsblights.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.cammiescorner.witchsblights.common.status_effects.CursedStatusEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;

@Mixin(PotionEntity.class)
public class PotionEntityMixin {
	@Inject(method = "applySplashPotion", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffectInstance;getEffectType()Lnet/minecraft/registry/entry/RegistryEntry;"), locals = LocalCapture.CAPTURE_FAILSOFT)
	private void captureSplashStatusEffectInstance(Iterable<StatusEffectInstance> effects, @Nullable Entity entity, CallbackInfo info, Box box, List list, Entity entity2, Iterator var6, LivingEntity livingEntity, double d, double e, Iterator var12, StatusEffectInstance statusEffectInstance, @Share("originalSplashEffect") LocalRef<StatusEffectInstance> ref) {
		ref.set(statusEffectInstance);
	}

	@ModifyExpressionValue(method = "applySplashPotion", at = @At(value = "NEW", target = "(Lnet/minecraft/registry/entry/RegistryEntry;IIZZ)Lnet/minecraft/entity/effect/StatusEffectInstance;"))
	private StatusEffectInstance fixSplashPotions(StatusEffectInstance original, @Share("originalSplashEffect") LocalRef<StatusEffectInstance> ref) {
		int duration = original.getEffectType().value() instanceof CursedStatusEffect ? ref.get().getDuration() : original.getDuration();

		return new StatusEffectInstance(original.getEffectType(), duration, original.getAmplifier(), original.isAmbient(), original.shouldShowParticles(), ref.get().shouldShowIcon());
	}
}
