package dev.cammiescorner.witchsblights.common.components;

import dev.cammiescorner.witchsblights.common.registries.ModComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class BeastMovementComponent implements AutoSyncedComponent {
	private final LivingEntity beast;
	private boolean usingSpecialMovement = false;

	public BeastMovementComponent(LivingEntity beast) {
		this.beast = beast;
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		usingSpecialMovement = tag.getBoolean("UsingSpecialMovement");
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putBoolean("UsingSpecialMovement", usingSpecialMovement);
	}

	public boolean isUsingSpecialMovement() {
		return usingSpecialMovement;
	}

	public void setUsingSpecialMovement(boolean movingSpecially) {
		if(usingSpecialMovement != movingSpecially) {
			usingSpecialMovement = movingSpecially;
			beast.syncComponent(ModComponents.SPECIAL_BEAST_MOVEMENT);
		}
	}
}
