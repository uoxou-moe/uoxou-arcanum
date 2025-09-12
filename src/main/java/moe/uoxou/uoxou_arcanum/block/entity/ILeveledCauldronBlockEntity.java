package moe.uoxou.uoxou_arcanum.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeveledCauldronBlock;
import software.bernie.geckolib.animatable.GeoBlockEntity;

import java.util.Optional;

public interface ILeveledCauldronBlockEntity extends GeoBlockEntity {
	Optional<BlockState> getBlock();

	default int getLevel() {
		return this.getBlock()
				.filter(blockState -> blockState.contains(LeveledCauldronBlock.LEVEL))
				.map(blockState -> blockState.get(LeveledCauldronBlock.LEVEL))
				.orElse(0);
	}
}
