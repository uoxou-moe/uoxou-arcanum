package moe.uoxou.uoxou_arcanum.block.entity;

import moe.uoxou.uoxou_arcanum.UoxoUArcanum;
import moe.uoxou.uoxou_arcanum.block.ModBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public final class ModBlockEntities {
	public static final BlockEntityType<ManaCauldronBlockEntity> MANA_CAULDRON = FabricBlockEntityTypeBuilder.create(ManaCauldronBlockEntity::new, ModBlocks.MANA_CAULDRON).build();
	public static final BlockEntityType<PotionCauldronBlockEntity> POTION_CAULDRON = FabricBlockEntityTypeBuilder.create(PotionCauldronBlockEntity::new, ModBlocks.POTION_CAULDRON).build();

	public static final RegistryKey<BlockEntityType<?>> KEY_MANA_CAULDRON = RegistryKey.of(RegistryKeys.BLOCK_ENTITY_TYPE, UoxoUArcanum.identifier("mana_cauldron"));
	public static final RegistryKey<BlockEntityType<?>> KEY_POTION_CAULDRON = RegistryKey.of(RegistryKeys.BLOCK_ENTITY_TYPE, UoxoUArcanum.identifier("potion_cauldron"));

	public static void init() {
		Registry.register(Registries.BLOCK_ENTITY_TYPE, KEY_MANA_CAULDRON, MANA_CAULDRON);
		Registry.register(Registries.BLOCK_ENTITY_TYPE, KEY_POTION_CAULDRON, POTION_CAULDRON);
	}
}
