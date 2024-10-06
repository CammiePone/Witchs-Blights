package dev.cammiescorner.common.components.client;

import dev.cammiescorner.common.components.TransformationComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.RegistryByteBuf;

public class ClientTransformationComponent extends TransformationComponent {
	protected boolean targetExists;
	protected int targetId;

	public ClientTransformationComponent(PlayerEntity player) {
		super(player);
	}

	@Override
	public LivingEntity getTarget() {
		return targetExists && player.getWorld().getEntityById(targetId) instanceof LivingEntity target ? target : null;
	}

	@Override
	public void applySyncPacket(RegistryByteBuf buf) {
		super.applySyncPacket(buf);
		targetExists = buf.readBoolean();

		if(targetExists)
			targetId = buf.readVarInt();
	}
}
