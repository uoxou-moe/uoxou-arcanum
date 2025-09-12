package moe.uoxou.uoxou_arcanum.client;

import moe.uoxou.uoxou_arcanum.block.ModBlocks;
import moe.uoxou.uoxou_arcanum.item.ModItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;
import net.minecraft.util.Identifier;

public class ModModelProvider extends FabricModelProvider {
	public ModModelProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
		blockStateModelGenerator.registerBuiltinWithParticle(ModBlocks.MANA_CAULDRON, Identifier.ofVanilla("block/cauldron_side"));
		blockStateModelGenerator.registerBuiltinWithParticle(ModBlocks.POTION_CAULDRON, Identifier.ofVanilla("block/cauldron_side"));
	}

	@Override
	public void generateItemModels(ItemModelGenerator itemModelGenerator) {
		itemModelGenerator.register(ModItems.EMBELLISHED_GLASS_BOTTLE, Models.GENERATED);
		itemModelGenerator.register(ModItems.MANA_BOTTLE, Models.GENERATED);
	}

	@Override
	public String getName() {
		return "UoxoU Arcanum Model Provider";
	}
}