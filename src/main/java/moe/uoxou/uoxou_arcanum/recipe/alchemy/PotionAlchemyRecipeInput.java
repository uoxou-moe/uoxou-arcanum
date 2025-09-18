package moe.uoxou.uoxou_arcanum.recipe.alchemy;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.registry.RegistryKey;

public class PotionAlchemyRecipeInput extends AlchemyRecipeInput implements IPotionAlchemyRecipeInput {
	private final RegistryKey<Potion> potion;

	public PotionAlchemyRecipeInput(RegistryKey<Potion> potion, BlockState heatSource, int juiceLevel, ItemStack... inputs) {
		super(heatSource, juiceLevel, inputs);
		this.potion = potion;
	}

	@Override
	public RegistryKey<Potion> getPotion() {
		return this.potion;
	}
}
