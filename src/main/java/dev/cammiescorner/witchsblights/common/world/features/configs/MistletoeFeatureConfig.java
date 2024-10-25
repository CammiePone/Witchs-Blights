package dev.cammiescorner.witchsblights.common.world.features.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.gen.feature.FeatureConfig;

public record MistletoeFeatureConfig(TagKey<Block> validBlocks) implements FeatureConfig {
	public static final Codec<MistletoeFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(TagKey.unprefixedCodec(RegistryKeys.BLOCK).fieldOf("valid_blocks").forGetter(MistletoeFeatureConfig::validBlocks)).apply(instance, MistletoeFeatureConfig::new));
}
