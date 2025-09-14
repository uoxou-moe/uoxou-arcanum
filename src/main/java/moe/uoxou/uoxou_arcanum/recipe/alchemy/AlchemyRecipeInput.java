package moe.uoxou.uoxou_arcanum.recipe.alchemy;

import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public class AlchemyRecipeInput implements IAlchemyRecipeInput {
	public DefaultedList<ItemStack> inputs;
	private final IAlchemyRecipe.HeatType heatType;
	private final int juiceLevel;

	public AlchemyRecipeInput(IAlchemyRecipe.HeatType heatType, int juiceLevel, ItemStack... inputs) {
		this.inputs = DefaultedList.copyOf(ItemStack.EMPTY, inputs);
		this.heatType = heatType;
		this.juiceLevel = juiceLevel;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.inputs.get(slot);
	}

	@Override
	public int size() {
		return this.inputs.size();
	}

	@Override
	public IAlchemyRecipe.HeatType getHeatType() {
		return this.heatType;
	}

	@Override
	public int getJuiceLevel() {
		return this.juiceLevel;
	}
}
