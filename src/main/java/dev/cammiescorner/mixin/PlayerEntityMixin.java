package dev.cammiescorner.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.cammiescorner.common.components.TransformationComponent;
import dev.cammiescorner.common.registries.ModComponents;
import dev.cammiescorner.common.registries.ModTags;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) { super(entityType, world); }

	@ModifyReturnValue(method = "canConsume", at = @At("RETURN"))
	private boolean vampiresDontEat(boolean original, boolean ignoreHunger) {
		TransformationComponent component = getComponent(ModComponents.TRANSFORMATION);

		return (!component.getTransformation().isIn(ModTags.DRINKS_BLOOD) || ignoreHunger) && original;
	}

	@WrapWithCondition(method = "eatFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/HungerManager;eat(Lnet/minecraft/component/type/FoodComponent;)V"))
	private boolean dontGiveFoodToVampires(HungerManager instance, FoodComponent foodComponent) {
		return !getComponent(ModComponents.TRANSFORMATION).getTransformation().isIn(ModTags.DRINKS_BLOOD);
	}
}
