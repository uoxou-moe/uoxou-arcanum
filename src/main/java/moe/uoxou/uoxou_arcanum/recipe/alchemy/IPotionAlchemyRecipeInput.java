package moe.uoxou.uoxou_arcanum.recipe.alchemy;

import net.minecraft.potion.Potion;
import net.minecraft.registry.RegistryKey;

public interface IPotionAlchemyRecipeInput extends IAlchemyRecipeInput {
	RegistryKey<Potion> getPotion();
}
