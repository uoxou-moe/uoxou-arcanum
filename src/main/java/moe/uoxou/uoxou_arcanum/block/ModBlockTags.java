package moe.uoxou.uoxou_arcanum.block;

import moe.uoxou.uoxou_arcanum.UoxoUArcanum;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public final class ModBlockTags {
	public static final TagKey<Block> ALCHEMY_HEAT_SOURCES = of("alchemy_heat_sources");
	public static final TagKey<Block> ALCHEMY_SOUL_SOURCES = of("alchemy_soul_sources");

	private static TagKey<Block> of(String id) {
		return TagKey.of(RegistryKeys.BLOCK, UoxoUArcanum.identifier(id));
	}
}
