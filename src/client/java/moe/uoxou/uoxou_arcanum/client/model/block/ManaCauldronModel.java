package moe.uoxou.uoxou_arcanum.client.model.block;

import moe.uoxou.uoxou_arcanum.UoxoUArcanum;
import moe.uoxou.uoxou_arcanum.block.entity.ManaCauldronBlockEntity;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public class ManaCauldronModel extends DefaultedBlockGeoModel<ManaCauldronBlockEntity> {
	public ManaCauldronModel() {
		super(UoxoUArcanum.identifier("mana_cauldron"));
	}
}
