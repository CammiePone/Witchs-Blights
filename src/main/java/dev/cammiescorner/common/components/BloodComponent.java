package dev.cammiescorner.common.components;

import dev.cammiescorner.common.registries.ModDamageTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

public class BloodComponent implements ServerTickingComponent {
	private static final int MAX_BLOOD = 20;
	private final LivingEntity entity;
	private final World world;
	private long lastDrunkFrom = 0;
	private int blood = MAX_BLOOD;

	public BloodComponent(LivingEntity entity) {
		this.entity = entity;
		this.world = entity.getWorld();
	}

	@Override
	public void serverTick() {
		long timer = world.getTime() - lastDrunkFrom;

		if(blood < MAX_BLOOD && timer > 0 && timer % 200 == 0)
			gainBlood(1);
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		lastDrunkFrom = tag.getLong("LastDrunkFrom");
		blood = tag.getInt("Blood");
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putLong("LastDrunkFrom", lastDrunkFrom);
		tag.putInt("Blood", blood);
	}

	public int getBlood() {
		return blood;
	}

	public void gainBlood(int amount) {
		blood = Math.clamp(blood + amount, 0, MAX_BLOOD);
	}

	public void drainBlood(int amount, @Nullable LivingEntity drainer) {
		blood = Math.clamp(blood - amount, 0, MAX_BLOOD);
		lastDrunkFrom = world.getTime();

		if(drainer != null) {
			DamageSource source = ModDamageTypes.bloodDrained(drainer);
			entity.damage(source, 2);
		}
	}
}
