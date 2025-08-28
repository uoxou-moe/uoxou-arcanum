package moe.uoxou.uoxou_arcanum;

import moe.uoxou.uoxou_arcanum.block.ModBlocks;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;

public class UoxoUArcanum implements ModInitializer {
	public static final String MOD_ID = "uoxou_arcanum";

	@Override
	public void onInitialize() {
		ModBlocks.init();
	}

	public static ResourceLocation identifier(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
