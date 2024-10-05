package dev.cammiescorner.common.registries;

import dev.cammiescorner.WitchsBlights;
import dev.cammiescorner.api.Transformation;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;

public class ModTransformations {
	public static final RegistryHandler<Transformation> TRANSFORMATIONS = RegistryHandler.create(WitchsBlights.TRANSFORMATIONS_KEY, WitchsBlights.MOD_ID);

	public static final RegistrySupplier<Transformation> NONE = TRANSFORMATIONS.register("none", () -> new Transformation(null, null, null, 0, 0, 0));
	public static final RegistrySupplier<Transformation> VAMPIRE = TRANSFORMATIONS.register("vampire", () -> new Transformation(ModPotions.SANGUINE_BLIGHT.holder(), ModEntities.VAMPIRE_BEAST.get(), ModTags.VAMPIRE_BEAST_TARGETS, 400, 0.25, 0.5));
}
