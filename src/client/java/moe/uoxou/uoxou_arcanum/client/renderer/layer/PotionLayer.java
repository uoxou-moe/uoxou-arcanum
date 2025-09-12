package moe.uoxou.uoxou_arcanum.client.renderer.layer;

import moe.uoxou.uoxou_arcanum.client.UoxoUArcanumClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.GeoRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;
import software.bernie.geckolib.renderer.layer.TextureLayerGeoLayer;

public class PotionLayer<T extends GeoAnimatable, O, R extends GeoRenderState> extends TextureLayerGeoLayer<T, O, R> {
	public PotionLayer(GeoRenderer<T, O, R> renderer) {
		super(renderer, MissingSprite.getMissingSpriteId());
	}

	@Override
	protected Identifier getTextureResource(R renderState) {
		return this.renderer.getTextureLocation(renderState).withPath(p -> p.replace(".png", "_potion.png"));
	}

	@Override
	protected RenderLayer getRenderType(R renderState) {
		return AutoGlowingGeoLayer.EmissiveRenderType.getRenderType(this.getTextureResource(renderState), false, true, false);
	}

	@Override
	public void render(R renderState, MatrixStack poseStack, BakedGeoModel bakedModel, @Nullable RenderLayer renderType, VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, int packedLight, int packedOverlay, int renderColor) {
		Potion potion = renderState.getOrDefaultGeckolibData(UoxoUArcanumClient.POTION, Potions.WATER.value());
		int color = PotionContentsComponent.mixColors(potion.getEffects()).orElse(-13083194);

		super.render(renderState, poseStack, bakedModel, renderType, bufferSource, buffer, packedLight, packedOverlay, color);
	}
}
