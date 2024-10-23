package dev.cammiescorner.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.cammiescorner.common.components.TransformationComponent;
import dev.cammiescorner.common.registries.ModComponents;
import dev.cammiescorner.common.registries.ModTransformations;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Item.class)
public class ItemMixin {
	@ModifyExpressionValue(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;canConsume(Z)Z"))
	private boolean werewolvesOnlyEatMeat(boolean original, World world, PlayerEntity user, Hand hand, @Local ItemStack itemStack, @Local FoodComponent foodComponent) {
		TransformationComponent component = user.getComponent(ModComponents.TRANSFORMATION);
		System.out.println("Is Werewolf: " + (component.getTransformation() == ModTransformations.WEREWOLF.get()));
		System.out.println("Is Stack Meat: " + itemStack.isIn(ItemTags.MEAT));

		if(component.getTransformation() == ModTransformations.WEREWOLF.get())
			return itemStack.isIn(ItemTags.MEAT);

		return original;
	}
}
