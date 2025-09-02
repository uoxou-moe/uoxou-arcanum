package moe.uoxou.uoxou_arcanum.recipe.alchemy;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moe.uoxou.uoxou_arcanum.UoxoUArcanum;
import moe.uoxou.uoxou_arcanum.recipe.ModRecipeSerializers;
import moe.uoxou.uoxou_arcanum.recipe.ModRecipeTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
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
	public DefaultedList<Ingredient> getIngredients() {
		return DefaultedList.copyOf(Ingredient.EMPTY, this.ingredients.toArray(new Ingredient[0]));
	}

	@Override
	public boolean matches(IAlchemyRecipeInput input, World world) {
		if (input.getSize() != this.ingredients.size()) {
			return false;
		}

		Set<Ingredient> remaining = new java.util.HashSet<>(this.ingredients);

		for (int i = 0; i < input.getSize(); i++) {
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
	public boolean fits(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getResult() {
		return this.result;
	}

	@Override
	public ItemStack getResult(RegistryWrapper.WrapperLookup registriesLookup) {
		return this.result;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.ALCHEMY;
	}

	@Override
	public RecipeType<?> getType() {
		return ModRecipeTypes.ALCHEMY;
	}

	public static class Serializer implements RecipeSerializer<IAlchemyRecipe> {
		private final MapCodec<IAlchemyRecipe> codec;
		private final PacketCodec<RegistryByteBuf, IAlchemyRecipe> packetCodec;

		public Serializer() {
			this.codec = RecordCodecBuilder.mapCodec(instance -> instance.group(
					Ingredient.DISALLOW_EMPTY_CODEC.listOf().fieldOf("ingredients").forGetter(IAlchemyRecipe::getIngredients),
					ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter(IAlchemyRecipe::getResult)
			).apply(instance, AlchemyRecipe::new));
			this.packetCodec = PacketCodec.ofStatic(Serializer::write, Serializer::read);
		}

		@Override
		public MapCodec<IAlchemyRecipe> codec() {
			return this.codec;
		}

		@Override
		public PacketCodec<RegistryByteBuf, IAlchemyRecipe> packetCodec() {
			return this.packetCodec;
		}

		private static IAlchemyRecipe read(RegistryByteBuf buffer) {
			int i = buffer.readVarInt();
			DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(i, Ingredient.EMPTY);
			ingredients.replaceAll(empty -> Ingredient.PACKET_CODEC.decode(buffer));
			ItemStack result = ItemStack.PACKET_CODEC.decode(buffer);

			return new AlchemyRecipe(result, ingredients.toArray(new Ingredient[0]));
		}

		private static void write(RegistryByteBuf buffer, IAlchemyRecipe recipe) {
			buffer.writeVarInt(recipe.getIngredients().size());
			recipe.getIngredients().forEach(ingredient -> {
				Ingredient.PACKET_CODEC.encode(buffer, ingredient);
			});
			ItemStack.PACKET_CODEC.encode(buffer, recipe.getResult());
		}
	}
}
