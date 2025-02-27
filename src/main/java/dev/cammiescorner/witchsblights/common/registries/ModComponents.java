package dev.cammiescorner.witchsblights.common.registries;

import dev.cammiescorner.witchsblights.WitchsBlights;
import dev.cammiescorner.witchsblights.common.components.*;
import dev.cammiescorner.witchsblights.common.components.client.ClientTransformationComponent;
import dev.cammiescorner.witchsblights.common.entities.VampireBeastEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

public class ModComponents implements EntityComponentInitializer {
	public static final ComponentKey<VisibleToSupernaturalComponent> VISIBLE_TO_SUPERNATURAL = createComponent("visible_to_supernatural", VisibleToSupernaturalComponent.class);
	public static final ComponentKey<RespawnableEffectsComponent> RESPAWNABLE_EFFECTS = createComponent("respawnable_effects", RespawnableEffectsComponent.class);
	public static final ComponentKey<BloodComponent> BLOOD = createComponent("blood", BloodComponent.class);
	public static final ComponentKey<BeastMovementComponent> SPECIAL_BEAST_MOVEMENT = createComponent("special_beast_movement", BeastMovementComponent.class);
	public static final ComponentKey<TransformationComponent> TRANSFORMATION = createComponent("transformation", TransformationComponent.class);
	public static final ComponentKey<CureCurseComponent> CURE_CURSE = createComponent("cure_curse", CureCurseComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.beginRegistration(LivingEntity.class, VISIBLE_TO_SUPERNATURAL).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(VisibleToSupernaturalComponent::new);
		registry.registerForPlayers(RESPAWNABLE_EFFECTS, RespawnableEffectsComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
		registry.beginRegistration(PlayerEntity.class, BLOOD).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(BloodComponent::new);
		registry.beginRegistration(MerchantEntity.class, BLOOD).end(BloodComponent::new);
		registry.beginRegistration(IllagerEntity.class, BLOOD).end(BloodComponent::new);
		registry.beginRegistration(VampireBeastEntity.class, SPECIAL_BEAST_MOVEMENT).end(BeastMovementComponent::new);
		registry.registerForPlayers(TRANSFORMATION, player -> player instanceof ServerPlayerEntity ? new TransformationComponent(player) : new ClientTransformationComponent(player), RespawnCopyStrategy.ALWAYS_COPY);
		registry.registerForPlayers(CURE_CURSE, CureCurseComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
	}

	private static <T extends Component> ComponentKey<T> createComponent(String name, Class<T> component) {
		return ComponentRegistry.getOrCreate(WitchsBlights.id(name), component);
	}
}
