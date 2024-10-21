package dev.cammiescorner.mixin;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import dev.cammiescorner.common.entities.VampireBeastEntity;
import dev.cammiescorner.common.entities.WerewolfBeastEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity implements EquipmentHolder, Leashable, Targeter {
	@Unique private final MobEntity self = (MobEntity) (Object) this;

	protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) { super(entityType, world); }

	@ModifyReceiver(method = "tickNewAi", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/control/MoveControl;tick()V"))
	private MoveControl makeVampFly(MoveControl instance) {
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
		else if(self instanceof WerewolfBeastEntity beast) {
			MoveControl control = instance;

			if(isSubmergedInWater())
				control = beast.swimMoveControl;

			return control;
		}

		return instance;
	}
}
