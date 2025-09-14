package moe.uoxou.uoxou_arcanum.recipe.alchemy;

import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.StringIdentifiable;

import java.util.List;

public interface IAlchemyRecipe extends Recipe<IAlchemyRecipeInput> {
	List<Ingredient> getIngredients();
	List<IAlchemyResultEntry> getResult();
	HeatType getHeatType();
	int getJuiceCost();

	enum HeatType implements StringIdentifiable {
		NONE,
		FIRE,
		SOUL_FIRE,
		;

		@Override
		public String asString() {
			return this.name().toLowerCase();
		}
	}
}
