package moe.uoxou.uoxou_arcanum.recipe.alchemy;

import net.minecraft.block.BlockState;
import net.minecraft.recipe.input.RecipeInput;

public interface IAlchemyRecipeInput extends RecipeInput {
	BlockState getHeatSource();

	int getJuiceLevel();
}
