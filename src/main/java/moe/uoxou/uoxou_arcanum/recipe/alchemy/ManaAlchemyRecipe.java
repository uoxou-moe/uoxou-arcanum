package moe.uoxou.uoxou_arcanum.recipe.alchemy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moe.uoxou.uoxou_arcanum.recipe.ModRecipeSerializers;
import moe.uoxou.uoxou_arcanum.recipe.ModRecipeTypes;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.StringIdentifiable;

import java.util.List;

public class ManaAlchemyRecipe extends AbstractAlchemyRecipe {
	public ManaAlchemyRecipe(List<Ingredient> ingredients, List<IAlchemyResultEntry> result, HeatType heatType, int juiceCost) {
		super(ingredients, result, heatType, juiceCost);
	}

	@Override
	public RecipeType<? extends Recipe<IAlchemyRecipeInput>> getType() {
		return ModRecipeTypes.MANA_ALCHEMY;
	}

	@Override
	public RecipeSerializer<? extends Recipe<IAlchemyRecipeInput>> getSerializer() {
		return ModRecipeSerializers.MANA_ALCHEMY;
	}

	public static class Serializer implements RecipeSerializer<IAlchemyRecipe> {
		private final MapCodec<IAlchemyRecipe> codec;
		private final PacketCodec<RegistryByteBuf, IAlchemyRecipe> packetCodec;

		public Serializer() {
			this.codec = RecordCodecBuilder.mapCodec(instance -> instance.group(
					Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(IAlchemyRecipe::getIngredients),
					AlchemyResultEntry.CODEC.listOf().fieldOf("result").forGetter(IAlchemyRecipe::getResult),
					StringIdentifiable.createCodec(HeatType::values).fieldOf("heat_type").forGetter(IAlchemyRecipe::getHeatType),
					Codec.INT.fieldOf("juice_cost").forGetter(IAlchemyRecipe::getJuiceCost)
			).apply(instance, ManaAlchemyRecipe::new));
			this.packetCodec = PacketCodec.tuple(
					Ingredient.PACKET_CODEC.collect(PacketCodecs.toList()),
					IAlchemyRecipe::getIngredients,
					AlchemyResultEntry.PACKET_CODEC.collect(PacketCodecs.toList()),
					IAlchemyRecipe::getResult,
					PacketCodecs.indexed(i -> HeatType.values()[i], HeatType::ordinal),
					IAlchemyRecipe::getHeatType,
					PacketCodecs.VAR_INT,
					IAlchemyRecipe::getJuiceCost,
					ManaAlchemyRecipe::new
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
