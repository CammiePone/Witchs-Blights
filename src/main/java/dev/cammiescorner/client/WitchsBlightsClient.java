package dev.cammiescorner.client;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.cammiescorner.ModConfig;
import dev.cammiescorner.WitchsBlights;
import dev.cammiescorner.client.models.VampireBeastEntityModel;
import dev.cammiescorner.client.models.WerewolfBeastEntityModel;
import dev.cammiescorner.client.renderers.VampireBeastEntityRenderer;
import dev.cammiescorner.client.renderers.WerewolfBeastEntityRenderer;
import dev.cammiescorner.common.components.TransformationComponent;
import dev.cammiescorner.common.registries.ModBlocks;
import dev.cammiescorner.common.registries.ModComponents;
import dev.cammiescorner.common.registries.ModEntities;
import dev.cammiescorner.common.registries.ModParticles;
import dev.upcraft.sparkweave.api.client.event.RegisterParticleFactoriesEvent;
import dev.upcraft.sparkweave.api.entrypoint.ClientEntryPoint;
import dev.upcraft.sparkweave.api.platform.ModContainer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WitchsBlightsClient implements ClientEntryPoint {
	private static final MinecraftClient client = MinecraftClient.getInstance();
	public static final Identifier TRANSFORMED_WAKE_ONE = WitchsBlights.id("textures/gui/hud/transformed_wake_one.png");
	public static final Identifier TRANSFORMED_WAKE_TWO = WitchsBlights.id("textures/gui/hud/transformed_wake_two.png");
	public static final Identifier TRANSFORMED_BLINK = WitchsBlights.id("textures/gui/hud/transformed_blink.png");
	public static final Identifier URGING = WitchsBlights.id("textures/gui/hud/urging_overlay.png");

	@Override
	public void onInitializeClient(ModContainer mod) {
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.MISTLETOE.get(), ModBlocks.BUNDLED_MISTLETOE.get());
//		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getDefaultColor(), ModBlocks.MISTLETOE.get());

		EntityModelLayerRegistry.registerModelLayer(VampireBeastEntityModel.MODEL_LAYER, VampireBeastEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(WerewolfBeastEntityModel.MODEL_LAYER, WerewolfBeastEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(ModEntities.VAMPIRE_BEAST.get(), VampireBeastEntityRenderer::new);
		EntityRendererRegistry.register(ModEntities.WEREWOLF_BEAST.get(), WerewolfBeastEntityRenderer::new);

		RegisterParticleFactoriesEvent.EVENT.register(event -> {
			event.registerSprite(ModParticles.BLOOD, (parameters, world, x, y, z, velocityX, velocityY, velocityZ) -> new SpriteBillboardParticle(world, x, y, z, velocityX, velocityY, velocityZ) {
				@Override
				public void tick() {
					gravityStrength = 1f;
					super.tick();
				}

				@Override
				public ParticleTextureSheet getType() {
					return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
				}
			});

			event.registerSpriteSet(ModParticles.BITING, spriteProvider -> (parameters, world, x, y, z, velX, velY, velZ) -> {
				SpriteBillboardParticle particle = new SpriteBillboardParticle(world, x, y, z, velX, velY, velZ) {
					{
						this.velocityY = velY;
						this.velocityX = velX;
						this.velocityZ = velZ;
					}

					@Override
					public void tick() {
						setSpriteForAge(spriteProvider);
						super.tick();
					}

					@Override
					public ParticleTextureSheet getType() {
						return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
					}
				};

				particle.setMaxAge(10);
				particle.setSpriteForAge(spriteProvider);

				return particle;
			});
		});

		HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
			PlayerEntity player = client.player;
			World world = client.world;
			float tickDelta = tickCounter.getTickDelta(false);
			float urgingLookStrength = ModConfig.AllBeasts.urgingLookStrength;

			if(player != null && world != null) {
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

		ClientTickEvents.END_WORLD_TICK.register(world -> {
			PlayerEntity player = client.player;

			if(!client.isPaused() && player != null) {
				TransformationComponent component = player.getComponent(ModComponents.TRANSFORMATION);
				LivingEntity target = component.getTarget();

				if(target != null && !component.isTransformed() && component.getUrgingProgress() > 0 && player.age % 15 == 0) {
					Vec3d directionToPlayer = player.getPos().subtract(target.getPos()).normalize().multiply(target.getWidth());
					Vec3d particlePos = target.getPos().add(0, target.getHeight() * 0.75, 0).add(directionToPlayer.x, 0, directionToPlayer.z);

					world.addParticle(ModParticles.BITING.get(), particlePos.x, particlePos.y, particlePos.z, 0, 0, 0);
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
