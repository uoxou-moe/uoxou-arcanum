package moe.uoxou.uoxou_arcanum.recipe.alchemy;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moe.uoxou.uoxou_arcanum.recipe.ModRecipeSerializers;
import moe.uoxou.uoxou_arcanum.recipe.ModRecipeTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class AlchemyRecipe implements IAlchemyRecipe {
	private final Set<Ingredient> ingredients;
	private final ItemStack result;

	public AlchemyRecipe(Set<Ingredient> ingredients, ItemStack result) {
		this.ingredients = ingredients;
		this.result = result;
	}

	public AlchemyRecipe(List<Ingredient> ingredients, ItemStack result) {
		this.ingredients = Set.copyOf(ingredients);
		this.result = result;
	}

	public AlchemyRecipe(ItemStack result, Ingredient... ingredients) {
		this.ingredients = Set.of(ingredients);
		this.result = result;
	}

	@Override
	public IngredientPlacement getIngredientPlacement() {
		return IngredientPlacement.forShapeless(this.ingredients.stream().toList());
	}

	@Override
	public List<Ingredient> getIngredients() {
		return this.ingredients.stream().toList();
	}

	@Override
	public RecipeBookCategory getRecipeBookCategory() {
		return null;
	}

	@Override
	public boolean matches(IAlchemyRecipeInput input, World world) {
		if (input.size() != this.ingredients.size()) {
			return false;
		}

		Set<Ingredient> remaining = new java.util.HashSet<>(this.ingredients);

		for (int i = 0; i < input.size(); i++) {
			ItemStack stack = input.getStackInSlot(i);

			boolean matched = false;
			for (Iterator<Ingredient> it = remaining.iterator(); it.hasNext(); ) {
				Ingredient ingredient = it.next();
				if (ingredient.test(stack)) {
					it.remove();
					matched = true;
					break;
				}
			}

			if (!matched) {
				return false;
			}
		}

		return remaining.isEmpty();
	}

	@Override
	public ItemStack craft(IAlchemyRecipeInput input, RegistryWrapper.WrapperLookup lookup) {
		return this.result.copy();
	}

	@Override
	public RecipeSerializer<? extends Recipe<IAlchemyRecipeInput>> getSerializer() {
		return ModRecipeSerializers.ALCHEMY;
	}

	@Override
	public RecipeType<? extends Recipe<IAlchemyRecipeInput>> getType() {
		return ModRecipeTypes.ALCHEMY;
	}

	@Override
	public ItemStack getResult() {
		return this.result;
	}

	public static class Serializer implements RecipeSerializer<IAlchemyRecipe> {
		private final MapCodec<IAlchemyRecipe> codec;
		private final PacketCodec<RegistryByteBuf, IAlchemyRecipe> packetCodec;

		public Serializer() {
			this.codec = RecordCodecBuilder.mapCodec(instance -> instance.group(
					Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(IAlchemyRecipe::getIngredients),
					ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter(IAlchemyRecipe::getResult)
			).apply(instance, AlchemyRecipe::new));
			this.packetCodec = PacketCodec.tuple(
					Ingredient.PACKET_CODEC.collect(PacketCodecs.toList()),
					IAlchemyRecipe::getIngredients,
					ItemStack.PACKET_CODEC,
					IAlchemyRecipe::getResult,
					AlchemyRecipe::new
			);
		}

		@Override
		public MapCodec<IAlchemyRecipe> codec() {
			return this.codec;
		}

		@Override
		public PacketCodec<RegistryByteBuf, IAlchemyRecipe> packetCodec() {
			return this.packetCodec;
		}
	}
}
