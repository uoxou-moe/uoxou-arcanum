package moe.uoxou.uoxou_arcanum.recipe.alchemy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import moe.uoxou.uoxou_arcanum.recipe.ModRecipeSerializers;
import moe.uoxou.uoxou_arcanum.recipe.ModRecipeTypes;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.world.World;

import java.util.List;

public class PotionAlchemyRecipe extends AbstractAlchemyRecipe<IPotionAlchemyRecipeInput> {
	private final RegistryEntry<Potion> potion;

	public PotionAlchemyRecipe(List<Ingredient> ingredients, List<IAlchemyResultEntry> result, HeatType heatType, int juiceCost, RegistryEntry<Potion> potion) {
		super(ingredients, result, heatType, juiceCost);
		this.potion = potion;
	}

	@Override
	public boolean matches(IPotionAlchemyRecipeInput input, World world) {
		if (!this.potion.matchesId(input.getPotion().getValue())) {
			return false;
		}

		return super.matches(input, world);
	}

	public RegistryEntry<Potion> getPotion() {
		return this.potion;
	}

	@Override
	public RecipeType<? extends Recipe<IPotionAlchemyRecipeInput>> getType() {
		return ModRecipeTypes.POTION_ALCHEMY;
	}

	@Override
	public RecipeSerializer<? extends Recipe<IPotionAlchemyRecipeInput>> getSerializer() {
		return ModRecipeSerializers.POTION_ALCHEMY;
	}

	public static class Serializer implements RecipeSerializer<PotionAlchemyRecipe> {
		private final MapCodec<PotionAlchemyRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(IAlchemyRecipe::getIngredients),
				AlchemyResultEntry.CODEC.listOf().fieldOf("result").forGetter(IAlchemyRecipe::getResult),
				StringIdentifiable.createCodec(HeatType::values).fieldOf("heat_type").forGetter(IAlchemyRecipe::getHeatType),
				Codec.INT.fieldOf("juice_cost").forGetter(IAlchemyRecipe::getJuiceCost),
				Potion.CODEC.fieldOf("potion").forGetter(PotionAlchemyRecipe::getPotion)
		).apply(instance, PotionAlchemyRecipe::new));
		private final PacketCodec<RegistryByteBuf, PotionAlchemyRecipe> PACKET_CODEC = PacketCodec.tuple(
				Ingredient.PACKET_CODEC.collect(PacketCodecs.toList()),
				IAlchemyRecipe::getIngredients,
				AlchemyResultEntry.PACKET_CODEC.collect(PacketCodecs.toList()),
				IAlchemyRecipe::getResult,
				PacketCodecs.indexed(i -> HeatType.values()[i], HeatType::ordinal),
				IAlchemyRecipe::getHeatType,
				PacketCodecs.VAR_INT,
				IAlchemyRecipe::getJuiceCost,
				Potion.PACKET_CODEC,
				PotionAlchemyRecipe::getPotion,
				PotionAlchemyRecipe::new
		);

		@Override
		public MapCodec<PotionAlchemyRecipe> codec() {
			return CODEC;
		}

		@Override
		public PacketCodec<RegistryByteBuf, PotionAlchemyRecipe> packetCodec() {
			return PACKET_CODEC;
		}
	}
}
