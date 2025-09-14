package moe.uoxou.uoxou_arcanum.recipe;

import moe.uoxou.uoxou_arcanum.UoxoUArcanum;
import moe.uoxou.uoxou_arcanum.recipe.alchemy.IAlchemyRecipe;
import moe.uoxou.uoxou_arcanum.recipe.alchemy.ManaAlchemyRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public final class ModRecipeSerializers {
	public static final RecipeSerializer<IAlchemyRecipe> MANA_ALCHEMY = new ManaAlchemyRecipe.Serializer();

	public static final RegistryKey<RecipeSerializer<?>> MANA_ALCHEMY_KEY = RegistryKey.of(RegistryKeys.RECIPE_SERIALIZER, UoxoUArcanum.identifier("mana_alchemy"));

	public static void init() {
		Registry.register(Registries.RECIPE_SERIALIZER, MANA_ALCHEMY_KEY, MANA_ALCHEMY);
	}
}
