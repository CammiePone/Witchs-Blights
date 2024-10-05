package dev.cammiescorner.common.components;

import dev.cammiescorner.common.status_effects.CursedStatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IncurableEffectsComponent implements Component {
	private final PlayerEntity player;
	private final List<StatusEffectInstance> effects = new ArrayList<>();

	public IncurableEffectsComponent(PlayerEntity player) {
		this.player = player;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		effects.clear();
		NbtList list = tag.getList("StatusEffects", NbtElement.COMPOUND_TYPE);

		for(int i = 0; i < list.size(); i++) {
			NbtCompound nbt = list.getCompound(i);
			effects.add(StatusEffectInstance.fromNbt(nbt));
		}
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		NbtList list = new NbtList();

		for(StatusEffectInstance effect : effects)
			list.add(effect.writeNbt());

		tag.put("StatusEffects", list);
	}

	public void addStatusEffect(StatusEffectInstance statusEffect) {
		if(statusEffect.getEffectType().value() instanceof CursedStatusEffect)
			effects.add(statusEffect);
	}

	public List<StatusEffectInstance> getEffects() {
		return Collections.unmodifiableList(effects);
	}

	public void clearStatusEffects() {
		effects.clear();
	}
}
