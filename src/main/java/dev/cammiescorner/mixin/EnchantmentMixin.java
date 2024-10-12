package dev.cammiescorner.mixin;

import dev.cammiescorner.common.registries.ModStatusEffects;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
	@Unique private final Enchantment self = (Enchantment) (Object) this;

	@Inject(method = "modifyDamage", at = @At("HEAD"))
	private void fuckEnchants(ServerWorld world, int level, ItemStack stack, Entity user, DamageSource damageSource, MutableFloat damage, CallbackInfo info) {
		if(world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(self).matchesKey(Enchantments.SMITE) && user instanceof LivingEntity target && target.hasStatusEffect(ModStatusEffects.SANGUINE_BLIGHT.holder()))
			damage.add(level * 2.5);
	}
}
