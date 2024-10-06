package dev.cammiescorner.api;

import dev.cammiescorner.common.components.TransformationComponent;
import dev.cammiescorner.common.registries.ModComponents;
import dev.cammiescorner.common.registries.ModTransformations;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class Transformation {
	private final RegistryEntry<Potion> curse;
	private final EntityType<? extends HostileEntity> beast;
	private final TagKey<EntityType<?>> targets;
	private final int baseUrgingTicks;
	private final double baseStageUrgingModifier;
	private final double baseBabyUrgingModifier;

	public Transformation(RegistryEntry<Potion> curse, EntityType<? extends HostileEntity> beast, TagKey<EntityType<?>> targets, int baseUrgingTicks, double baseStageUrgingModifier, double baseBabyUrgingModifier) {
		this.curse = curse;
		this.beast = beast;
		this.targets = targets;
		this.baseUrgingTicks = baseUrgingTicks;
		this.baseStageUrgingModifier = baseStageUrgingModifier;
		this.baseBabyUrgingModifier = baseBabyUrgingModifier;
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
		HostileEntity beast = getBeast().create(world);
		TransformationComponent component = player.getComponent(ModComponents.TRANSFORMATION);

		if(beast != null) {
			beast.refreshPositionAndAngles(player.getPos(), player.getHeadYaw(), player.getPitch());
			beast.setTarget(target);
			world.spawnEntity(beast);


			player.setCameraEntity(beast);
			component.setNoTargetTimer(100);
			component.setTransformed(true);
		}
	}

	public void untransform(ServerPlayerEntity player) {
		TransformationComponent component = player.getComponent(ModComponents.TRANSFORMATION);
		component.setTransformed(false);
		component.stopUrging();

		if(!player.equals(player.getCameraEntity()))
			player.getCameraEntity().remove(Entity.RemovalReason.DISCARDED);
	}

	public RegistryEntry<Potion> getCurse() {
		return curse;
	}

	public EntityType<? extends HostileEntity> getBeast() {
		return beast;
	}

	public TagKey<EntityType<?>> getTargets() {
		return targets;
	}
}
