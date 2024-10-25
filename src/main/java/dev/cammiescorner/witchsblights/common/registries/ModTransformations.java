package dev.cammiescorner.witchsblights.common.registries;

import dev.cammiescorner.witchsblights.WitchsBlights;
import dev.cammiescorner.witchsblights.ModConfig;
import dev.cammiescorner.witchsblights.api.Transformation;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;

public class ModTransformations {
	public static final RegistryHandler<Transformation> TRANSFORMATIONS = RegistryHandler.create(WitchsBlights.TRANSFORMATIONS_KEY, WitchsBlights.MOD_ID);

	public static final RegistrySupplier<Transformation> NONE = TRANSFORMATIONS.register("none", () -> new Transformation(null, null, null, 0, 0, 0, 0));
	public static final RegistrySupplier<Transformation> VAMPIRE = TRANSFORMATIONS.register("vampire", () -> new Transformation(ModPotions.SANGUINE_BLIGHT.holder(), ModEntities.VAMPIRE_BEAST.get(), ModTags.VAMPIRE_BEAST_TARGETS, ModConfig.VampireBeast.baseVampireUrgingTicks, ModConfig.VampireBeast.baseVampireStageUrgingModifier, ModConfig.VampireBeast.baseVampireBabyUrgingModifier, ModConfig.VampireBeast.vampireUrgingRange));
	public static final RegistrySupplier<Transformation> WEREWOLF = TRANSFORMATIONS.register("werewolf", () -> new Transformation(ModPotions.CURSED_CLAWS.holder(), ModEntities.WEREWOLF_BEAST.get(), ModTags.WEREWOLF_BEAST_TARGETS, ModConfig.Werewolf.baseWerewolfUrgingTicks, ModConfig.Werewolf.baseWerewolfStageUrgingModifier, ModConfig.Werewolf.baseWerewolfBabyUrgingModifier, ModConfig.Werewolf.werewolfUrgingRange));
}
