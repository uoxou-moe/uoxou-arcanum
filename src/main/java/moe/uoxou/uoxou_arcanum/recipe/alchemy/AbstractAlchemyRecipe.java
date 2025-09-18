package moe.uoxou.uoxou_arcanum.recipe.alchemy;

import moe.uoxou.uoxou_arcanum.block.ModBlockTags;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.IngredientPlacement;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

public abstract class AbstractAlchemyRecipe<I extends IAlchemyRecipeInput> implements IAlchemyRecipe<I> {
	private final List<Ingredient> ingredients;
	private final List<IAlchemyResultEntry> result;
	private final HeatType heatType;
	private final int juiceCost;

	public AbstractAlchemyRecipe(List<Ingredient> ingredients, List<IAlchemyResultEntry> result, HeatType heatType, int juiceCost) {
		this.ingredients = ingredients;
		this.result = result;
		this.heatType = heatType;
		this.juiceCost = juiceCost;

	}

	@Override
	public boolean matches(I input, World world) {
		if (this.heatType == HeatType.FIRE && !input.getHeatSource().isIn(ModBlockTags.ALCHEMY_HEAT_SOURCES)) {
			return false;
		} else if (this.heatType == HeatType.SOUL_FIRE && !input.getHeatSource().isIn(ModBlockTags.ALCHEMY_SOUL_SOURCES)) {
			return false;
		}

		if (input.getJuiceLevel() < this.juiceCost) {
			return false;
		}

		if (input.size() != this.ingredients.size()) {
			return false;
		}

		Set<Ingredient> remaining = new java.util.HashSet<>(this.ingredients);

			List<ItemStack> inputStacks = input.getAllStacks().stream().map(ItemStack::copy).toList();

			for (Iterator<Ingredient> it = remaining.iterator(); it.hasNext(); ) {
				Ingredient ingredient = it.next();

				for (ItemStack stack : inputStacks) {
					if (stack.isEmpty()) {
						continue;
					}
					if (ingredient.test(stack)) {
						stack.decrement(1);
						it.remove();
						break;
					}
				}
			}


		return remaining.isEmpty();
	}

	@Override
	public ItemStack craft(I input, RegistryWrapper.WrapperLookup registries) {
		int weightsSum =  this.result.stream()
				.map(IAlchemyResultEntry::getWeight)
				.mapToInt(Integer::intValue)
				.sum();

		int randomWeight = new Random().nextInt(weightsSum);

		for (IAlchemyResultEntry entry : this.result) {
			randomWeight -= entry.getWeight();
			if (randomWeight < 0) {
				return entry.getItem().copy();
			}
		}

		return this.result.getFirst().getItem().copy();
	}

	@Override
	public IngredientPlacement getIngredientPlacement() {
		return IngredientPlacement.forShapeless(this.ingredients);
	}

	@Override
	public RecipeBookCategory getRecipeBookCategory() {
		return null;
	}

	@Override
	public List<Ingredient> getIngredients() {
		return this.ingredients;
	}

	@Override
	public List<IAlchemyResultEntry> getResult() {
		return this.result;
	}

	@Override
	public HeatType getHeatType() {
		return this.heatType;
	}

	@Override
	public int getJuiceCost() {
		return this.juiceCost;
	}
}
