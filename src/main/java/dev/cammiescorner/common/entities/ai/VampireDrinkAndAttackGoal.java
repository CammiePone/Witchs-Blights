package dev.cammiescorner.common.entities.ai;

import dev.cammiescorner.common.components.BloodComponent;
import dev.cammiescorner.common.entities.VampireBeastEntity;
import dev.cammiescorner.common.registries.ModComponents;
import dev.cammiescorner.common.registries.ModSoundEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;

public class VampireDrinkAndAttackGoal extends MeleeAttackGoal {
	private final VampireBeastEntity vampireBeast;
	private int ticks;

	public VampireDrinkAndAttackGoal(VampireBeastEntity vampireBeast, double speed, boolean pauseWhenMobIdle) {
		super(vampireBeast, speed, pauseWhenMobIdle);
		this.vampireBeast = vampireBeast;
	}

	@Override
	public void start() {
		super.start();
		ticks = 0;
	}

	@Override
	public void stop() {
		super.stop();
		vampireBeast.setAttacking(false);
	}

	@Override
	public void tick() {
		super.tick();
		ticks++;

		vampireBeast.setAttacking(ticks >= 5 && getCooldown() < getMaxCooldown() / 2);
		vampireBeast.setAttackCooldown(getCooldown());
	}

	@Override
	protected int getMaxCooldown() {
		return getTickCount(30);
	}

	@Override
	protected void attack(LivingEntity target) {
		LivingEntity attacker = vampireBeast.getAttacker();

		if(attacker instanceof Ownable ownable && ownable.getOwner() instanceof LivingEntity owner)
			attacker = owner;

		if(target.equals(attacker)) {
			super.attack(target);
			vampireBeast.setAttacker(attacker);
		}
		else if(canAttack(target) && target.getComponent(ModComponents.BLOOD).getBlood() > 0) {
			drink(target);
		}
	}

	@Override
	protected boolean canAttack(LivingEntity target) {
		return super.canAttack(target) && target.timeUntilRegen <= 0;
	}

	private void drink(LivingEntity target) {
		BloodComponent component = target.getComponent(ModComponents.BLOOD);
		World world = target.getWorld();

		if(vampireBeast.isAttacking()) {
			component.drainBlood(1, vampireBeast);
			world.playSound(null, vampireBeast.getBlockPos(), ModSoundEvents.VAMPIRE_DRINK_BLOOD.get(), SoundCategory.HOSTILE, 1f, world.random.nextFloat() * 0.1f + 0.9f);
			resetCooldown();
		}
	}
}
