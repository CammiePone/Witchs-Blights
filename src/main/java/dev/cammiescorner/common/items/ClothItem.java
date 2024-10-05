package dev.cammiescorner.common.items;

import dev.cammiescorner.common.Utils;
import dev.cammiescorner.common.registries.ModComponentTypes;
import dev.cammiescorner.common.registries.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ClothItem extends Item {
	public ClothItem() {
		super(new Settings().maxCount(16).component(ModComponentTypes.TARGETED_ENTITY.get(), Utils.NIL_UUID));
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if(user.isSneaking()) {
			EntityHitResult hitResult = Utils.raycastEntity(user);
			ItemStack stack = user.getStackInHand(hand);

			if(hitResult != null && hitResult.getEntity() instanceof PlayerEntity target && Math.abs(MathHelper.subtractAngles(target.getHeadYaw(), user.getHeadYaw())) <= 75 && stack.isOf(ModItems.CLOTH.get())) {
				ItemStack newStack = new ItemStack(ModItems.BLOODSTAINED_CLOTH.get());

				newStack.set(ModComponentTypes.TARGETED_ENTITY.get(), target.getUuid());

				return TypedActionResult.success(ItemUsage.exchangeStack(stack, user, newStack), user.getWorld().isClient());
			}
		}

		return super.use(world, user, hand);
	}
}
