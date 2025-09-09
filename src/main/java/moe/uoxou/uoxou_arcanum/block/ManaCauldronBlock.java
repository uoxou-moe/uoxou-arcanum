package moe.uoxou.uoxou_arcanum.block;

import moe.uoxou.uoxou_arcanum.block.entity.ManaCauldronBlockEntity;
import moe.uoxou.uoxou_arcanum.item.ModItems;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class ManaCauldronBlock extends LeveledCauldronBlock implements BlockEntityProvider {
	private static final CauldronBehavior.CauldronBehaviorMap INTERACTIONS = CauldronBehavior.createMap("mana");
	private static final CauldronBehavior POUR_MANA = (state, world, pos, player, hand, stack) -> {
		if (state.get(LeveledCauldronBlock.LEVEL) == 3)
			return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;

		if (!world.isClient()) {
			player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(ModItems.EMBELLISHED_GLASS_BOTTLE)));
			player.incrementStat(Stats.USE_CAULDRON);
			player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
			world.setBlockState(pos, state.cycle(LeveledCauldronBlock.LEVEL));
			world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
			world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);

			return ActionResult.SUCCESS;
		}

		return ActionResult.SUCCESS;
	};
	private static final CauldronBehavior SCOOP_MANA = (state, world, pos, player, hand, stack) -> {
		if (!world.isClient()) {
			player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(ModItems.MANA_BOTTLE)));
			player.incrementStat(Stats.USE_CAULDRON);
			player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
			LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
			world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
			world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
		}

		return ActionResult.SUCCESS;
	};
	private static final CauldronBehavior EMPTY_POUR_MANA = (state, world, pos, player, hand, stack) -> {
		if (!world.isClient()) {
			player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(ModItems.EMBELLISHED_GLASS_BOTTLE)));
			player.incrementStat(Stats.USE_CAULDRON);
			player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
			world.setBlockState(pos, ModBlocks.MANA_CAULDRON.getDefaultState());
			world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
			world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
		}

		return ActionResult.SUCCESS;
	};
	private static final CauldronBehavior FAIL_SCOOP_MANA = (state, world, pos, player, hand, stack) -> {
		if (!world.isClient()) {
			player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, ItemStack.EMPTY));
			world.playSound(null, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
		}

		return ActionResult.FAIL;
	};

	public static void init() {
		INTERACTIONS.map().put(ModItems.EMBELLISHED_GLASS_BOTTLE, SCOOP_MANA);
		INTERACTIONS.map().put(Items.GLASS_BOTTLE, FAIL_SCOOP_MANA);
		INTERACTIONS.map().put(ModItems.MANA_BOTTLE, POUR_MANA);
		CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR.map().put(ModItems.MANA_BOTTLE, EMPTY_POUR_MANA);
	}

	public ManaCauldronBlock() {
		super(Biome.Precipitation.NONE, INTERACTIONS, Settings.copy(Blocks.CAULDRON).registryKey(ModBlocks.KEY_MANA_CAULDRON));
	}

	@Override
	public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		if (world.isClient()) {
			return null;
		}

		return (world1, pos, state1, be) -> {
			if (be instanceof ManaCauldronBlockEntity manaCauldron) {
				manaCauldron.tick(world1, pos);
			}
		};
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new ManaCauldronBlockEntity(pos, state);
	}
}
