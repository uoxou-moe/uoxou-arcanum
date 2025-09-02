package moe.uoxou.uoxou_arcanum;

import moe.uoxou.uoxou_arcanum.block.ModBlocks;
import moe.uoxou.uoxou_arcanum.block.entity.ModBlockEntities;
import moe.uoxou.uoxou_arcanum.item.ModItems;
import moe.uoxou.uoxou_arcanum.recipe.ModRecipeSerializers;
import moe.uoxou.uoxou_arcanum.recipe.ModRecipeTypes;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UoxoUArcanum implements ModInitializer {
	public static final String MOD_ID = "uoxou_arcanum";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModBlocks.init();
		ModBlockEntities.init();
		ModItems.init();
		ModRecipeTypes.init();
		ModRecipeSerializers.init();

		LOGGER.info("UoxoU Arcanum initialized");
	}

	public static Identifier identifier(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
