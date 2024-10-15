package dev.cammiescorner.common.registries;

import dev.cammiescorner.WitchsBlights;
import dev.cammiescorner.api.Transformation;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModTags {
	public static final TagKey<Block> VALID_MISTLETOE_GEN_BLOCKS = TagKey.of(RegistryKeys.BLOCK, WitchsBlights.id("valid_mistletoe_gen_blocks"));
	public static final TagKey<EntityType<?>> VAMPIRE_BEAST_TARGETS = TagKey.of(RegistryKeys.ENTITY_TYPE, WitchsBlights.id("vampire_beast_targets"));
	public static final TagKey<EntityType<?>> WEREWOLF_BEAST_TARGETS = TagKey.of(RegistryKeys.ENTITY_TYPE, WitchsBlights.id("werewolf_beast_targets"));
	public static final TagKey<Transformation> NOCTURNAL = TagKey.of(WitchsBlights.TRANSFORMATIONS_KEY, WitchsBlights.id("nocturnal"));
	public static final TagKey<Transformation> DRINKS_BLOOD = TagKey.of(WitchsBlights.TRANSFORMATIONS_KEY, WitchsBlights.id("drinks_blood"));
}
