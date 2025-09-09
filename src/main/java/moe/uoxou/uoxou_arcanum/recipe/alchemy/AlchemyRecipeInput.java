package moe.uoxou.uoxou_arcanum.recipe.alchemy;

import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public class AlchemyRecipeInput implements IAlchemyRecipeInput {
	public DefaultedList<ItemStack> inputs;

	public AlchemyRecipeInput(ItemStack... inputs) {
		this.inputs = DefaultedList.copyOf(ItemStack.EMPTY, inputs);
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return this.inputs.get(slot);
	}

	@Override
	public int size() {
		return this.inputs.size();
	}
}
