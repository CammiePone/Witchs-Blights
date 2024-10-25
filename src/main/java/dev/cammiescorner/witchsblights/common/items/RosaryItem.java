package dev.cammiescorner.witchsblights.common.items;

import dev.cammiescorner.witchsblights.common.registries.ModItems;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Equipment;
import net.minecraft.item.Item;

public class RosaryItem extends Item implements Equipment {
	public RosaryItem() {
		super(new Settings().equipmentSlot((entity, stack) -> EquipmentSlot.CHEST).maxDamage(300).maxCount(1));
	}

	@Override
	public EquipmentSlot getSlotType() {
		return EquipmentSlot.CHEST;
	}

	public static boolean hasRosaryEquipped(LivingEntity entity) {
		EquipmentSlot slot = getRosarySlot(entity);
		return slot == EquipmentSlot.CHEST || slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND;
	}

	public static EquipmentSlot getRosarySlot(LivingEntity entity) {
		if(entity.getEquippedStack(EquipmentSlot.CHEST).isOf(ModItems.ROSARY.get()))
			return EquipmentSlot.CHEST;

		if(entity.getEquippedStack(EquipmentSlot.MAINHAND).isOf(ModItems.ROSARY.get()))
			return EquipmentSlot.MAINHAND;

		if(entity.getEquippedStack(EquipmentSlot.OFFHAND).isOf(ModItems.ROSARY.get()))
			return EquipmentSlot.OFFHAND;

		return null;
	}
}
