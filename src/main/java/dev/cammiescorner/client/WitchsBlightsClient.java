package dev.cammiescorner.client;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.cammiescorner.ModConfig;
import dev.cammiescorner.WitchsBlights;
import dev.cammiescorner.client.models.VampireBeastEntityModel;
import dev.cammiescorner.client.renderers.VampireBeastEntityRenderer;
import dev.cammiescorner.common.components.TransformationComponent;
import dev.cammiescorner.common.registries.ModBlocks;
import dev.cammiescorner.common.registries.ModComponents;
import dev.cammiescorner.common.registries.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class WitchsBlightsClient implements ClientModInitializer {
	private static final MinecraftClient client = MinecraftClient.getInstance();
	public static final Identifier TRANSFORMED_WAKE = WitchsBlights.id("textures/gui/hud/transformed_wake.png");
	public static final Identifier TRANSFORMED_BLINK = WitchsBlights.id("textures/gui/hud/transformed_blink.png");
	public static final Identifier URGING = WitchsBlights.id("textures/gui/hud/urging_overlay.png");

	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.MISTLETOE.get(), ModBlocks.BUNDLED_MISTLETOE.get());
//		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getDefaultColor(), ModBlocks.MISTLETOE.get());

		EntityModelLayerRegistry.registerModelLayer(VampireBeastEntityModel.MODEL_LAYER, VampireBeastEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(ModEntities.VAMPIRE_BEAST.get(), VampireBeastEntityRenderer::new);

		HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
			PlayerEntity player = client.player;
			float tickDelta = tickCounter.getTickDelta(false);
			float urgingLookStrength = ModConfig.AllBeasts.urgingLookStrength;

			if(player != null) {
				TransformationComponent component = player.getComponent(ModComponents.TRANSFORMATION);

				if(!component.isTransformed() && urgingLookStrength > 0) {
					LivingEntity target = component.getTarget();
					float urgingProgress = component.getUrgingProgress();

					if(target != null && target.isAlive()) {
						renderOverlay(drawContext, URGING, urgingProgress * 0.8f);

						if(!client.isPaused()) {
							Vec3d targetPos = target.getLerpedPos(tickDelta).add(0, target.getHeight() * 0.5, 0);
							Vec3d playerPos = player.getLerpedPos(tickDelta).add(0, player.getEyeHeight(player.getPose()), 0);
							Vec3d distance = targetPos.subtract(playerPos);
							double distanceXZ = Math.sqrt(distance.x * distance.x + distance.z * distance.z);
							float currentPitch = MathHelper.wrapDegrees(player.getPitch(tickDelta));
							float currentYaw = MathHelper.wrapDegrees(player.getYaw(tickDelta));
							float desiredPitch = MathHelper.wrapDegrees((float) (-(MathHelper.atan2(distance.y, distanceXZ) * (180 / Math.PI))));
							float desiredYaw = MathHelper.wrapDegrees((float) (MathHelper.atan2(distance.z, distance.x) * (180 / Math.PI)) - 90.0f);

							Vec2f rotationChange = new Vec2f(
									MathHelper.wrapDegrees(desiredPitch - currentPitch),
									MathHelper.wrapDegrees(desiredYaw - currentYaw)
							);

							Vec2f rotationStep = rotationChange.normalize().multiply(component.getUrgingProgress() * 10f * (MathHelper.clamp(rotationChange.length(), 0, 10) / 10f) * urgingLookStrength);

							player.setPitch(player.getPitch(tickDelta) + rotationStep.x);
							player.setYaw(player.getYaw(tickDelta) + rotationStep.y);
						}
					}
				}
			}
		});
	}

	public static void renderOverlay(DrawContext context, Identifier texture, float opacity) {
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.enableBlend();
		context.setShaderColor(1f, 1f, 1f, opacity);
		context.drawTexture(texture, 0, 0, -90, 0f, 0f, context.getScaledWindowWidth(), context.getScaledWindowHeight(), context.getScaledWindowWidth(), context.getScaledWindowHeight());
		RenderSystem.disableBlend();
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		context.setShaderColor(1f, 1f, 1f, 1f);
	}
}
