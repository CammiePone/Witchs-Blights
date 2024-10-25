package dev.cammiescorner.witchsblights.api;

import dev.cammiescorner.witchsblights.WitchsBlights;
import dev.cammiescorner.witchsblights.common.components.TransformationComponent;
import dev.cammiescorner.witchsblights.common.entities.BeastEntity;
import dev.cammiescorner.witchsblights.common.registries.ModComponents;
import dev.cammiescorner.witchsblights.common.registries.ModTransformations;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class Transformation {
	private final RegistryEntry<Potion> curse;
	private final EntityType<? extends BeastEntity> beast;
	private final TagKey<EntityType<?>> targets;
	private final int baseUrgingTicks;
	private final double baseStageUrgingModifier;
	private final double baseBabyUrgingModifier;
	private final Box urgingBox;

	public Transformation(RegistryEntry<Potion> curse, EntityType<? extends BeastEntity> beast, TagKey<EntityType<?>> targets, int baseUrgingTicks, double baseStageUrgingModifier, double baseBabyUrgingModifier, double radius) {
		this.curse = curse;
		this.beast = beast;
		this.targets = targets;
		this.baseUrgingTicks = baseUrgingTicks;
		this.baseStageUrgingModifier = baseStageUrgingModifier;
		this.baseBabyUrgingModifier = baseBabyUrgingModifier;
		this.urgingBox = new Box(-radius, -radius, -radius, radius, radius, radius);
	}

	public int getMaxUrgingTicks(PlayerEntity player, @NotNull LivingEntity target) {
		int stage = getStage(player);

		if(stage < 0)
			return 0;

		double stageModifier = 1 - (baseStageUrgingModifier * stage);
		double babyModifier = target.isBaby() ? baseBabyUrgingModifier * stageModifier : 1;

		return (int) (baseUrgingTicks * stageModifier * babyModifier);
	}

	public int getStage(PlayerEntity player) {
		if(equals(ModTransformations.NONE.get()))
			return -1;

		StatusEffectInstance effectInstance = player.getStatusEffect(curse.value().getEffects().getFirst().getEffectType());

		return effectInstance != null ? effectInstance.getAmplifier() : -1;
	}

	public boolean isAfflicted(PlayerEntity player) {
		if(equals(ModTransformations.NONE.get()))
			return false;

		return player.hasStatusEffect(curse.value().getEffects().getFirst().getEffectType());
	}

	public void transform(ServerPlayerEntity player, LivingEntity target) {
		World world = player.getWorld();
		BeastEntity beast = getBeast().create(world);
		TransformationComponent component = player.getComponent(ModComponents.TRANSFORMATION);

		if(beast != null) {
			beast.setTarget(target);
			beast.setOwner(player);
			beast.refreshPositionAndAngles(player.getPos(), player.getYaw(), player.getPitch());
			world.spawnEntity(beast);
			beast.refreshPositionAndAngles(player.getPos(), player.getYaw(), player.getPitch());

			for(EquipmentSlot slot : EquipmentSlot.values()) {
				ItemStack stack = player.getEquippedStack(slot);

				if(!stack.isEmpty()) {
					player.dropItem(stack, true, false);
					player.equipStack(slot, ItemStack.EMPTY);
				}
			}

			player.setInvulnerable(true);
			player.setCameraEntity(beast);
			component.setNoTargetTimer(100);
			component.setInitiallyTransformedTime(world.getTime());
			component.setTransformed(true);
			world.sendEntityStatus(player, EntityStatuses.ADD_DEATH_PARTICLES);
		}
	}

	public void untransform(ServerPlayerEntity player) {
		TransformationComponent component = player.getComponent(ModComponents.TRANSFORMATION);

		component.setTransformed(false);
		component.stopUrging();
		player.setInvulnerable(false);

		if(player.getCameraEntity() instanceof BeastEntity beast) {
			Vec3d position = beast.getPos();
			float yaw = beast.getYaw();
			float pitch = beast.getPitch();

			player.getCameraEntity().remove(Entity.RemovalReason.DISCARDED);
			player.getWorld().sendEntityStatus(player, EntityStatuses.ADD_DEATH_PARTICLES);
			player.refreshPositionAndAngles(position, yaw, pitch);
		}
	}

	public RegistryEntry<Potion> getCurse() {
		return curse;
	}

	public EntityType<? extends BeastEntity> getBeast() {
		return beast;
	}

	public TagKey<EntityType<?>> getTargets() {
		return targets;
	}

	public boolean isIn(TagKey<Transformation> tag) {
		return WitchsBlights.TRANSFORMATIONS.getEntry(this).isIn(tag);
	}

	public Box getBoxForUrging() {
		return urgingBox;
	}
}
