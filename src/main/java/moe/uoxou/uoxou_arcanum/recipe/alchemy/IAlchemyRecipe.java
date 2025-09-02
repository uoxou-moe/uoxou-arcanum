package moe.uoxou.uoxou_arcanum.recipe.alchemy;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.collection.DefaultedList;

public interface IAlchemyRecipe extends Recipe<IAlchemyRecipeInput> {
	@Override DefaultedList<Ingredient> getIngredients();
	ItemStack getResult();
}
