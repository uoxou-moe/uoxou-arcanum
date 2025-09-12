package moe.uoxou.uoxou_arcanum.client.renderer.block;

import moe.uoxou.uoxou_arcanum.block.entity.ManaCauldronBlockEntity;
import moe.uoxou.uoxou_arcanum.client.model.block.ManaCauldronModel;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class ManaCauldronRenderer extends GeoBlockRenderer<ManaCauldronBlockEntity> {
	public ManaCauldronRenderer(BlockEntityRendererFactory.Context context) {
		super(new ManaCauldronModel());

		// TODO: マナのテクスチャがアニメーションするようにする。今のところ GeckoLib ではアニメーションと発光が同時に使えない
		//       っぽいので一旦保留。
		//       また、既知の問題として、マナのテクスチャがブロックの枠や中のアイテムを透過してしまう。
		//       これも GeckoLib のバグの可能性があるので、一旦放置で。
		this.addRenderLayer(new AutoGlowingGeoLayer<>(this) {
			@Override
			protected boolean shouldRespectWorldLighting(GeoRenderState renderState) {
				return true;
			}
		});
	}
}
