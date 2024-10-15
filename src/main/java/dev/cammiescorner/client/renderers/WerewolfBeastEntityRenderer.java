package dev.cammiescorner.client.renderers;

import dev.cammiescorner.WitchsBlights;
import dev.cammiescorner.client.models.WerewolfBeastEntityModel;
import dev.cammiescorner.common.entities.WerewolfBeastEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class WerewolfBeastEntityRenderer extends MobEntityRenderer<WerewolfBeastEntity, WerewolfBeastEntityModel> {
	private static final Identifier WEREWOLF_TEXTURE = WitchsBlights.id("textures/entity/werewolf_beast.png");
	private static final Identifier WEREWOLF_SNOWY_TEXTURE = WitchsBlights.id("textures/entity/werewolf_beast_snowy.png");

	public WerewolfBeastEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new WerewolfBeastEntityModel(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(WerewolfBeastEntityModel.MODEL_LAYER)), 0.85f);
	}

	@Override
	public void render(WerewolfBeastEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		model.sneaking = livingEntity.isSneaking();
		model.climbing = livingEntity.isClimbing();
		model.hunting = livingEntity.isHunting();
		model.attackCooldownProgress = 1 - livingEntity.getAttackCooldown() / 20f;

		super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
	}

	@Override
	public Identifier getTexture(WerewolfBeastEntity entity) {
		return entity.isSnowy() ? WEREWOLF_SNOWY_TEXTURE : WEREWOLF_TEXTURE;
	}
}
