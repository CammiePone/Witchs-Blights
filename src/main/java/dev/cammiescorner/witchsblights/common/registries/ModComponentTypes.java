package dev.cammiescorner.witchsblights.common.registries;

import dev.cammiescorner.witchsblights.WitchsBlights;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Uuids;

import java.util.UUID;

public class ModComponentTypes {
	public static final RegistryHandler<ComponentType<?>> COMPONENTS = RegistryHandler.create(RegistryKeys.DATA_COMPONENT_TYPE, WitchsBlights.MOD_ID);

	public static final RegistrySupplier<ComponentType<UUID>> TARGETED_ENTITY = COMPONENTS.register("targeted_entity", () -> ComponentType.<UUID>builder().codec(Uuids.CODEC).packetCodec(Uuids.PACKET_CODEC).build());
}
