package moe.uoxou.uoxou_arcanum.recipe;

import moe.uoxou.uoxou_arcanum.UoxoUArcanum;
import moe.uoxou.uoxou_arcanum.recipe.alchemy.AlchemyRecipe;
import moe.uoxou.uoxou_arcanum.recipe.alchemy.IAlchemyRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public final class ModRecipeSerializers {
	public static final RecipeSerializer<IAlchemyRecipe> ALCHEMY = new AlchemyRecipe.Serializer();

	public static final RegistryKey<RecipeSerializer<?>> ALCHEMY_KEY = RegistryKey.of(RegistryKeys.RECIPE_SERIALIZER, UoxoUArcanum.identifier("alchemy"));

	public static void init() {
		Registry.register(Registries.RECIPE_SERIALIZER, ALCHEMY_KEY, ALCHEMY);
	}
}
