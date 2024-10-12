package dev.cammiescorner.mixin;

import dev.cammiescorner.WitchsBlights;
import dev.cammiescorner.api.Transformation;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PatrolEntity;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;

@Mixin(RaiderEntity.class)
public abstract class RaiderEntityMixin extends PatrolEntity {
	@Unique private final RaiderEntity self = (RaiderEntity) (Object) this;

	protected RaiderEntityMixin(EntityType<? extends PatrolEntity> entityType, World world) { super(entityType, world); }

	@Inject(method = "onDeath", at = @At("HEAD"))
	private void throwCursedPotion(DamageSource damageSource, CallbackInfo info) {
		if(self instanceof WitchEntity && damageSource.getAttacker() instanceof PlayerEntity attacker && !attacker.isCreative() && getRandom().nextFloat() <= 0.25f) {
			Vec3d vec3d = attacker.getVelocity();
			double d = attacker.getX() + vec3d.x - getX();
			double e = attacker.getEyeY() - 1.1f - getY();
			double f = attacker.getZ() + vec3d.z - getZ();
			double g = Math.sqrt(d * d + f * f);

			List<RegistryEntry<Potion>> curses = WitchsBlights.TRANSFORMATIONS.stream().map(Transformation::getCurse).filter(Objects::nonNull).toList();
			RegistryEntry<Potion> registryEntry = curses.get(getRandom().nextInt(curses.size()));
			PotionEntity potionEntity = new PotionEntity(getWorld(), this);
			potionEntity.setItem(PotionContentsComponent.createStack(Items.SPLASH_POTION, registryEntry));
			potionEntity.setPitch(potionEntity.getPitch() + 20f);
			potionEntity.setVelocity(d, e + g * 0.2f, f, 0.5f, 8f);

			if(!isSilent())
				getWorld().playSound(
						null, getX(), getY(), getZ(), SoundEvents.ENTITY_WITCH_THROW, getSoundCategory(), 1f, 0.8f + random.nextFloat() * 0.4f
				);

			getWorld().spawnEntity(potionEntity);
		}
	}
}
