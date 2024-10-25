package dev.cammiescorner.witchsblights.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;

@Mixin(AreaEffectCloudEntity.class)
public class AreaEffectCloudEntityMixin {
	@Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffectInstance;getEffectType()Lnet/minecraft/registry/entry/RegistryEntry;", ordinal = 0), locals = LocalCapture.CAPTURE_FAILSOFT)
	private void captureLingeringStatusEffectInstance(CallbackInfo ci, boolean bl, float f, List list, Iterator var5, StatusEffectInstance statusEffectInstance, @Share("originalLingeringEffect") LocalRef<StatusEffectInstance> ref) {
		ref.set(statusEffectInstance);
	}

	@ModifyExpressionValue(method = "tick", at = @At(value = "NEW", target = "(Lnet/minecraft/registry/entry/RegistryEntry;IIZZ)Lnet/minecraft/entity/effect/StatusEffectInstance;"))
	private StatusEffectInstance fixLingeringPotions(StatusEffectInstance original, @Share("originalLingeringEffect") LocalRef<StatusEffectInstance> ref) {
		return new StatusEffectInstance(original.getEffectType(), original.getDuration(), original.getAmplifier(), original.isAmbient(), original.shouldShowParticles(), ref.get().shouldShowIcon());
	}
}
