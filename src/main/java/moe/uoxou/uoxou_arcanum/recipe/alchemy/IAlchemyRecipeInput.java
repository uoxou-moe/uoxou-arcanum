package moe.uoxou.uoxou_arcanum.recipe.alchemy;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;

import java.util.ArrayList;
import java.util.List;

public interface IAlchemyRecipeInput extends RecipeInput {
	BlockState getHeatSource();

	int getJuiceLevel();

	default List<ItemStack> getAllStacks() {
		List<ItemStack> stacks = new ArrayList<>();

		for (int i = 0; i < this.size(); i++) {
			stacks.add(this.getStackInSlot(i));
		}

		return stacks;
	}
}
