package moe.uoxou.uoxou_arcanum.recipe;

import moe.uoxou.uoxou_arcanum.UoxoUArcanum;
import moe.uoxou.uoxou_arcanum.recipe.alchemy.ManaAlchemyRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public final class ModRecipeTypes {
	public static final RecipeType<ManaAlchemyRecipe> MANA_ALCHEMY = new RecipeType<>() {};

	public static final RegistryKey<RecipeType<?>> MANA_ALCHEMY_KEY = RegistryKey.of(RegistryKeys.RECIPE_TYPE, UoxoUArcanum.identifier("mana_alchemy"));

	public static void init() {
		Registry.register(Registries.RECIPE_TYPE, MANA_ALCHEMY_KEY, MANA_ALCHEMY);
	}
}
