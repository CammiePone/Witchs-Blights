package dev.cammiescorner.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.cammiescorner.WitchsBlights;
import dev.cammiescorner.api.Transformation;
import dev.cammiescorner.common.components.TransformationComponent;
import dev.cammiescorner.common.items.RosaryItem;
import dev.cammiescorner.common.registries.*;
import dev.cammiescorner.common.status_effects.CursedStatusEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
	@Unique private final LivingEntity self = (LivingEntity) (Object) this;

	@Shadow public abstract boolean hasStatusEffect(RegistryEntry<StatusEffect> effect);
	@Shadow public abstract Collection<StatusEffectInstance> getStatusEffects();

	public LivingEntityMixin(EntityType<?> type, World world) { super(type, world); }

	@Inject(method = "onStatusEffectApplied", at = @At("HEAD"))
	private void addTransformation(StatusEffectInstance effect, Entity source, CallbackInfo info) {
		if(self instanceof PlayerEntity player) {
			TransformationComponent component = player.getComponent(ModComponents.TRANSFORMATION);

			if(component.getTransformation() == ModTransformations.NONE.get()) {
				for(Transformation tf : WitchsBlights.TRANSFORMATIONS) {
					if(tf.getCurse() == null)
						continue;

					if(tf.getCurse().value().getEffects().getFirst().getEffectType().equals(effect.getEffectType())) {
						component.setTransformation(tf);
						syncComponent(ModComponents.TRANSFORMATION);
						break;
					}
				}
			}
		}
	}

	@Inject(method = "onStatusEffectRemoved", at = @At("HEAD"))
	private void removeTransformation(StatusEffectInstance effect, CallbackInfo info) {
		if(self instanceof ServerPlayerEntity player && effect.getEffectType().value() instanceof CursedStatusEffect) {
			TransformationComponent component = player.getComponent(ModComponents.TRANSFORMATION);

			component.setTransformation(ModTransformations.NONE.get());
			component.stopUrging();
		}
	}

	@Inject(method = "canHaveStatusEffect", at = @At("HEAD"), cancellable = true)
	private void shouldApplyCurse(StatusEffectInstance effect, CallbackInfoReturnable<Boolean> info) {
		if(effect.getEffectType().value() instanceof CursedStatusEffect && RosaryItem.hasRosaryEquipped(self)) {
			EquipmentSlot slot = RosaryItem.getRosarySlot(self);
			ItemStack stack = self.getEquippedStack(slot);

			stack.damage(10, self, slot);
			info.setReturnValue(false);
			return;
		}

		if(effect.getEffectType().value() instanceof CursedStatusEffect && !getStatusEffects().stream().filter(s -> s.getEffectType().value() instanceof CursedStatusEffect && !s.getEffectType().equals(effect.getEffectType())).toList().isEmpty())
			info.setReturnValue(false);
	}

	@Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageSource;getAttacker()Lnet/minecraft/entity/Entity;", ordinal = 0))
	private void letsPaintThisClothRed(DamageSource source, float amount, CallbackInfoReturnable<Boolean> info) {
		if(self instanceof PlayerEntity && source.isDirect() && source.getAttacker() instanceof PlayerEntity attacker && attacker.getOffHandStack().isOf(ModItems.CLOTH.get())) {
			ItemStack stack = attacker.getOffHandStack();
			ItemStack newStack = new ItemStack(ModItems.BLOODSTAINED_CLOTH.get());

			newStack.set(ModComponentTypes.TARGETED_ENTITY.get(), getUuid());
			stack.decrementUnlessCreative(1, attacker);

			if(!attacker.getInventory().insertStack(newStack))
				attacker.dropItem(newStack, false);
		}
	}

	@ModifyReturnValue(method = "hasInvertedHealingAndHarm", at = @At("RETURN"))
	private boolean invertHealingAndHarm(boolean original) {
		return original || hasStatusEffect(ModStatusEffects.SANGUINE_BLIGHT.holder());
	}
}
