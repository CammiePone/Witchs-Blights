package dev.cammiescorner.common.entities;

import dev.cammiescorner.common.Utils;
import dev.cammiescorner.common.registries.ModSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class BeastEntity extends HostileEntity {
	public static final TrackedData<Boolean> HUNTING = DataTracker.registerData(BeastEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	public static final TrackedData<Integer> ATTACK_COOLDOWN = DataTracker.registerData(BeastEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private UUID ownerId = Utils.NIL_UUID;

	public BeastEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}

	public static DefaultAttributeContainer.Builder createBeastAttributes() {
		return HostileEntity.createHostileAttributes()
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 100)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.15)
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 40);
	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
		builder.add(HUNTING, false);
		builder.add(ATTACK_COOLDOWN, 0);
	}

	@Override
	protected void mobTick() {
		super.mobTick();

		setHunting(getTarget() != null && getTarget().isAlive());
	}

	@Override
	protected float getBaseMovementSpeedMultiplier() {
		return getTarget() != null && getTarget().isSprinting() ? 2f : 1f;
	}

	@Override
	public float getMovementSpeed() {
		return super.getMovementSpeed() * getBaseMovementSpeedMultiplier();
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		super.playStepSound(pos, state);
		BlockSoundGroup blockSoundGroup = state.getSoundGroup();
		playSound(getStepSound(), blockSoundGroup.getVolume() * 0.15f, blockSoundGroup.getPitch());
	}

	@Override
	protected void playAttackSound() {
		resetSoundDelay();
		playSound(getAttackSound(), 0.5f, getSoundPitch());
	}

	@Override
	protected void playHurtSound(DamageSource damageSource) {
		super.playHurtSound(damageSource);
	}

	@Override
	public void setTarget(@Nullable LivingEntity target) {
		super.setTarget(target);

		if(target != null && !target.equals(getTarget()))
			playSound(getFoundTargetSound());
	}

	@Override
	public boolean tryAttack(Entity target) {
		boolean succeeds = super.tryAttack(target);

		if(target instanceof LivingEntity livingTarget && succeeds) {
			addExtraAttackEffects(livingTarget);
			livingTarget.playSound(ModSoundEvents.BEAST_SCRATCH.get());
		}

		return succeeds;
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putBoolean("IsHunting", isHunting());
		nbt.putInt("AttackCooldown", getAttackCooldown());
		nbt.putUuid("Owner", ownerId);
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		setHunting(nbt.getBoolean("IsHunting"));
		setAttackCooldown(nbt.getInt("AttackCooldown"));
		ownerId = nbt.getUuid("Owner");
	}

	public ServerPlayerEntity getOwner() {
		return getWorld() instanceof ServerWorld serverWorld ? (ServerPlayerEntity) serverWorld.getPlayerByUuid(ownerId) : null;
	}

	public void setOwner(PlayerEntity player) {
		ownerId = player.getUuid();
	}

	public abstract void addExtraAttackEffects(LivingEntity target);

	public abstract SoundEvent getFoundTargetSound();

	public abstract SoundEvent getAttackSound();

	public SoundEvent getStepSound() {
		return ModSoundEvents.BEAST_STEP.get();
	}

	public boolean isHunting() {
		return dataTracker.get(HUNTING);
	}

	public void setHunting(boolean isHunting) {
		dataTracker.set(HUNTING, isHunting);
	}

	public int getAttackCooldown() {
		return dataTracker.get(ATTACK_COOLDOWN);
	}

	public void setAttackCooldown(int cooldown) {
		dataTracker.set(ATTACK_COOLDOWN, cooldown);
	}
}
