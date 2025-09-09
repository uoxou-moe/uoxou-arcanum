package moe.uoxou.uoxou_arcanum.recipe.alchemy;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.IngredientPlacement;
import net.minecraft.recipe.Recipe;

import java.util.List;

public interface IAlchemyRecipe extends Recipe<IAlchemyRecipeInput> {
	@Override IngredientPlacement getIngredientPlacement();
	List<Ingredient> getIngredients();
	ItemStack getResult();
}
