package dev.cammiescorner.witchsblights.common.registries;

import dev.cammiescorner.witchsblights.WitchsBlights;
import dev.cammiescorner.witchsblights.common.blocks.MistletoeBlock;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;

public class ModBlocks {
	public static final RegistryHandler<Block> BLOCKS = RegistryHandler.create(RegistryKeys.BLOCK, WitchsBlights.MOD_ID);

	public static final RegistrySupplier<Block> MISTLETOE = BLOCKS.register("mistletoe", () -> new MistletoeBlock(AbstractBlock.Settings.create().ticksRandomly()));
	public static final RegistrySupplier<Block> BUNDLED_MISTLETOE = BLOCKS.register("bundled_mistletoe", () -> new MistletoeBlock(AbstractBlock.Settings.create()));
}
