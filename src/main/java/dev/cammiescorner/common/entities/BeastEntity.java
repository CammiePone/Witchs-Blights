package dev.cammiescorner.common.entities;

import com.google.common.collect.Iterators;
import dev.cammiescorner.common.Utils;
import dev.cammiescorner.common.registries.ModComponents;
import dev.cammiescorner.common.registries.ModParticles;
import dev.cammiescorner.common.registries.ModSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
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
	public boolean isDead() {
		boolean bl = super.isDead();

		if(bl && getOwner() != null)
			getOwner().kill();

		return bl;
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

		if(target instanceof LivingEntity livingTarget && succeeds && getWorld() instanceof ServerWorld world) {
			addExtraAttackEffects(livingTarget);
			livingTarget.playSound(ModSoundEvents.BEAST_SCRATCH.get());

			for(int count = 0; count < random.nextBetween(8, 16); count++)
				world.spawnParticles(ModParticles.BLOOD.get(), target.getX(), target.getY() + (target.getHeight() * 0.5f) + random.nextFloat(), target.getZ(), 0, 0, 0, 0, 0);
		}

		return succeeds;
	}

	@Override
	public void tickMovement() {
		super.tickMovement();

		if(!getWorld().isClient()) {
			Iterator<BlockPos.Mutable> iterator = BlockPos.iterateInSquare(getBlockPos().up(2), 1, Direction.WEST, Direction.SOUTH).iterator();
			boolean shouldSneak = false;

			if(fitsInOneBlockGap())
				iterator = Iterators.concat(iterator, BlockPos.iterateInSquare(getBlockPos().up(1), 1, Direction.WEST, Direction.SOUTH).iterator());

			while(!shouldSneak && iterator.hasNext() && horizontalCollision) {
				BlockPos.Mutable mutable = iterator.next();
				shouldSneak = !getWorld().getBlockState(mutable).canPathfindThrough(NavigationType.LAND);
			}

			if(isSneaking() != shouldSneak && canChangeIntoPose(isSneaking() ? EntityPose.STANDING : EntityPose.CROUCHING)) {
				setSneaking(shouldSneak);
				setPose(isSneaking() ? EntityPose.CROUCHING : EntityPose.STANDING);
			}
		}
	}

	@Override
	public boolean canTarget(LivingEntity target) {
		boolean isVisible = target.getComponent(ModComponents.VISIBLE_TO_SUPERNATURAL).isVisible();

		return super.canTarget(target) && isVisible;
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putBoolean("IsHunting", isHunting());
		nbt.putInt("AttackCooldown", getAttackCooldown());
		nbt.putUuid("Owner", ownerId);
		nbt.putInt("PoseIndex", getPose().getIndex());
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		setHunting(nbt.getBoolean("IsHunting"));
		setAttackCooldown(nbt.getInt("AttackCooldown"));
		ownerId = nbt.containsUuid("Owner") ? nbt.getUuid("Owner") : Utils.NIL_UUID;
		setPose(EntityPose.values()[nbt.getInt("PoseIndex")]);
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

	protected boolean canChangeIntoPose(EntityPose pose) {
		return getWorld().isSpaceEmpty(this, getBaseDimensions(pose).getBoxAt(getPos()).contract(1.0E-7));
	}

	public boolean isWeakTo(DamageSource source) {
		return source.getSource() instanceof VampireBeastEntity || source.getSource() instanceof WerewolfBeastEntity || source.isIn(DamageTypeTags.IS_FIRE) || source.isIn(DamageTypeTags.BYPASSES_RESISTANCE) || (source.getWeaponStack() != null && EnchantmentHelper.getLevel(Utils.registryEntry(Enchantments.SMITE, getWorld()), source.getWeaponStack()) > 0);
	}

	public boolean fitsInOneBlockGap() {
		return false;
	}
}
