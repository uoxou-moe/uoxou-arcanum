package moe.uoxou.uoxou_arcanum.client.renderer.block;

import moe.uoxou.uoxou_arcanum.block.entity.PotionCauldronBlockEntity;
import moe.uoxou.uoxou_arcanum.client.UoxoUArcanumClient;
import moe.uoxou.uoxou_arcanum.client.model.block.PotionCauldronModel;
import moe.uoxou.uoxou_arcanum.client.renderer.layer.PotionLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.registry.Registries;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class PotionCauldronRenderer extends GeoBlockRenderer<PotionCauldronBlockEntity> {
	public PotionCauldronRenderer(BlockEntityRendererFactory.Context context) {
		super(new PotionCauldronModel());

		this.addRenderLayer(new PotionLayer<>(this));
	}

	@Override
	public void addRenderData(PotionCauldronBlockEntity animatable, Void relatedObject, GeoRenderState renderState) {
		animatable.getPotion().ifPresent(potionKey -> renderState.addGeckolibData(UoxoUArcanumClient.POTION, Registries.POTION.get(potionKey)));
	}
}
