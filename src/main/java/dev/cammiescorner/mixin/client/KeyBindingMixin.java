package dev.cammiescorner.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.cammiescorner.common.components.TransformationComponent;
import dev.cammiescorner.common.entities.BeastEntity;
import dev.cammiescorner.common.registries.ModComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Set;

@Mixin(KeyBinding.class)
public class KeyBindingMixin {
	@Unique private final MinecraftClient client = MinecraftClient.getInstance();
	@Unique private final KeyBinding self = (KeyBinding) (Object) this;

	@Shadow private boolean pressed;

	@ModifyReturnValue(method = "isPressed", at = @At("RETURN"))
	private boolean isNotPressedWhileTransformed(boolean original) {
		PlayerEntity player = client.player;

		if(!client.isPaused() && player != null && player.getWorld() != null && !permittedKeys().contains(self)) {
			TransformationComponent component = player.getComponent(ModComponents.TRANSFORMATION);

			if(component.isTransformed() && client.getCameraEntity() instanceof BeastEntity) {
				this.pressed = false;
				return false;
			}
		}

		return original;
	}

	@ModifyReturnValue(method = "wasPressed", at = @At(value = "RETURN", ordinal = 1))
	private boolean wasNotPressedWhileTransformed(boolean original) {
		PlayerEntity player = client.player;

		if(!client.isPaused() && player != null && player.getWorld() != null && !permittedKeys().contains(self)) {
			TransformationComponent component = player.getComponent(ModComponents.TRANSFORMATION);

			if(component.isTransformed() && client.getCameraEntity() instanceof BeastEntity) {
				this.pressed = false;
				return false;
			}
		}

		return original;
	}

	@Unique
	private Set<KeyBinding> permittedKeys() {
		GameOptions options = client.options;

		return Set.of(
				options.togglePerspectiveKey,
				options.fullscreenKey,
				options.commandKey,
				options.chatKey,
				options.screenshotKey,
				options.playerListKey,
				options.smoothCameraKey,
				options.socialInteractionsKey
		);
	}
}
