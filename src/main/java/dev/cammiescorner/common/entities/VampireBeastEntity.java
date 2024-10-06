package dev.cammiescorner.common.entities;

import dev.cammiescorner.common.Utils;
import dev.cammiescorner.common.entities.ai.VampireDrinkAndAttackGoal;
import dev.cammiescorner.common.registries.ModComponents;
import dev.cammiescorner.common.registries.ModSoundEvents;
import dev.cammiescorner.common.registries.ModStatusEffects;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VampireBeastEntity extends HostileEntity {
	public static final TrackedData<Boolean> HUNTING = DataTracker.registerData(VampireBeastEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	public static final TrackedData<Integer> ATTACK_COOLDOWN = DataTracker.registerData(VampireBeastEntity.class, TrackedDataHandlerRegistry.INTEGER);
	public FlightMoveControl flightMoveControl;
	public int targetUnreachableTimer;

	public VampireBeastEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
		this.flightMoveControl = new FlightMoveControl(this);
	}

	public static DefaultAttributeContainer.Builder createVampireBeastAttributes() {
		return HostileEntity.createHostileAttributes()
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 100)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.15)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6)
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 40);
	}

	@Override
	protected void initGoals() {
		goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 8f));
		goalSelector.add(8, new LookAroundGoal(this));
		goalSelector.add(2, new VampireDrinkAndAttackGoal(this, 3, false));
		goalSelector.add(8, new WanderAroundGoal(this, 1));
		targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, false, entity -> !entity.hasStatusEffect(ModStatusEffects.SANGUINE_BLIGHT.holder()) || entity.equals(getAttacker())));
		targetSelector.add(3, new ActiveTargetGoal<>(this, MerchantEntity.class, false));
		targetSelector.add(3, new ActiveTargetGoal<>(this, IllagerEntity.class, false));
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

		dataTracker.set(HUNTING, getTarget() != null && getTarget().isAlive());
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
	public void tickMovement() {
		super.tickMovement();

		if(!getWorld().isClient()) {
			boolean shouldSneak = getTarget() != null && !getWorld().isSpaceEmpty(getTarget(), Box.of(getTarget().getEyePos().add(0, 0.5, 0), getTarget().getWidth(), 1, getTarget().getWidth()));

			if(isSneaking() != shouldSneak && canChangeIntoPose(isSneaking() ? EntityPose.STANDING : EntityPose.CROUCHING)) {
				setSneaking(shouldSneak);
				setPose(isSneaking() ? EntityPose.CROUCHING : EntityPose.STANDING);
			}
		}
	}

	@Override
	protected EntityDimensions getBaseDimensions(EntityPose pose) {
		return pose == EntityPose.STANDING ? EntityDimensions.changing(0.6f, 2.7f).withEyeHeight(2.35f) : EntityDimensions.changing(0.6f, 1.9f).withEyeHeight(1.75f);
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		super.playStepSound(pos, state);
		BlockSoundGroup blockSoundGroup = state.getSoundGroup();
		playSound(ModSoundEvents.BEAST_STEP.get(), blockSoundGroup.getVolume() * 0.15f, blockSoundGroup.getPitch());
	}

	@Override
	protected @Nullable SoundEvent getAmbientSound() {
		return ModSoundEvents.VAMPIRE_IDLE.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return ModSoundEvents.VAMPIRE_DEATH.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return ModSoundEvents.VAMPIRE_HURT.get();
	}

	@Override
	protected void playHurtSound(DamageSource damageSource) {
		super.playHurtSound(damageSource);
	}

	@Override
	protected void playAttackSound() {
		resetSoundDelay();
		playSound(ModSoundEvents.VAMPIRE_ATTACK.get(), 0.5f, getSoundPitch());
	}

	@Override
	public void setTarget(@Nullable LivingEntity target) {
		super.setTarget(target);

		if(target != null && !target.equals(getTarget()))
			playSound(ModSoundEvents.VAMPIRE_SCREAM.get());
	}

	@Override
	public boolean tryAttack(Entity target) {
		DamageSource source = getDamageSources().mobAttack(this);
		boolean succeeds = super.tryAttack(target);

		if(target instanceof LivingEntity livingTarget && succeeds) {
			heal(livingTarget.modifyAppliedDamage(source, livingTarget.applyArmorToDamage(source, (float) getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE))));
			livingTarget.playSound(ModSoundEvents.BEAST_SCRATCH.get());
		}

		return succeeds;
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		if(source.isIn(DamageTypeTags.IS_FALL))
			return false;

		if(!isWeakTo(source))
			amount *= 0.55f;

		return super.damage(source, amount);
	}

	@Override
	public boolean canTarget(LivingEntity target) {
		boolean isVisible = target.getComponent(ModComponents.VISIBLE_TO_SUPERNATURAL).isVisible();
		boolean shouldTarget = (target.getComponent(ModComponents.BLOOD).getBlood() > 0 || target.equals(getAttacker()));

		return super.canTarget(target) && isVisible && shouldTarget;
	}

	protected boolean canChangeIntoPose(EntityPose pose) {
		return getWorld().isSpaceEmpty(this, getBaseDimensions(pose).getBoxAt(getPos()).contract(1.0E-7));
	}

	public boolean isWeakTo(DamageSource source) {
		return source.getSource() instanceof VampireBeastEntity || source.isIn(DamageTypeTags.IS_FIRE) || source.isIn(DamageTypeTags.BYPASSES_RESISTANCE) || (source.getWeaponStack() != null && EnchantmentHelper.getLevel(Utils.registryEntry(Enchantments.SMITE, getWorld()), source.getWeaponStack()) > 0);
	}

	public boolean canReachTarget(@NotNull LivingEntity target) {
		if(target.isFallFlying())
			return false;

		Path path = getNavigation().findPathTo(target, 0);

		if(path == null)
			return false;
		else {
			PathNode pathNode = path.getEnd();

			if(pathNode == null)
				return false;
			else
				return pathNode.getSquaredDistance(target.getBlockPos()) <= 1f;
		}
	}

	public void setFlying(boolean flying) {
		getComponent(ModComponents.SPECIAL_BEAST_MOVEMENT).setUsingSpecialMovement(flying);
	}

	public boolean isFlying() {
		return getComponent(ModComponents.SPECIAL_BEAST_MOVEMENT).isUsingSpecialMovement() && !isOnGround();
	}

	public boolean isHunting() {
		return dataTracker.get(HUNTING);
	}

	public int getAttackCooldown() {
		return dataTracker.get(ATTACK_COOLDOWN);
	}

	public void setAttackCooldown(int cooldown) {
		dataTracker.set(ATTACK_COOLDOWN, cooldown);
	}

	public int getTargetOutOfReachTimer() {
		return targetUnreachableTimer;
	}

	public void incrementTargetOutOfReachTimer() {
		targetUnreachableTimer++;
	}

	public void resetTargetOutOfReachTimer() {
		targetUnreachableTimer = 0;
	}

	public class FlightMoveControl extends MoveControl {
		private float targetSpeed = 0.2f;

		public FlightMoveControl(final MobEntity owner) {
			super(owner);
		}

		public void tick() {
			VampireBeastEntity.this.setFlying(true);

			if(VampireBeastEntity.this.horizontalCollision) {
				VampireBeastEntity.this.setYaw(VampireBeastEntity.this.getYaw() + 180f);
				targetSpeed = 0.2f;
			}

			Vec3d targetPosition = VampireBeastEntity.this.getTarget() != null ? VampireBeastEntity.this.getTarget().getPos().add(0, 0.5, 0) : Vec3d.ZERO;

			double distanceX = targetPosition.x - VampireBeastEntity.this.getX();
			double distanceY = targetPosition.y - VampireBeastEntity.this.getY();
			double distanceZ = targetPosition.z - VampireBeastEntity.this.getZ();
			double horizontalDistance = Math.sqrt(distanceX * distanceX + distanceZ * distanceZ);

			if(Math.abs(horizontalDistance) > 0.00001) {
				double h = 1 - Math.abs(distanceY * 0.7) / horizontalDistance;

				distanceX *= h;
				distanceZ *= h;
				horizontalDistance = Math.sqrt(distanceX * distanceX + distanceZ * distanceZ);

				double distance = Math.sqrt(distanceX * distanceX + distanceZ * distanceZ + distanceY * distanceY);
				float yaw = VampireBeastEntity.this.getYaw();
				float angleBetweenXZ = (float) MathHelper.atan2(distanceZ, distanceX);
				float wrappedYaw = MathHelper.wrapDegrees(VampireBeastEntity.this.getYaw() + 90f);
				float wrappedAngleBetweenXZ = MathHelper.wrapDegrees(angleBetweenXZ * 60f);
				float speedMultiplier = getTarget() != null && getTarget().isFallFlying() ? 1f : 0.25f;

				VampireBeastEntity.this.setYaw(MathHelper.stepUnwrappedAngleTowards(wrappedYaw, wrappedAngleBetweenXZ, 4f) - 90f);
				VampireBeastEntity.this.bodyYaw = VampireBeastEntity.this.getYaw();

				if(MathHelper.angleBetween(yaw, VampireBeastEntity.this.getYaw()) < 3f)
					targetSpeed = MathHelper.stepTowards(targetSpeed, 4f * speedMultiplier, 0.1f * ((2f * speedMultiplier) / targetSpeed));
				else
					targetSpeed = MathHelper.stepTowards(targetSpeed, 0.2f, 0.05f);

				float pitch = (float) -(MathHelper.atan2(-distanceY, horizontalDistance) * 60f);

				VampireBeastEntity.this.setPitch(pitch);

				float adjustedYaw = VampireBeastEntity.this.getYaw() + 90f;
				double accelerationX = (targetSpeed * MathHelper.cos(adjustedYaw * 0.02f)) * Math.abs(distanceX / distance);
				double accelerationZ = (targetSpeed * MathHelper.sin(adjustedYaw * 0.02f)) * Math.abs(distanceZ / distance);
				double accelerationY = (targetSpeed * MathHelper.sin((pitch * 0.02f))) * Math.abs(distanceY / distance);
				Vec3d vec3d = VampireBeastEntity.this.getVelocity();

				VampireBeastEntity.this.setVelocity(vec3d.add((new Vec3d(accelerationX, accelerationY, accelerationZ)).subtract(vec3d).multiply(0.2)));

				if(age % 35 == 0 && isFlying())
					playSound(ModSoundEvents.VAMPIRE_WINGS.get(), 0.5f, getSoundPitch());
			}
		}
	}
}
