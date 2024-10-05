package dev.cammiescorner.mixin;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.cammiescorner.common.status_effects.CursedStatusEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MilkBucketItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(MilkBucketItem.class)
public class MilkBucketItemMixin {
	@Inject(method = "finishUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;clearStatusEffects()Z"))
	private void witchsblights$captureStatusEffect(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> info, @Share("statusEffects") LocalRef<List<StatusEffectInstance>> ref) {
		ref.set(user.getStatusEffects().stream().filter(statusEffectInstance -> statusEffectInstance.getEffectType().value() instanceof CursedStatusEffect).toList());
	}

	@Inject(method = "finishUsing", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/LivingEntity;clearStatusEffects()Z"))
	private void witchsblights$dontClearIncurableEffects(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> info, @Share("statusEffects") LocalRef<List<StatusEffectInstance>> ref) {
		for(StatusEffectInstance statusEffectInstance : ref.get()) {
			user.addStatusEffect(statusEffectInstance);
		}
	}
}
