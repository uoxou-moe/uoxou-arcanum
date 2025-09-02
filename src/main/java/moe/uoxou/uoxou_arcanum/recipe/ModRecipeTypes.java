package moe.uoxou.uoxou_arcanum.recipe;

import moe.uoxou.uoxou_arcanum.UoxoUArcanum;
import moe.uoxou.uoxou_arcanum.recipe.alchemy.IAlchemyRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public final class ModRecipeTypes {
	public static final RecipeType<IAlchemyRecipe> ALCHEMY = new RecipeType<>() {};

	public static final RegistryKey<RecipeType<?>> ALCHEMY_KEY = RegistryKey.of(RegistryKeys.RECIPE_TYPE, UoxoUArcanum.identifier("alchemy"));

	public static void init() {
		Registry.register(Registries.RECIPE_TYPE, ALCHEMY_KEY, ALCHEMY);
	}
}
