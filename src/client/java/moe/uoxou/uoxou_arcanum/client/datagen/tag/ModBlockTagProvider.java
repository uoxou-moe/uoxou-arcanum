package moe.uoxou.uoxou_arcanum.client.datagen.tag;

import moe.uoxou.uoxou_arcanum.block.ModBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
	public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
		this.valueLookupBuilder(ModBlockTags.ALCHEMY_HEAT_SOURCES)
				.add(Blocks.FIRE)
				.add(Blocks.CAMPFIRE)
				.add(Blocks.LAVA)
				.add(Blocks.MAGMA_BLOCK)
				.addTag(ModBlockTags.ALCHEMY_SOUL_SOURCES);
		this.valueLookupBuilder(ModBlockTags.ALCHEMY_SOUL_SOURCES)
				.add(Blocks.SOUL_FIRE)
				.add(Blocks.SOUL_CAMPFIRE);
	}
}
