package dev.cammiescorner.witchsblights.common.registries;

import dev.cammiescorner.witchsblights.WitchsBlights;
import dev.cammiescorner.witchsblights.common.entities.VampireBeastEntity;
import dev.cammiescorner.witchsblights.common.entities.WerewolfBeastEntity;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.RegistryKeys;

public class ModEntities {
	public static final RegistryHandler<EntityType<?>> ENTITIES = RegistryHandler.create(RegistryKeys.ENTITY_TYPE, WitchsBlights.MOD_ID);

	public static final RegistrySupplier<EntityType<VampireBeastEntity>> VAMPIRE_BEAST = ENTITIES.register("vampire_beast", () -> FabricEntityType.Builder.createMob(VampireBeastEntity::new, SpawnGroup.MONSTER, living -> living.defaultAttributes(VampireBeastEntity::createVampireBeastAttributes)).dimensions(0.8f, 2.7f).eyeHeight(2.35f).maxTrackingRange(100).build());
	public static final RegistrySupplier<EntityType<WerewolfBeastEntity>> WEREWOLF_BEAST = ENTITIES.register("werewolf_beast", () -> FabricEntityType.Builder.createMob(WerewolfBeastEntity::new, SpawnGroup.MONSTER, living -> living.defaultAttributes(WerewolfBeastEntity::createWerewolfBeastAttributes)).dimensions(0.8f, 2.7f).eyeHeight(2.35f).maxTrackingRange(100).build());
}
