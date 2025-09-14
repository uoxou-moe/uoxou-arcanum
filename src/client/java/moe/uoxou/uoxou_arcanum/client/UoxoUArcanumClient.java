package moe.uoxou.uoxou_arcanum.client;

import moe.uoxou.uoxou_arcanum.block.entity.IAlchemyCauldronBlockEntity;
import moe.uoxou.uoxou_arcanum.block.entity.ModBlockEntities;
import moe.uoxou.uoxou_arcanum.client.renderer.block.ManaCauldronRenderer;
import moe.uoxou.uoxou_arcanum.client.renderer.block.PotionCauldronRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.impl.client.rendering.BlockEntityRendererRegistryImpl;
import net.minecraft.potion.Potion;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.loading.math.MolangQueries;

public class UoxoUArcanumClient implements ClientModInitializer {
	public static final DataTicket<Potion> POTION = DataTicket.create("potion", Potion.class);

	@Override
	@SuppressWarnings("UnstableApiUsage")
	public void onInitializeClient() {
		BlockEntityRendererRegistryImpl.register(ModBlockEntities.MANA_CAULDRON, ManaCauldronRenderer::new);
		BlockEntityRendererRegistryImpl.register(ModBlockEntities.POTION_CAULDRON, PotionCauldronRenderer::new);

		MolangQueries.<IAlchemyCauldronBlockEntity>setActorVariable("query.uoxou_arcanum.content_level", actor -> actor.animatable().getContentLevel());
	}
}
