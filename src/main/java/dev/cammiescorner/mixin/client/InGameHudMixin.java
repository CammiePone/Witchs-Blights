package dev.cammiescorner.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.cammiescorner.WitchsBlights;
import dev.cammiescorner.common.components.TransformationComponent;
import dev.cammiescorner.common.registries.ModComponents;
import dev.cammiescorner.common.registries.ModTags;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InGameHud.class)
public class InGameHudMixin {
	@Unique private static final Identifier BLOOD_EMPTY_HUNGER_TEXTURE = WitchsBlights.id("hud/blood_empty_hunger");
	@Unique private static final Identifier BLOOD_HALF_HUNGER_TEXTURE = WitchsBlights.id("hud/blood_half_hunger");
	@Unique private static final Identifier BLOOD_FULL_HUNGER_TEXTURE = WitchsBlights.id("hud/blood_full_hunger");
	@Unique private static final Identifier BLOOD_EMPTY_TEXTURE = WitchsBlights.id("hud/blood_empty");
	@Unique private static final Identifier BLOOD_HALF_TEXTURE = WitchsBlights.id("hud/blood_half");
	@Unique private static final Identifier BLOOD_FULL_TEXTURE = WitchsBlights.id("hud/blood_full");

	@ModifyExpressionValue(method = "renderFood", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/hud/InGameHud;FOOD_EMPTY_HUNGER_TEXTURE:Lnet/minecraft/util/Identifier;"))
	private Identifier bloodEmptyHungerTexture(Identifier original, DrawContext context, PlayerEntity player, int top, int right) {
		TransformationComponent component = player.getComponent(ModComponents.TRANSFORMATION);
		return component.getTransformation().isIn(ModTags.DRINKS_BLOOD) ? BLOOD_EMPTY_HUNGER_TEXTURE : original;
	}

	@ModifyExpressionValue(method = "renderFood", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/hud/InGameHud;FOOD_HALF_HUNGER_TEXTURE:Lnet/minecraft/util/Identifier;"))
	private Identifier bloodHalfHungerTexture(Identifier original, DrawContext context, PlayerEntity player, int top, int right) {
		TransformationComponent component = player.getComponent(ModComponents.TRANSFORMATION);
		return component.getTransformation().isIn(ModTags.DRINKS_BLOOD) ? BLOOD_HALF_HUNGER_TEXTURE : original;
	}

	@ModifyExpressionValue(method = "renderFood", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/hud/InGameHud;FOOD_FULL_HUNGER_TEXTURE:Lnet/minecraft/util/Identifier;"))
	private Identifier bloodFullHungerTexture(Identifier original, DrawContext context, PlayerEntity player, int top, int right) {
		TransformationComponent component = player.getComponent(ModComponents.TRANSFORMATION);
		return component.getTransformation().isIn(ModTags.DRINKS_BLOOD) ? BLOOD_FULL_HUNGER_TEXTURE : original;
	}

	@ModifyExpressionValue(method = "renderFood", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/hud/InGameHud;FOOD_EMPTY_TEXTURE:Lnet/minecraft/util/Identifier;"))
	private Identifier bloodEmptyTexture(Identifier original, DrawContext context, PlayerEntity player, int top, int right) {
		TransformationComponent component = player.getComponent(ModComponents.TRANSFORMATION);
		return component.getTransformation().isIn(ModTags.DRINKS_BLOOD) ? BLOOD_EMPTY_TEXTURE : original;
	}

	@ModifyExpressionValue(method = "renderFood", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/hud/InGameHud;FOOD_HALF_TEXTURE:Lnet/minecraft/util/Identifier;"))
	private Identifier bloodHalfTexture(Identifier original, DrawContext context, PlayerEntity player, int top, int right) {
		TransformationComponent component = player.getComponent(ModComponents.TRANSFORMATION);
		return component.getTransformation().isIn(ModTags.DRINKS_BLOOD) ? BLOOD_HALF_TEXTURE : original;
	}

	@ModifyExpressionValue(method = "renderFood", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/hud/InGameHud;FOOD_FULL_TEXTURE:Lnet/minecraft/util/Identifier;"))
	private Identifier bloodFullTexture(Identifier original, DrawContext context, PlayerEntity player, int top, int right) {
		TransformationComponent component = player.getComponent(ModComponents.TRANSFORMATION);
		return component.getTransformation().isIn(ModTags.DRINKS_BLOOD) ? BLOOD_FULL_TEXTURE : original;
	}
}
