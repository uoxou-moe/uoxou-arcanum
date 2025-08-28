package moe.uoxou.uoxou_arcanum.block;

import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.gameevent.GameEvent;

public class ManaCauldronBlock extends LayeredCauldronBlock {
	private static final CauldronInteraction.InteractionMap INTERACTIONS = CauldronInteraction.newInteractionMap("mana");
	private static final CauldronInteraction POUR_MANA = (state, world, pos, player, hand, stack) -> {
		if (state.getValue(LayeredCauldronBlock.LEVEL) == 3)
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

		if (!world.isClientSide()) {
			player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
			player.awardStat(Stats.USE_CAULDRON);
			player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
			world.setBlockAndUpdate(pos, state.cycle(LayeredCauldronBlock.LEVEL));
			world.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
			world.gameEvent(null, GameEvent.FLUID_PLACE, pos);
		}

		return ItemInteractionResult.sidedSuccess(world.isClientSide());
	};
	private static final CauldronInteraction SCOOP_MANA = (state, world, pos, player, hand, stack) -> {
		if (!world.isClientSide) {
			player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, PotionContents.createItemStack(Items.POTION, Potions.WATER)));
			player.awardStat(Stats.USE_CAULDRON);
			player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
			LayeredCauldronBlock.lowerFillLevel(state, world, pos);
			world.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
			world.gameEvent(null, GameEvent.FLUID_PICKUP, pos);
		}

		return ItemInteractionResult.sidedSuccess(world.isClientSide());
	};
	private static final CauldronInteraction EMPTY_POUR_MANA = (state, world, pos, player, hand, stack) -> {
		if (!world.isClientSide()) {
			player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
			player.awardStat(Stats.USE_CAULDRON);
			player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
			world.setBlockAndUpdate(pos, ModBlocks.MANA_CAULDRON.defaultBlockState());
			world.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
			world.gameEvent(null, GameEvent.FLUID_PLACE, pos);
		}

		return ItemInteractionResult.sidedSuccess(world.isClientSide());
	};

	public ManaCauldronBlock() {
		super(Biome.Precipitation.NONE, INTERACTIONS, Properties.ofFullCopy(Blocks.CAULDRON));
	}

	static {
		INTERACTIONS.map().put(Items.GLASS_BOTTLE, SCOOP_MANA);
		INTERACTIONS.map().put(Items.POTION, POUR_MANA);
		CauldronInteraction.EMPTY.map().put(Items.POTION, EMPTY_POUR_MANA);
	}
}
