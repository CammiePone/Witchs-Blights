package dev.cammiescorner.witchsblights.common.recipes;

import com.mojang.serialization.MapCodec;
import dev.cammiescorner.witchsblights.common.registries.ModRecipes;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.recipe.input.CraftingRecipeInput;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class ShearingRecipe extends ShapelessRecipe {
	public ShearingRecipe(String group, CraftingRecipeCategory category, ItemStack result, DefaultedList<Ingredient> ingredients) {
		super(group, category, result, ingredients);
	}

	@Override
	public boolean matches(CraftingRecipeInput input, World world) {
		ItemStack shears = ItemStack.EMPTY;

		for(int i = 0; i < input.getSize(); ++i) {
			ItemStack stack = input.getStackInSlot(i);

			if(stack.isIn(ConventionalItemTags.SHEAR_TOOLS)) {
				shears = stack.copy();
				break;
			}
		}

		return !shears.isEmpty() && super.matches(input, world);
	}

	@Override
	public boolean fits(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public DefaultedList<ItemStack> getRemainder(CraftingRecipeInput input) {
		DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(input.getSize(), ItemStack.EMPTY);

		for(int i = 0; i < defaultedList.size(); i++) {
			ItemStack stack = input.getStackInSlot(i);

			if(stack.isIn(ConventionalItemTags.SHEAR_TOOLS)) {
				if(stack.isDamageable())
					stack.setDamage(stack.getDamage() + 1);

				defaultedList.set(i, stack.copy());
			}
		}

		return defaultedList;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipes.SHEARING_RECIPE.get();
	}

	public static final class Serializer implements RecipeSerializer<ShearingRecipe> {
		private static final MapCodec<ShearingRecipe> CODEC = RecipeSerializer.SHAPELESS.codec().xmap(shapelessRecipe -> {
			DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(shapelessRecipe.getIngredients().size() + 1, Ingredient.EMPTY);

			for(int i = 0; i < shapelessRecipe.getIngredients().size(); i++)
				ingredients.set(i + 1, shapelessRecipe.getIngredients().get(i));

			ingredients.set(0, Ingredient.fromTag(ConventionalItemTags.SHEAR_TOOLS));

			return new ShearingRecipe(shapelessRecipe.getGroup(), shapelessRecipe.getCategory(), shapelessRecipe.result, ingredients);
		}, shearingRecipe -> {
			DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(shearingRecipe.getIngredients().size() - 1, Ingredient.EMPTY);

			for(int i = 0; i < ingredients.size(); i++)
				ingredients.set(i, shearingRecipe.getIngredients().get(i + 1));

			return new ShapelessRecipe(shearingRecipe.getGroup(), shearingRecipe.getCategory(), shearingRecipe.result, ingredients);
		});
		private static final PacketCodec<RegistryByteBuf, ShearingRecipe> PACKET_CODEC = RecipeSerializer.SHAPELESS.packetCodec().xmap(shapelessRecipe -> {
			DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(shapelessRecipe.getIngredients().size() + 1, Ingredient.EMPTY);

			for(int i = 0; i < shapelessRecipe.getIngredients().size(); i++)
				ingredients.set(i + 1, shapelessRecipe.getIngredients().get(i));

			ingredients.set(0, Ingredient.fromTag(ConventionalItemTags.SHEAR_TOOLS));

			return new ShearingRecipe(shapelessRecipe.getGroup(), shapelessRecipe.getCategory(), shapelessRecipe.result, ingredients);
		}, shearingRecipe -> {
			DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(shearingRecipe.getIngredients().size() - 1, Ingredient.EMPTY);

			for(int i = 0; i < ingredients.size(); i++)
				ingredients.set(i, shearingRecipe.getIngredients().get(i + 1));

			return new ShapelessRecipe(shearingRecipe.getGroup(), shearingRecipe.getCategory(), shearingRecipe.result, ingredients);
		});

		@Override
		public MapCodec<ShearingRecipe> codec() {
			return CODEC;
		}

		@Override
		public PacketCodec<RegistryByteBuf, ShearingRecipe> packetCodec() {
			return PACKET_CODEC;
		}
	}
}
