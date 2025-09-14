package moe.uoxou.uoxou_arcanum.block;

import moe.uoxou.uoxou_arcanum.block.entity.AbstractAlchemyCauldronBlockEntity;
import moe.uoxou.uoxou_arcanum.block.entity.ModBlockEntities;
import moe.uoxou.uoxou_arcanum.block.entity.PotionCauldronBlockEntity;
import moe.uoxou.uoxou_arcanum.util.PotionUtils;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PotionCauldronBlock extends LeveledCauldronBlock implements BlockEntityProvider {
	private static final CauldronBehavior.CauldronBehaviorMap INTERACTIONS = CauldronBehavior.createMap("potion");
	private static final CauldronBehavior POUR_POTION = (state, world, pos, player, hand, stack) -> {
		if (state.get(LeveledCauldronBlock.LEVEL) == 3) return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
		if (!(world.getBlockEntity(pos) instanceof PotionCauldronBlockEntity blockEntity)) return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;

		@Nullable PotionContentsComponent potionContentsComponent = stack.get(DataComponentTypes.POTION_CONTENTS);
		if (potionContentsComponent == null || potionContentsComponent.potion().filter(p -> p.matchesKey(blockEntity.getPotion().orElse(null))).isEmpty()) return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;

		if (!world.isClient()) {
			player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, Items.GLASS_BOTTLE.getDefaultStack()));
			player.incrementStat(Stats.USE_CAULDRON);
			player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
			world.setBlockState(pos, state.cycle(LeveledCauldronBlock.LEVEL));
			world.getBlockEntity(pos, ModBlockEntities.POTION_CAULDRON).ifPresent(AbstractAlchemyCauldronBlockEntity::triggerPourAnimation);
			world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
			world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
		}

		return ActionResult.SUCCESS;
	};
	private static final CauldronBehavior SCOOP_POTION = (state, world, pos, player, hand, stack) -> {
		if (!(world.getBlockEntity(pos) instanceof PotionCauldronBlockEntity blockEntity)) return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
		Optional<RegistryKey<Potion>> potion = blockEntity.getPotion();
		if (potion.isEmpty()) return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;

		if (!world.isClient()) {
			player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, PotionUtils.createPotionStack(potion.get())));
			player.incrementStat(Stats.USE_CAULDRON);
			player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
			LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
			world.getBlockEntity(pos, ModBlockEntities.POTION_CAULDRON).ifPresent(AbstractAlchemyCauldronBlockEntity::triggerScoopAnimation);
			world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
			world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
		}

		return ActionResult.SUCCESS;
	};
	private static final CauldronBehavior EMPTY_POUR_POTION = (state, world, pos, player, hand, stack) -> {
		@Nullable PotionContentsComponent potionContentsComponent = stack.get(DataComponentTypes.POTION_CONTENTS);
		if (potionContentsComponent == null || potionContentsComponent.matches(Potions.WATER)) return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;
		Optional<RegistryEntry<Potion>> potion = potionContentsComponent.potion();
		if (potion.isEmpty()) return ActionResult.PASS_TO_DEFAULT_BLOCK_ACTION;

		if (!world.isClient()) {
			player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, Items.GLASS_BOTTLE.getDefaultStack()));
			player.incrementStat(Stats.USE_CAULDRON);
			player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
			world.setBlockState(pos, ModBlocks.POTION_CAULDRON.getDefaultState());
			if (world.getBlockEntity(pos) instanceof PotionCauldronBlockEntity blockEntity) {
				potionContentsComponent.potion().flatMap(RegistryEntry::getKey)
						.ifPresent(blockEntity::setPotion);
			}
			world.getBlockEntity(pos, ModBlockEntities.POTION_CAULDRON).ifPresent(AbstractAlchemyCauldronBlockEntity::triggerPourAnimation);
			world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
			world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
		}

		return ActionResult.SUCCESS;
	};

	public static void init() {
		INTERACTIONS.map().put(Items.GLASS_BOTTLE, SCOOP_POTION);
		INTERACTIONS.map().put(Items.POTION, POUR_POTION);
		CauldronBehavior.EMPTY_CAULDRON_BEHAVIOR.map().put(Items.POTION, EMPTY_POUR_POTION);
	}

	public PotionCauldronBlock() {
		super(Biome.Precipitation.NONE , INTERACTIONS, Settings.copy(Blocks.CAULDRON).registryKey(ModBlocks.KEY_POTION_CAULDRON));
	}

	@Override
	protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler handler) {
		if (!world.isClient() && entity instanceof LivingEntity living && world.getBlockEntity(pos) instanceof PotionCauldronBlockEntity blockEntity) {
			blockEntity.getPotion().map(Registries.POTION::get)
					.stream()
					.flatMap(p -> p.getEffects().stream())
					.filter(e -> !e.getEffectType().value().isInstant())
					.forEach(e -> living.addStatusEffect(new StatusEffectInstance(e.getEffectType(), 20, e.getAmplifier(), true, true)));
		}

		super.onEntityCollision(state, world, pos, entity, handler);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new PotionCauldronBlockEntity(pos, state);
	}
}
