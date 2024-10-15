package dev.cammiescorner.client.renderers;

import dev.cammiescorner.WitchsBlights;
import dev.cammiescorner.client.models.VampireBeastEntityModel;
import dev.cammiescorner.common.entities.VampireBeastEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class VampireBeastEntityRenderer extends MobEntityRenderer<VampireBeastEntity, VampireBeastEntityModel> {
	private static final Identifier VAMPIRE_TEXTURE = WitchsBlights.id("textures/entity/vampire_beast.png");

	public VampireBeastEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new VampireBeastEntityModel(MinecraftClient.getInstance().getEntityModelLoader().getModelPart(VampireBeastEntityModel.MODEL_LAYER)), 0.65f);
	}

	@Override
	public void render(VampireBeastEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		model.sneaking = livingEntity.isSneaking();
		model.flying = livingEntity.isFlying();
		model.hunting = livingEntity.isHunting();
		model.attackCooldownProgress = 1 - livingEntity.getAttackCooldown() / 20f;

		super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
	}

	@Override
	public Identifier getTexture(VampireBeastEntity entity) {
		return VAMPIRE_TEXTURE;
	}
}
