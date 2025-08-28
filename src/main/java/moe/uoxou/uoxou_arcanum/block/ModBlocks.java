package moe.uoxou.uoxou_arcanum.block;

import moe.uoxou.uoxou_arcanum.UoxoUArcanum;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

public final class ModBlocks {
	public static final Block MANA_CAULDRON = new ManaCauldronBlock();

	public static final ResourceKey<Block> KEY_MANA_CAULDRON = ResourceKey.create(Registries.BLOCK, UoxoUArcanum.identifier("mana_cauldron"));

	public static void init() {
		Registry.register(BuiltInRegistries.BLOCK, KEY_MANA_CAULDRON, MANA_CAULDRON);

		Item.BY_BLOCK.put(MANA_CAULDRON, Items.CAULDRON);
	}
}
