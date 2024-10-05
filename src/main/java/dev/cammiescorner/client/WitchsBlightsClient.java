package dev.cammiescorner.client;

import dev.cammiescorner.client.models.VampireBeastEntityModel;
import dev.cammiescorner.client.renderers.VampireBeastEntityRenderer;
import dev.cammiescorner.common.registries.ModBlocks;
import dev.cammiescorner.common.registries.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

public class WitchsBlightsClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.MISTLETOE.get(), ModBlocks.BUNDLED_MISTLETOE.get());
//		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getDefaultColor(), ModBlocks.MISTLETOE.get());

		EntityModelLayerRegistry.registerModelLayer(VampireBeastEntityModel.MODEL_LAYER, VampireBeastEntityModel::getTexturedModelData);
		EntityRendererRegistry.register(ModEntities.VAMPIRE_BEAST.get(), VampireBeastEntityRenderer::new);
	}
}
