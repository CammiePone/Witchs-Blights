package dev.cammiescorner.common.components;

import dev.cammiescorner.common.registries.ModComponents;
import dev.cammiescorner.common.status_effects.CursedStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.WorldEvents;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import java.util.ArrayList;
import java.util.List;

public class CureCurseComponent implements AutoSyncedComponent, ServerTickingComponent {
	private final PlayerEntity player;
	private static final long MAX_CURE_TIME = 1200;
	private long startedCuring = 0;
	private boolean isCuring = false;

	public CureCurseComponent(PlayerEntity player) {
		this.player = player;
	}

	@Override
	public void serverTick() {
		long timer = player.getWorld().getTime() - startedCuring;

		if(isCuring && timer >= MAX_CURE_TIME) {
			List<RegistryEntry<StatusEffect>> effects = new ArrayList<>();

			for(StatusEffectInstance effect : player.getStatusEffects())
				if(effect.getEffectType().value() instanceof CursedStatusEffect)
					effects.add(effect.getEffectType());

			for(RegistryEntry<StatusEffect> effect : effects)
				player.removeStatusEffect(effect);

			isCuring = false;
			player.syncComponent(ModComponents.CURE_CURSE);

			if(!player.isSilent())
				player.getWorld().syncWorldEvent(null, WorldEvents.ZOMBIE_VILLAGER_CURED, player.getBlockPos(), 0);
		}
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		startedCuring = tag.getLong("StartedCuring");
		isCuring = tag.getBoolean("IsCuring");
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putLong("StartedCuring", startedCuring);
		tag.putBoolean("IsCuring", isCuring);
	}

	public void startCuring() {
		if(!player.getStatusEffects().stream().filter(effect -> effect.getEffectType().value() instanceof CursedStatusEffect).toList().isEmpty()) {
			startedCuring = player.getWorld().getTime();
			isCuring = true;
			player.syncComponent(ModComponents.CURE_CURSE);
		}
	}

	public boolean isCuring() {
		return isCuring;
	}
}
