package moe.uoxou.uoxou_arcanum.recipe.alchemy;

import net.minecraft.item.ItemStack;

public interface IAlchemyResultEntry {
	ItemStack getItem();

	int getWeight();

	boolean isFailure();
}
