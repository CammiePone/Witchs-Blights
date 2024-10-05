package dev.cammiescorner.common;

import dev.cammiescorner.WitchsBlights;
import dev.cammiescorner.common.registries.ModStatusEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Locale;
import java.util.UUID;

public class Utils {
	public static final UUID NIL_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
	public static final StatusEffectInstance SANGUINE_BLIGHT_I = new StatusEffectInstance(ModStatusEffects.SANGUINE_BLIGHT.holder(), 7 * Utils.DurationUnits.DAYS.getMultiplier(), 0, true, false, true);
	public static final StatusEffectInstance SANGUINE_BLIGHT_II = new StatusEffectInstance(ModStatusEffects.SANGUINE_BLIGHT.holder(), 7 * Utils.DurationUnits.WEEKS.getMultiplier(), 1, true, false, true);
	public static final StatusEffectInstance SANGUINE_BLIGHT_III = new StatusEffectInstance(ModStatusEffects.SANGUINE_BLIGHT.holder(), 7 * Utils.DurationUnits.MONTHS.getMultiplier(), 2, true, false, true);
	public static final StatusEffectInstance CURSED_CLAWS_I = new StatusEffectInstance(ModStatusEffects.CURSED_CLAWS.holder(), 7 * Utils.DurationUnits.DAYS.getMultiplier(), 0, true, false, true);
	public static final StatusEffectInstance CURSED_CLAWS_II = new StatusEffectInstance(ModStatusEffects.CURSED_CLAWS.holder(), 7 * Utils.DurationUnits.WEEKS.getMultiplier(), 1, true, false, true);
	public static final StatusEffectInstance CURSED_CLAWS_III = new StatusEffectInstance(ModStatusEffects.CURSED_CLAWS.holder(), 7 * Utils.DurationUnits.MONTHS.getMultiplier(), 2, true, false, true);

	public static EntityHitResult raycastEntity(PlayerEntity camera) {
		double d = Math.max(camera.getBlockInteractionRange(), camera.getEntityInteractionRange());
		Vec3d vec3d = camera.getCameraPosVec(1f);
		HitResult hitResult = camera.raycast(d, 1f, false);
		double f = hitResult.getPos().squaredDistanceTo(vec3d);
		Vec3d vec3d2 = camera.getRotationVec(1f);
		Vec3d vec3d3 = vec3d.add(vec3d2.x * d, vec3d2.y * d, vec3d2.z * d);
		Box box = camera.getBoundingBox().stretch(vec3d2.multiply(d)).expand(1f, 1f, 1f);
		EntityHitResult entityHitResult = ProjectileUtil.raycast(camera, vec3d, vec3d3, box, entity -> !entity.isSpectator() && entity.canHit(), MathHelper.square(d));

		return entityHitResult != null && entityHitResult.getPos().squaredDistanceTo(vec3d) < f ? entityHitResult : null;
	}

	public static <T> RegistryEntry.Reference<T> registryEntry(RegistryKey<T> key, World world) {
		return world.getRegistryManager().get(key.getRegistryRef()).entryOf(key);
	}

	public enum DurationUnits {
		SECONDS(20), MINUTES(1200), DAYS(24000), WEEKS(168000), MONTHS(672000);

		private final int multiplier;

		DurationUnits(int multiplier) {
			this.multiplier = multiplier;
		}

		public int getMultiplier() {
			return multiplier;
		}

		public String getTranslationKey() {
			return "duration_unit." + WitchsBlights.MOD_ID + "." + name().toLowerCase(Locale.ROOT);
		}
	}
}
