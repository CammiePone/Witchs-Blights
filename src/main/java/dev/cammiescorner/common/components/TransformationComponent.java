package dev.cammiescorner.common.components;

import dev.cammiescorner.WitchsBlights;
import dev.cammiescorner.api.Transformation;
import dev.cammiescorner.common.Utils;
import dev.cammiescorner.common.registries.ModComponents;
import dev.cammiescorner.common.registries.ModTransformations;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Difficulty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import java.util.List;
import java.util.UUID;

public class TransformationComponent implements AutoSyncedComponent, ServerTickingComponent {
	protected final PlayerEntity player;
	protected Transformation transformation = ModTransformations.NONE.get();
	protected UUID targetId = Utils.NIL_UUID;
	protected boolean isUrging;
	protected boolean isTransformed;
	protected long startedUrging;
	protected int noTargetTimer;

	public TransformationComponent(PlayerEntity player) {
		this.player = player;
	}

	@Override
	public void serverTick() {
		if(player.getWorld() instanceof ServerWorld world && player instanceof ServerPlayerEntity serverPlayer && transformation.isAfflicted(player)) {
			HostileEntity thaBeast = serverPlayer.getCameraEntity() instanceof HostileEntity beast ? beast : null;

			if(!isTransformed && world.getDifficulty() != Difficulty.PEACEFUL) {
				List<Entity> targets = world.getOtherEntities(player, player.getBoundingBox().expand(16), entity -> entity instanceof LivingEntity && entity.getType().isIn(transformation.getTargets())).stream().sorted((o1, o2) -> Double.compare(o1.squaredDistanceTo(player), o2.squaredDistanceTo(player))).toList();

				if(targets.isEmpty() && isUrging)
					stopUrging();
				else if(!targets.isEmpty() && targets.getFirst() instanceof LivingEntity target) {
					if(!isUrging)
						startUrging(target);

					if(getUrgingProgress() >= 1)
						transformation.transform(serverPlayer, target);
				}
			}
			else {
				if(thaBeast != null) {
					if(thaBeast.isRemoved()) {
						transformation.untransform(serverPlayer);
						return;
					}

					if(thaBeast.getTarget() != null)
						noTargetTimer = 100;
					else if(noTargetTimer-- <= 0)
						transformation.untransform(serverPlayer);
				}
				else if(getUrgingProgress() < 1)
					transformation.untransform(serverPlayer);
			}
		}
	}

	@Override
	public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		transformation = WitchsBlights.TRANSFORMATIONS.get(Identifier.of(tag.getString("Id")));
		targetId = tag.getUuid("TargetId");
		isUrging = tag.getBoolean("IsUrging");
		isTransformed = tag.getBoolean("IsTransformed");
		startedUrging = tag.getLong("StartedUrging");
		noTargetTimer = tag.getInt("NoTargetTimer");
	}

	@Override
	public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
		tag.putString("Id", WitchsBlights.TRANSFORMATIONS.getId(transformation).toString());
		tag.putUuid("TargetId", targetId);
		tag.putBoolean("IsUrging", isUrging);
		tag.putBoolean("IsTransformed", isTransformed);
		tag.putLong("StartedUrging", startedUrging);
		tag.putInt("NoTargetTimer", noTargetTimer);
	}

	public Transformation getTransformation() {
		return transformation;
	}

	public void setTransformation(Transformation transformation) {
		this.transformation = transformation;
	}

	public void startUrging(@NotNull LivingEntity target) {
		isUrging = true;
		startedUrging = player.getWorld().getTime();
		targetId = target.getUuid();
		player.syncComponent(ModComponents.TRANSFORMATION);
	}

	public void stopUrging() {
		isUrging = false;
		startedUrging = -1;
		targetId = Utils.NIL_UUID;
		player.syncComponent(ModComponents.TRANSFORMATION);
	}

	public float getUrgingProgress() {
		if(!isUrging || getTarget() == null)
			return 0;

		int timer = (int) (player.getWorld().getTime() - startedUrging);
		int maxUrgingTime = transformation.getMaxUrgingTicks(player, getTarget());

		return MathHelper.clamp((float) timer / maxUrgingTime, 0f, 1f);
	}

	@Nullable
	public LivingEntity getTarget() {
		if(player.getWorld() instanceof ServerWorld serverWorld && serverWorld.getEntity(targetId) instanceof LivingEntity target)
			return target;

		return null;
	}

	public boolean isTransformed() {
		return isTransformed;
	}

	public void setTransformed(boolean transformed) {
		isTransformed = transformed;
		player.syncComponent(ModComponents.TRANSFORMATION);
	}

	public void setNoTargetTimer(int timer) {
		noTargetTimer = timer;
	}

	@Override
	public void writeSyncPacket(RegistryByteBuf buf, ServerPlayerEntity recipient) {
		AutoSyncedComponent.super.writeSyncPacket(buf, recipient);
		LivingEntity target = getTarget();

		buf.writeBoolean(target != null);

		if(target != null)
			buf.writeVarInt(target.getId());
	}
}
