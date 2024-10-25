package dev.cammiescorner.witchsblights.mixin;

import dev.cammiescorner.witchsblights.common.registries.ModComponents;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
	@Inject(method = "onPlayerCollision", at = @At("HEAD"), cancellable = true)
	private void beastsDontNeedItems(PlayerEntity player, CallbackInfo info) {
		if(player.getComponent(ModComponents.TRANSFORMATION).isTransformed())
			info.cancel();
	}
}
