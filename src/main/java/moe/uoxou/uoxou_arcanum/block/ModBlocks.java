package moe.uoxou.uoxou_arcanum.block;

import moe.uoxou.uoxou_arcanum.UoxoUArcanum;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public final class ModBlocks {
	public static final Block MANA_CAULDRON = new ManaCauldronBlock();

	public static final RegistryKey<Block> KEY_MANA_CAULDRON = RegistryKey.of(RegistryKeys.BLOCK, UoxoUArcanum.identifier("mana_cauldron"));

	public static void init() {
		Registry.register(Registries.BLOCK, KEY_MANA_CAULDRON, MANA_CAULDRON);

		Item.BLOCK_ITEMS.put(MANA_CAULDRON, Items.CAULDRON);
	}
}
