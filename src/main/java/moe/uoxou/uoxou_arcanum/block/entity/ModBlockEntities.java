package moe.uoxou.uoxou_arcanum.block.entity;

import moe.uoxou.uoxou_arcanum.UoxoUArcanum;
import moe.uoxou.uoxou_arcanum.block.ModBlocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public final class ModBlockEntities {
	public static final BlockEntityType<ManaCauldronBlockEntity> MANA_CAULDRON = BlockEntityType.Builder.create(ManaCauldronBlockEntity::new, ModBlocks.MANA_CAULDRON).build(null);

	public static final RegistryKey<BlockEntityType<?>> KEY_MANA_CAULDRON = RegistryKey.of(RegistryKeys.BLOCK_ENTITY_TYPE, UoxoUArcanum.identifier("mana_cauldron"));

	public static void init() {
		Registry.register(Registries.BLOCK_ENTITY_TYPE, KEY_MANA_CAULDRON, MANA_CAULDRON);
	}
}
