package dev.cammiescorner.witchsblights.common.registries;

import dev.cammiescorner.witchsblights.WitchsBlights;
import dev.cammiescorner.witchsblights.common.Utils;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.potion.Potion;
import net.minecraft.registry.RegistryKeys;

public class ModPotions {
	public static final RegistryHandler<Potion> POTIONS = RegistryHandler.create(RegistryKeys.POTION, WitchsBlights.MOD_ID);

	public static final RegistrySupplier<Potion> SANGUINE_BLIGHT = POTIONS.register("sanguine_blight", () -> new Potion(Utils.SANGUINE_BLIGHT_I));
	public static final RegistrySupplier<Potion> STRONG_SANGUINE_BLIGHT = POTIONS.register("strong_sanguine_blight", () -> new Potion(Utils.SANGUINE_BLIGHT_II));
	public static final RegistrySupplier<Potion> STRONGEST_SANGUINE_BLIGHT = POTIONS.register("strongest_sanguine_blight", () -> new Potion(Utils.SANGUINE_BLIGHT_III));
	public static final RegistrySupplier<Potion> CURSED_CLAWS = POTIONS.register("cursed_claws", () -> new Potion(Utils.CURSED_CLAWS_I));
	public static final RegistrySupplier<Potion> STRONG_CURSED_CLAWS = POTIONS.register("strong_cursed_claws", () -> new Potion(Utils.CURSED_CLAWS_II));
	public static final RegistrySupplier<Potion> STRONGEST_CURSED_CLAWS = POTIONS.register("strongest_cursed_claws", () -> new Potion(Utils.CURSED_CLAWS_III));
}
