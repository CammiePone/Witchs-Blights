package dev.cammiescorner.common.registries;

import dev.cammiescorner.WitchsBlights;
import dev.cammiescorner.common.items.ClothItem;
import dev.cammiescorner.common.items.RosaryItem;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.RegistryKeys;

public class ModItems {
	public static final RegistryHandler<Item> ITEMS = RegistryHandler.create(RegistryKeys.ITEM, WitchsBlights.MOD_ID);

	public static final RegistrySupplier<Item> ROSARY = ITEMS.register("rosary", RosaryItem::new);
	public static final RegistrySupplier<Item> CLOTH = ITEMS.register("cloth", ClothItem::new);
	public static final RegistrySupplier<Item> BLOODSTAINED_CLOTH = ITEMS.register("bloodstained_cloth", ClothItem::new);
	public static final RegistrySupplier<Item> MISTLETOE = ITEMS.register("mistletoe", () -> new BlockItem(ModBlocks.MISTLETOE.get(), new Item.Settings()));
	public static final RegistrySupplier<Item> BUNDLED_MISTLETOE = ITEMS.register("bundled_mistletoe", () -> new BlockItem(ModBlocks.BUNDLED_MISTLETOE.get(), new Item.Settings()));
	public static final RegistrySupplier<Item> VAMPIRE_BEAST_EGG = ITEMS.register("vampire_beast_spawn_egg", () -> new SpawnEggItem(ModEntities.VAMPIRE_BEAST.get(), 0xb39e88, 0xe0d9c2, new Item.Settings()));
}
