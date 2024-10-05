package dev.cammiescorner.common.registries;

import dev.cammiescorner.WitchsBlights;
import dev.cammiescorner.common.world.features.MistletoeFeature;
import dev.cammiescorner.common.world.features.configs.MistletoeFeatureConfig;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.feature.Feature;

public class ModWorldFeatures {
	public static final RegistryHandler<Feature<?>> FEATURES = RegistryHandler.create(RegistryKeys.FEATURE, WitchsBlights.MOD_ID);

	public static final RegistrySupplier<Feature<MistletoeFeatureConfig>> MISTLETOE = FEATURES.register("mistletoe", MistletoeFeature::new);
}
