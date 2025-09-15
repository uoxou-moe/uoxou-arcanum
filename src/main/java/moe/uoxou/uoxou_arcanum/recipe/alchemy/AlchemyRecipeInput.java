package moe.uoxou.uoxou_arcanum.recipe.alchemy;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public class AlchemyRecipeInput implements IAlchemyRecipeInput {
	public DefaultedList<ItemStack> inputs;
	private final BlockState heatSource;
	private final int juiceLevel;

	public AlchemyRecipeInput(BlockState heatSource, int juiceLevel, ItemStack... inputs) {
		this.inputs = DefaultedList.copyOf(ItemStack.EMPTY, inputs);
		this.heatSource = heatSource;
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
	public BlockState getHeatSource() {
		return this.heatSource;
	}

	@Override
	public int getJuiceLevel() {
		return this.juiceLevel;
	}
}
