package dev.cammiescorner.common.registries;

import dev.cammiescorner.WitchsBlights;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.RegistryKeys;

public class ModParticles {
	public static final RegistryHandler<ParticleType<?>> PARTICLE_TYPES = RegistryHandler.create(RegistryKeys.PARTICLE_TYPE, WitchsBlights.MOD_ID);

	public static final RegistrySupplier<SimpleParticleType> BITING = PARTICLE_TYPES.register("biting", () -> FabricParticleTypes.simple(true));
	public static final RegistrySupplier<SimpleParticleType> BLOOD = PARTICLE_TYPES.register("blood", () -> FabricParticleTypes.simple(true));
	public static final RegistrySupplier<SimpleParticleType> BAT = PARTICLE_TYPES.register("bat", () -> FabricParticleTypes.simple(true));
	public static final RegistrySupplier<SimpleParticleType> CROW = PARTICLE_TYPES.register("crow", () -> FabricParticleTypes.simple(true));
	public static final RegistrySupplier<SimpleParticleType> RAT = PARTICLE_TYPES.register("rat", () -> FabricParticleTypes.simple(true));
	public static final RegistrySupplier<SimpleParticleType> SENSES = PARTICLE_TYPES.register("senses", () -> FabricParticleTypes.simple(true));
}
