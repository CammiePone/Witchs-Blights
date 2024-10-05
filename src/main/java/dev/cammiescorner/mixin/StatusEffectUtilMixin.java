package dev.cammiescorner.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.cammiescorner.common.Utils;
import dev.cammiescorner.common.status_effects.CursedStatusEffect;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(StatusEffectUtil.class)
public class StatusEffectUtilMixin {
	@WrapOperation(method = "getDurationText", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/StringHelper;formatTicks(IF)Ljava/lang/String;"))
	private static String witchsblights$formatPotionText(int ticks, float tickRateArg, Operation<String> original, StatusEffectInstance effect, float multiplier, float tickRateParam) {
		if(effect.getEffectType().value() instanceof CursedStatusEffect) {
			float months = ticks / (float) Utils.DurationUnits.MONTHS.getMultiplier();
			float weeks = ticks / (float) Utils.DurationUnits.WEEKS.getMultiplier();
			float days = ticks / (float) Utils.DurationUnits.DAYS.getMultiplier();

			if(MathHelper.floor(months) > 1)
				return I18n.translate(Utils.DurationUnits.MONTHS.getTranslationKey(), Math.round(months));
			else if(MathHelper.floor(weeks) > 1)
				return I18n.translate(Utils.DurationUnits.WEEKS.getTranslationKey(), Math.round(weeks));
			else if(MathHelper.floor(days) > 1)
				return I18n.translate(Utils.DurationUnits.DAYS.getTranslationKey(), Math.round(days));
		}

		return original.call(ticks, tickRateArg);
	}
}
