package dev.cammiescorner.witchsblights.common.registries;

import dev.cammiescorner.witchsblights.WitchsBlights;
import dev.cammiescorner.witchsblights.common.recipes.ShearingRecipe;
import dev.upcraft.sparkweave.api.registry.RegistryHandler;
import dev.upcraft.sparkweave.api.registry.RegistrySupplier;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.RegistryKeys;

public class ModRecipes {
	public static final RegistryHandler<RecipeSerializer<?>> RECIPE_SERIALIZERS = RegistryHandler.create(RegistryKeys.RECIPE_SERIALIZER, WitchsBlights.MOD_ID);

	public static final RegistrySupplier<ShearingRecipe.Serializer> SHEARING_RECIPE = RECIPE_SERIALIZERS.register("shearing_recipe", ShearingRecipe.Serializer::new);
}
