package dev.cammiescorner.mixin;

import dev.cammiescorner.common.registries.ModComponents;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
	@Inject(method = "onPlayerCollision", at = @At("HEAD"), cancellable = true)
	private void witchsblights$beastsDontNeedItems(PlayerEntity player, CallbackInfo info) {
		if(player.getComponent(ModComponents.TRANSFORMATION).isTransformed())
			info.cancel();
	}
}
