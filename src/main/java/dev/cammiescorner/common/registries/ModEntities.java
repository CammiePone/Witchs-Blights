package dev.cammiescorner.common.registries;

import dev.cammiescorner.WitchsBlights;
import dev.cammiescorner.common.entities.VampireBeastEntity;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.RegistryKeys;

public class ModEntities {
	public static final RegistryHandler<EntityType<?>> ENTITIES = RegistryHandler.create(RegistryKeys.ENTITY_TYPE, WitchsBlights.MOD_ID);

	public static final RegistrySupplier<EntityType<VampireBeastEntity>> VAMPIRE_BEAST = ENTITIES.register("vampire_beast", () -> FabricEntityType.Builder.createMob(VampireBeastEntity::new, SpawnGroup.MONSTER, living -> living.defaultAttributes(VampireBeastEntity::createVampireBeastAttributes)).dimensions(0.6f, 2.7f).eyeHeight(2.35f).maxTrackingRange(100).build());
}
