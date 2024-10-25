package dev.cammiescorner.witchsblights.common.components;

import dev.cammiescorner.witchsblights.common.registries.ModBlocks;
import dev.cammiescorner.witchsblights.common.registries.ModComponents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import java.util.ArrayList;
import java.util.List;

public class VisibleToSupernaturalComponent implements ServerTickingComponent, AutoSyncedComponent {
	private final LivingEntity entity;
	private boolean isVisible = true;

	public VisibleToSupernaturalComponent(LivingEntity entity) {
		this.entity = entity;
	}

	@Override
	public void serverTick() {
		if(entity.age % 20 == 0) {
			World world = entity.getWorld();
			List<BlockState> states = new ArrayList<>();
			BlockPos.Mutable mutable = entity.getBlockPos().mutableCopy();

			for(int x = -5; x < 5; x++)
				for(int z = -5; z < 5; z++)
					for(int y = -5; y < 5; y++)
						states.add(world.getBlockState(mutable.add(x, y, z)));

			setVisible(states.stream().filter(blockState -> blockState.isOf(ModBlocks.MISTLETOE.get()) || blockState.isOf(ModBlocks.BUNDLED_MISTLETOE.get())).toList().isEmpty());
		}
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		isVisible = tag.getBoolean("isVisible");
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putBoolean("isVisible", isVisible);
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean visible) {
		if(isVisible != visible) {
			isVisible = visible;
			ModComponents.VISIBLE_TO_SUPERNATURAL.sync(entity);
		}
	}
}
