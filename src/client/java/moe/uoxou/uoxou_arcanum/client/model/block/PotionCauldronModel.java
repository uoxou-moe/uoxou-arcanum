package moe.uoxou.uoxou_arcanum.client.model.block;

import moe.uoxou.uoxou_arcanum.UoxoUArcanum;
import moe.uoxou.uoxou_arcanum.block.entity.PotionCauldronBlockEntity;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public class PotionCauldronModel extends DefaultedBlockGeoModel<PotionCauldronBlockEntity> {
	public PotionCauldronModel() {
		super(UoxoUArcanum.identifier("potion_cauldron"));
	}
}
