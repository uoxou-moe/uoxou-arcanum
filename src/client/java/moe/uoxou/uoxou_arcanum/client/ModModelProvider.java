package moe.uoxou.uoxou_arcanum.client;

import moe.uoxou.uoxou_arcanum.UoxoUArcanum;
import moe.uoxou.uoxou_arcanum.block.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.data.client.*;

public class ModModelProvider extends FabricModelProvider {
	public ModModelProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
		blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(ModBlocks.MANA_CAULDRON)
				.coordinate(BlockStateVariantMap.create(LeveledCauldronBlock.LEVEL)
						.register(1, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_LEVEL1
								.upload(ModBlocks.MANA_CAULDRON, "_level1", TextureMap.cauldron(UoxoUArcanum.identifier("block/mana_still")), blockStateModelGenerator.modelCollector)))
						.register(2, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_LEVEL2
								.upload(ModBlocks.MANA_CAULDRON, "_level2", TextureMap.cauldron(UoxoUArcanum.identifier("block/mana_still")), blockStateModelGenerator.modelCollector)))
						.register(3, BlockStateVariant.create().put(VariantSettings.MODEL, Models.TEMPLATE_CAULDRON_FULL
								.upload(ModBlocks.MANA_CAULDRON, "_full", TextureMap.cauldron(UoxoUArcanum.identifier("block/mana_still")), blockStateModelGenerator.modelCollector)))
				)
		);
	}


	@Override
	public void generateItemModels(ItemModelGenerator itemModelGenerator) {
	}

	@Override
	public String getName() {
		return "UoxoU Arcanum Model Provider";
	}
}