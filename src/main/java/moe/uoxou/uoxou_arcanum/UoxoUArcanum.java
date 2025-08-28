package moe.uoxou.uoxou_arcanum;

import moe.uoxou.uoxou_arcanum.block.ModBlocks;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class UoxoUArcanum implements ModInitializer {
	public static final String MOD_ID = "uoxou_arcanum";

	@Override
	public void onInitialize() {
		ModBlocks.init();
	}

	public static Identifier identifier(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
