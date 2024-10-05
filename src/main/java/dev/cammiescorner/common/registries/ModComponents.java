package dev.cammiescorner.common.registries;

import dev.cammiescorner.WitchsBlights;
import dev.cammiescorner.common.components.*;
import dev.cammiescorner.common.entities.VampireBeastEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

public class ModComponents implements EntityComponentInitializer {
	public static final ComponentKey<VisibleToSupernaturalComponent> VISIBLE_TO_SUPERNATURAL = createComponent("visible_to_supernatural", VisibleToSupernaturalComponent.class);
	public static final ComponentKey<IncurableEffectsComponent> INCURABLE_EFFECTS = createComponent("incurable_effects", IncurableEffectsComponent.class);
	public static final ComponentKey<BloodComponent> BLOOD = createComponent("blood", BloodComponent.class);
	public static final ComponentKey<BeastMovementComponent> SPECIAL_BEAST_MOVEMENT = createComponent("special_beast_movement", BeastMovementComponent.class);
	public static final ComponentKey<TransformationComponent> TRANSFORMATION = createComponent("transformation", TransformationComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.beginRegistration(LivingEntity.class, VISIBLE_TO_SUPERNATURAL).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(VisibleToSupernaturalComponent::new);
		registry.registerForPlayers(INCURABLE_EFFECTS, IncurableEffectsComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
		registry.beginRegistration(PlayerEntity.class, BLOOD).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(BloodComponent::new);
		registry.beginRegistration(MerchantEntity.class, BLOOD).end(BloodComponent::new);
		registry.beginRegistration(IllagerEntity.class, BLOOD).end(BloodComponent::new);
		registry.beginRegistration(VampireBeastEntity.class, SPECIAL_BEAST_MOVEMENT).end(BeastMovementComponent::new);
		registry.registerForPlayers(TRANSFORMATION, TransformationComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
	}

	private static <T extends Component> ComponentKey<T> createComponent(String name, Class<T> component) {
		return ComponentRegistry.getOrCreate(WitchsBlights.id(name), component);
	}
}
