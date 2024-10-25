package dev.cammiescorner.witchsblights.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.cammiescorner.witchsblights.common.entities.BeastEntity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.ai.pathing.LandPathNodeMaker;
import net.minecraft.entity.ai.pathing.PathNodeMaker;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LandPathNodeMaker.class)
public abstract class LandPathNodeMakerMixin extends PathNodeMaker {
	@ModifyExpressionValue(method = "isBlocked(Lnet/minecraft/entity/ai/pathing/PathNode;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobEntity;getBoundingBox()Lnet/minecraft/util/math/Box;"))
	private Box changeBoundingBox(Box original) {
		if(entity instanceof BeastEntity beast)
			return beast.getBoundingBox(EntityPose.CROUCHING);

		return original;
	}
}
