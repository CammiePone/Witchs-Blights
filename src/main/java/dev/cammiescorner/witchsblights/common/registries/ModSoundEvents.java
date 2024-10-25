package dev.cammiescorner.witchsblights.common.registries;

import dev.cammiescorner.witchsblights.WitchsBlights;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvent;

public class ModSoundEvents {
	public static final RegistryHandler<SoundEvent> SOUND_EVENTS = RegistryHandler.create(RegistryKeys.SOUND_EVENT, WitchsBlights.MOD_ID);

	public static final RegistrySupplier<SoundEvent> BEAST_SCRATCH = register("entity.beasts.scratch");
	public static final RegistrySupplier<SoundEvent> BEAST_STEP = register("entity.beasts.step");
	public static final RegistrySupplier<SoundEvent> VAMPIRE_ATTACK = register("entity.vampire_beast.attack");
	public static final RegistrySupplier<SoundEvent> VAMPIRE_DEATH = register("entity.vampire_beast.death");
	public static final RegistrySupplier<SoundEvent> VAMPIRE_HURT = register("entity.vampire_beast.hurt");
	public static final RegistrySupplier<SoundEvent> VAMPIRE_IDLE = register("entity.vampire_beast.idle");
	public static final RegistrySupplier<SoundEvent> VAMPIRE_SCREAM = register("entity.vampire_beast.scream");
	public static final RegistrySupplier<SoundEvent> VAMPIRE_DRINK_BLOOD = register("entity.vampire_beast.drink_blood");
	public static final RegistrySupplier<SoundEvent> VAMPIRE_WINGS = register("entity.vampire_beast.wings");
	public static final RegistrySupplier<SoundEvent> WEREWOLF_ATTACK = register("entity.werewolf.attack");
	public static final RegistrySupplier<SoundEvent> WEREWOLF_DEATH = register("entity.werewolf.death");
	public static final RegistrySupplier<SoundEvent> WEREWOLF_HURT = register("entity.werewolf.hurt");
	public static final RegistrySupplier<SoundEvent> WEREWOLF_IDLE = register("entity.werewolf.idle");
	public static final RegistrySupplier<SoundEvent> WEREWOLF_SCREAM = register("entity.werewolf.scream");

	private static RegistrySupplier<SoundEvent> register(String name) {
		return SOUND_EVENTS.register(name, () -> SoundEvent.of(WitchsBlights.id(name)));
	}
}
