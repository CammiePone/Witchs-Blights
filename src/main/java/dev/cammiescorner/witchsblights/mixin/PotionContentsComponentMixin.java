package dev.cammiescorner.witchsblights.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.cammiescorner.witchsblights.common.status_effects.CursedStatusEffect;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.OptionalInt;

@Mixin(PotionContentsComponent.class)
public class PotionContentsComponentMixin {
	@Inject(method = "mixColors", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffectInstance;shouldShowParticles()Z"), locals = LocalCapture.CAPTURE_FAILSOFT)
	private static void captureStatusEffectInstance(Iterable<StatusEffectInstance> effects, CallbackInfoReturnable<OptionalInt> cir, int i, int j, int k, int l, Iterator var5, StatusEffectInstance statusEffectInstance, @Share("statusEffectInstance") LocalRef<StatusEffectInstance> ref) {
		ref.set(statusEffectInstance);
	}

	@ModifyExpressionValue(method = "mixColors", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffectInstance;shouldShowParticles()Z"))
	private static boolean showPotionColor(boolean original, @Share("statusEffectInstance") LocalRef<StatusEffectInstance> ref) {
		return original || ref.get().getEffectType().value() instanceof CursedStatusEffect;
	}
}
