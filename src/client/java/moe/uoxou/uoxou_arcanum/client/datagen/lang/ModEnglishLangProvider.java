package moe.uoxou.uoxou_arcanum.client.datagen.lang;

import moe.uoxou.uoxou_arcanum.block.ModBlocks;
import moe.uoxou.uoxou_arcanum.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModEnglishLangProvider extends FabricLanguageProvider {
	public ModEnglishLangProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
		super(dataOutput, "en_us", registryLookup);
	}

	@Override
	public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
		translationBuilder.add(ModBlocks.MANA_CAULDRON, "Mana Cauldron");
		translationBuilder.add(ModBlocks.POTION_CAULDRON, "Potion Cauldron");

		translationBuilder.add(ModItems.MANA_BOTTLE, "Mana Bottle");
		translationBuilder.add(ModItems.EMBELLISHED_GLASS_BOTTLE, "Embellished Glass Bottle");
		translationBuilder.add(ModItems.ARCANE_GOLD_INGOT, "Arcane Gold Ingot");
	}
}
