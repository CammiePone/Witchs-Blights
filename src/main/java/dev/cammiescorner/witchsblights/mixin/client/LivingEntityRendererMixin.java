package dev.cammiescorner.witchsblights.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.cammiescorner.witchsblights.common.registries.ModComponents;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {
	@ModifyReturnValue(method = "isShaking", at = @At("RETURN"))
	private boolean shakeWhatYourMommaGaveYou(boolean original, T entity) {
		return original || (entity instanceof PlayerEntity player && player.getComponent(ModComponents.CURE_CURSE).isCuring());
	}
}
