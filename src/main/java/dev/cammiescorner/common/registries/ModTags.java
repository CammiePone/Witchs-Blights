package dev.cammiescorner.common.registries;

import dev.cammiescorner.WitchsBlights;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModTags {
	public static final TagKey<Block> VALID_MISTLETOE_GEN_BLOCKS = TagKey.of(RegistryKeys.BLOCK, WitchsBlights.id("valid_mistletoe_gen_blocks"));
	public static final TagKey<EntityType<?>> VAMPIRE_BEAST_TARGETS = TagKey.of(RegistryKeys.ENTITY_TYPE, WitchsBlights.id("vampire_beast_targets"));
}
