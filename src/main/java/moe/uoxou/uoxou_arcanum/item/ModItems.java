package moe.uoxou.uoxou_arcanum.item;

import moe.uoxou.uoxou_arcanum.UoxoUArcanum;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public final class ModItems {
	public static final Item EMBELLISHED_GLASS_BOTTLE = new Item(new Item.Settings().maxCount(16));
	public static final Item MANA_BOTTLE = new Item(new Item.Settings().maxCount(16).recipeRemainder(EMBELLISHED_GLASS_BOTTLE));

	public static final RegistryKey<Item> EMBELLISHED_GLASS_BOTTLE_KEY = RegistryKey.of(RegistryKeys.ITEM, UoxoUArcanum.identifier("embellished_glass_bottle"));
	public static final RegistryKey<Item> MANA_BOTTLE_KEY = RegistryKey.of(RegistryKeys.ITEM, UoxoUArcanum.identifier("mana_bottle"));

	public static void init() {
		Registry.register(Registries.ITEM, EMBELLISHED_GLASS_BOTTLE_KEY, EMBELLISHED_GLASS_BOTTLE);
		Registry.register(Registries.ITEM, MANA_BOTTLE_KEY, MANA_BOTTLE);
	}
}
