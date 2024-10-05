package dev.cammiescorner.common.registries;

import dev.cammiescorner.WitchsBlights;
import dev.cammiescorner.common.status_effects.CursedStatusEffect;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;

public class ModStatusEffects {
	public static final RegistryHandler<StatusEffect> STATUS_EFFECTS = RegistryHandler.create(RegistryKeys.STATUS_EFFECT, WitchsBlights.MOD_ID);

	public static final RegistrySupplier<StatusEffect> SANGUINE_BLIGHT = STATUS_EFFECTS.register("sanguine_blight", () -> new CursedStatusEffect(StatusEffectCategory.HARMFUL, 0x2b2a2a));
	public static final RegistrySupplier<StatusEffect> CURSED_CLAWS = STATUS_EFFECTS.register("cursed_claws", () -> new CursedStatusEffect(StatusEffectCategory.HARMFUL, 0xfff899));

	public static final RegistryEntry<StatusEffect> SANGUINE_BLIGHT_ENTRY = Registries.STATUS_EFFECT.getEntry(SANGUINE_BLIGHT.get());
	public static final RegistryEntry<StatusEffect> CURSED_CLAWS_ENTRY = Registries.STATUS_EFFECT.getEntry(CURSED_CLAWS.get());
}
