package dev.cammiescorner.common.entities;

import dev.cammiescorner.ModConfig;
import dev.cammiescorner.common.Utils;
import dev.cammiescorner.common.registries.ModSoundEvents;
import dev.cammiescorner.common.registries.ModStatusEffects;
import dev.cammiescorner.common.registries.ModTags;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.SpiderNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class WerewolfBeastEntity extends BeastEntity {
	public static final TrackedData<Boolean> IS_CLIMBING = DataTracker.registerData(WerewolfBeastEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	public static final TrackedData<Boolean> SNOWY = DataTracker.registerData(WerewolfBeastEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	public SwimMoveControl swimMoveControl;

	public WerewolfBeastEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
		this.swimMoveControl = new SwimMoveControl(this);
	}

	public static DefaultAttributeContainer.Builder createWerewolfBeastAttributes() {
		return BeastEntity.createBeastAttributes()
				.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 50)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8);
	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
		builder.add(IS_CLIMBING, false);
		builder.add(SNOWY, false);
	}

	@Override
	protected void initGoals() {
		goalSelector.add(1, new MeleeAttackGoal(this, 3, false));
		goalSelector.add(2, new LookAtEntityGoal(this, PlayerEntity.class, 8f));
		goalSelector.add(3, new LookAroundGoal(this));
		goalSelector.add(3, new WanderAroundFarGoal(this, 1));
		targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, false, entity -> !entity.hasStatusEffect(ModStatusEffects.CURSED_CLAWS.holder()) || entity.equals(getAttacker())));

		Registry<EntityType<?>> registry = getRegistryManager().get(RegistryKeys.ENTITY_TYPE);
		RegistryKey<EntityType<?>> playerKey = registry.getKey(EntityType.PLAYER).orElseThrow();

		registry.getEntryList(ModTags.WEREWOLF_BEAST_TARGETS).ifPresent(registryEntries -> registryEntries.forEach(entry -> {
			if(entry.hasKeyAndValue() && !entry.matchesKey(playerKey)) {
				EntityType<?> type = entry.value();

				if(type.create(getWorld()) instanceof LivingEntity target)
					targetSelector.add(3, new ActiveTargetGoal(this, target.getClass(), false));
			}
		}));
	}

	@Override
	public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
		if(world.getBiome(getBlockPos()).isIn(ConventionalBiomeTags.IS_SNOWY))
			makeSnowy();

		return super.initialize(world, difficulty, spawnReason, entityData);
	}

	@Override
	protected EntityNavigation createNavigation(World world) {
		return new SpiderNavigation(this, world);
	}

	@Override
	public void tick() {
		super.tick();

		if(!getWorld().isClient())
			setClimbing(horizontalCollision && (!isSneaking() || canChangeIntoPose(EntityPose.STANDING)));
	}

	@Override
	public boolean shouldSneak() {
		return (super.shouldSneak() || isHunting()) && !isClimbing();
	}

	@Override
	public boolean isClimbing() {
		return dataTracker.get(IS_CLIMBING);
	}

	public void setClimbing(boolean climbing) {
		dataTracker.set(IS_CLIMBING, climbing);
	}

	@Override
	public void addExtraAttackEffects(LivingEntity target) {
		if(getWorld().getMoonPhase() == 0 && !getWorld().isDay() && target.canHaveStatusEffect(Utils.CURSED_CLAWS_I) && getRandom().nextFloat() < ModConfig.Werewolf.werewolfSpreadCurseChance) {
			target.addStatusEffect(Utils.CURSED_CLAWS_I);
			setTarget(null);
		}
	}

	@Override
	public boolean onKilledOther(ServerWorld world, LivingEntity other) {
		ServerPlayerEntity owner = getOwner();

		if(owner != null)
			owner.getHungerManager().add((int) other.getMaxHealth(), 0.8f);

		return super.onKilledOther(world, other);
	}

	@Override
	public boolean disablesShield() {
		return true;
	}

	@Override
	protected EntityDimensions getBaseDimensions(EntityPose pose) {
		return pose == EntityPose.STANDING ? EntityDimensions.changing(0.8f, 2.7f).withEyeHeight(2.35f) : EntityDimensions.changing(0.8f, 0.8f).withEyeHeight(0.7f);
	}

	@Override
	public float getTargetingMargin() {
		return getPose() == EntityPose.CROUCHING ? 0.8f : 0f;
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		if(source.isIn(DamageTypeTags.IS_FALL))
			return false;

		if(!Utils.isTransformationWeakTo(source, getWorld()))
			amount *= 0.1f;

		return super.damage(source, amount);
	}

	@Override
	protected @Nullable SoundEvent getAmbientSound() {
		return ModSoundEvents.WEREWOLF_IDLE.get();
	}

	@Override
	protected SoundEvent getDeathSound() {
		return ModSoundEvents.WEREWOLF_DEATH.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return ModSoundEvents.WEREWOLF_HURT.get();
	}

	@Override
	public SoundEvent getAttackSound() {
		return ModSoundEvents.WEREWOLF_ATTACK.get();
	}

	@Override
	public SoundEvent getFoundTargetSound() {
		return ModSoundEvents.WEREWOLF_SCREAM.get();
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);

		nbt.putBoolean("IsClimbing", isClimbing());
		nbt.putBoolean("IsSnowy", isSnowy());
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);

		setClimbing(nbt.getBoolean("IsClimbing"));

		if(nbt.getBoolean("IsSnowy"))
			makeSnowy();
	}

	public void makeSnowy() {
		dataTracker.set(SNOWY, true);
	}

	public boolean isSnowy() {
		return dataTracker.get(SNOWY);
	}

	public class SwimMoveControl extends MoveControl {
		private float targetSpeed = 0.2f;

		public SwimMoveControl(final MobEntity owner) {
			super(owner);
		}

		public void tick() {
			if(WerewolfBeastEntity.this.horizontalCollision) {
				WerewolfBeastEntity.this.setYaw(WerewolfBeastEntity.this.getYaw() + 180f);
				targetSpeed = 0.2f;
			}

			Vec3d targetPosition = WerewolfBeastEntity.this.getTarget() != null ? WerewolfBeastEntity.this.getTarget().getPos().add(0, 0.5, 0) : Vec3d.ZERO;

			double distanceX = targetPosition.x - WerewolfBeastEntity.this.getX();
			double distanceY = targetPosition.y - WerewolfBeastEntity.this.getY();
			double distanceZ = targetPosition.z - WerewolfBeastEntity.this.getZ();
			double horizontalDistance = Math.sqrt(distanceX * distanceX + distanceZ * distanceZ);

			if(Math.abs(horizontalDistance) > 0.00001) {
				double h = 1 - Math.abs(distanceY * 0.7) / horizontalDistance;

				distanceX *= h;
				distanceZ *= h;
				horizontalDistance = Math.sqrt(distanceX * distanceX + distanceZ * distanceZ);

				double distance = Math.sqrt(distanceX * distanceX + distanceZ * distanceZ + distanceY * distanceY);
				float yaw = WerewolfBeastEntity.this.getYaw();
				float angleBetweenXZ = (float) MathHelper.atan2(distanceZ, distanceX);
				float wrappedYaw = MathHelper.wrapDegrees(WerewolfBeastEntity.this.getYaw() + 90f);
				float wrappedAngleBetweenXZ = MathHelper.wrapDegrees(angleBetweenXZ * 60f);
				float speedMultiplier = getTarget() != null && getTarget().isFallFlying() ? 1f : 0.25f;

				WerewolfBeastEntity.this.setYaw(MathHelper.stepUnwrappedAngleTowards(wrappedYaw, wrappedAngleBetweenXZ, 4f) - 90f);
				WerewolfBeastEntity.this.bodyYaw = WerewolfBeastEntity.this.getYaw();

				if(MathHelper.angleBetween(yaw, WerewolfBeastEntity.this.getYaw()) < 3f)
					targetSpeed = MathHelper.stepTowards(targetSpeed, 4f * speedMultiplier, 0.1f * ((2f * speedMultiplier) / targetSpeed));
				else
					targetSpeed = MathHelper.stepTowards(targetSpeed, 0.2f, 0.05f);

				float pitch = (float) -(MathHelper.atan2(-distanceY, horizontalDistance) * 60f);

				WerewolfBeastEntity.this.setPitch(pitch);

				float adjustedYaw = WerewolfBeastEntity.this.getYaw() + 90f;
				double accelerationX = (targetSpeed * MathHelper.cos(adjustedYaw * 0.02f)) * Math.abs(distanceX / distance);
				double accelerationZ = (targetSpeed * MathHelper.sin(adjustedYaw * 0.02f)) * Math.abs(distanceZ / distance);
				double accelerationY = (targetSpeed * MathHelper.sin((pitch * 0.02f))) * Math.abs(distanceY / distance);
				Vec3d vec3d = WerewolfBeastEntity.this.getVelocity();

				WerewolfBeastEntity.this.setVelocity(vec3d.add((new Vec3d(accelerationX, accelerationY, accelerationZ)).subtract(vec3d).multiply(0.2)));
			}
		}
	}
}
