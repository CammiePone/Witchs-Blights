package dev.cammiescorner.mixin;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import dev.cammiescorner.common.entities.VampireBeastEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.mob.MobEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin {
	@Unique private final MobEntity self = (MobEntity) (Object) this;

	@Shadow @Nullable public abstract LivingEntity getTarget();

	@ModifyReceiver(method = "tickNewAi", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/control/MoveControl;tick()V"))
	private MoveControl witchsblights$makeVampFly(MoveControl instance) {
		if(self instanceof VampireBeastEntity beast) {
			MoveControl control = instance;

			if(getTarget() != null && !beast.canReachTarget(getTarget())) {
				if(beast.getTargetOutOfReachTimer() > 50 || getTarget().isFallFlying())
					control = beast.flightMoveControl;
				else
					beast.incrementTargetOutOfReachTimer();
			}
			else {
				beast.resetTargetOutOfReachTimer();
			}

			if(control == instance && beast.isFlying())
				beast.setFlying(false);

			return control;
		}

		return instance;
	}
}
