package dev.cammiescorner.common.entities;

import dev.cammiescorner.common.registries.ModSoundEvents;
import dev.cammiescorner.common.registries.ModStatusEffects;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
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
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class WerewolfBeastEntity extends BeastEntity {
	public static final TrackedData<Boolean> IS_CLIMBING = DataTracker.registerData(WerewolfBeastEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	public WerewolfBeastEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}

	public static DefaultAttributeContainer.Builder createWerewolfBeastAttributes() {
		return BeastEntity.createBeastAttributes()
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2)
				.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8);
	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
		builder.add(IS_CLIMBING, false);
	}

	@Override
	protected void initGoals() {
		goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 8f));
		goalSelector.add(8, new LookAroundGoal(this));
		goalSelector.add(2, new MeleeAttackGoal(this, 3, false));
		goalSelector.add(8, new WanderAroundGoal(this, 1));
		targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, false, entity -> !entity.hasStatusEffect(ModStatusEffects.CURSED_CLAWS.holder()) || entity.equals(getAttacker())));
		targetSelector.add(3, new ActiveTargetGoal<>(this, MerchantEntity.class, false));
		targetSelector.add(3, new ActiveTargetGoal<>(this, IllagerEntity.class, false));
		targetSelector.add(4, new ActiveTargetGoal<>(this, AnimalEntity.class, false));
	}

	@Override
	protected EntityNavigation createNavigation(World world) {
		return new SpiderNavigation(this, world);
	}

	@Override
	public void tick() {
		super.tick();

		if(!getWorld().isClient() && !isInPose(EntityPose.CROUCHING))
			setClimbing(horizontalCollision);
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

	}

	@Override
	protected EntityDimensions getBaseDimensions(EntityPose pose) {
		return pose == EntityPose.STANDING ? EntityDimensions.changing(0.6f, 2.7f).withEyeHeight(2.35f) : EntityDimensions.changing(0.8f, 0.8f).withEyeHeight(1.75f);
	}

	@Override
	public float getTargetingMargin() {
		return getPose() == EntityPose.CROUCHING ? 0.5f : 0f;
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		if(source.isIn(DamageTypeTags.IS_FALL))
			return false;

		if(!isWeakTo(source))
			amount *= 0.1f;

		return super.damage(source, amount);
	}

	@Override
	public boolean fitsInOneBlockGap() {
		return true;
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
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);

		setClimbing(nbt.getBoolean("IsClimbing"));
	}
}
