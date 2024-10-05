package dev.cammiescorner.common.registries;

import dev.cammiescorner.WitchsBlights;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;

public class ModDamageTypes {
	public static final RegistryKey<DamageType> BLOOD_DRAINED = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, WitchsBlights.id("blood_drained"));

	public static DamageSource bloodDrained(Entity source) {
		return create(source.getWorld(), BLOOD_DRAINED, source);
	}

	public static DamageSource create(World world, RegistryKey<DamageType> key, Entity attacker) {
		return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key), attacker);
	}
}
