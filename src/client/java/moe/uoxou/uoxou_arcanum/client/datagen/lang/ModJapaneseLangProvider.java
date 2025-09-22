package moe.uoxou.uoxou_arcanum.client.datagen.lang;

import moe.uoxou.uoxou_arcanum.block.ModBlocks;
import moe.uoxou.uoxou_arcanum.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModJapaneseLangProvider extends FabricLanguageProvider {
	public ModJapaneseLangProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
		super(dataOutput, "ja_jp", registryLookup);
	}

	@Override
	public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
		translationBuilder.add(ModBlocks.MANA_CAULDRON, "マナ入りの大釜");
		translationBuilder.add(ModBlocks.POTION_CAULDRON, "ポーション入りの大釜");

		translationBuilder.add(ModItems.MANA_BOTTLE, "マナ入りの瓶");
		translationBuilder.add(ModItems.EMBELLISHED_GLASS_BOTTLE, "装飾されたガラス瓶");
		translationBuilder.add(ModItems.ARCANE_GOLD_INGOT, "神秘の金インゴット");
	}
}
