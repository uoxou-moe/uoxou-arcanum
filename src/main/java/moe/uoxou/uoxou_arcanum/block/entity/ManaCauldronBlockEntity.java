package moe.uoxou.uoxou_arcanum.block.entity;

import moe.uoxou.uoxou_arcanum.recipe.ModRecipeTypes;
import moe.uoxou.uoxou_arcanum.recipe.alchemy.AlchemyRecipeInput;
import moe.uoxou.uoxou_arcanum.recipe.alchemy.IAlchemyRecipe;
import moe.uoxou.uoxou_arcanum.recipe.alchemy.IAlchemyRecipeInput;
import moe.uoxou.uoxou_arcanum.recipe.alchemy.ManaAlchemyRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.ServerRecipeManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ManaCauldronBlockEntity extends AbstractAlchemyCauldronBlockEntity {
	public int cookingTicks = 0;
	@Nullable public IAlchemyRecipeInput ingredients = null;
	public ItemStack result = ItemStack.EMPTY;
	@Nullable private ManaAlchemyRecipe cookingRecipe = null;

	private boolean popAnimFlag = false;

	public ManaCauldronBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.MANA_CAULDRON, pos, state);
	}

	public void tick(World world, BlockPos pos) {
		this.tickForRipple();

		if (this.cookingTicks == 0 && this.ingredients == null) {
			ItemStack[] stacks = world.getEntitiesByClass(ItemEntity.class, new Box(pos), (i) -> true)
					.stream()
					.map(ItemEntity::getStack)
					.toArray(ItemStack[]::new);
			IAlchemyRecipeInput input = new AlchemyRecipeInput(IAlchemyRecipe.HeatType.NONE, this.getContentLevel(), stacks);

			ServerRecipeManager.MatchGetter<IAlchemyRecipeInput, ManaAlchemyRecipe> matchGetter = ServerRecipeManager.createCachedMatchGetter(
					ModRecipeTypes.MANA_ALCHEMY
			);

			matchGetter
					.getFirstMatch(input, (ServerWorld) world)
					.ifPresent(r -> {
						this.startCooking(r, input, world);
						r.value().getIngredients().forEach(ing -> {
							for (ItemStack stack : stacks) {
								if (ing.test(stack)) {
									stack.decrement(1);
									break;
								}
							}
						});
					});
		}

		if (this.cookingTicks > 0) {
			this.cookingTicks--;
		}

		// popアニメーションが開始してからpop動作を行うまで15ticks
		if (this.cookingTicks < 15 && this.popAnimFlag) {
			this.triggerPopAnimation();
			this.popAnimFlag = false;
		}

		if (this.cookingTicks == 0 && this.ingredients != null) {
			this.finishCooking(world, pos);
		}
	}

	public void startCooking(RecipeEntry<? extends IAlchemyRecipe> recipe, IAlchemyRecipeInput input, World world) {
		this.cookingTicks = 40;
		this.ingredients = input;
		this.result = recipe.value().craft(input, world.getRegistryManager());
		this.cookingRecipe = (ManaAlchemyRecipe) recipe.value();

		this.popAnimFlag = true;
	}

	public void finishCooking(World world, BlockPos pos) {
		if (this.result.isEmpty() || this.cookingRecipe == null) return;

		//再チェック

		ItemEntity itemEntity = new ItemEntity(
				world,
				pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
				this.result
		);

		// 速度: X/Zはほんの少しランダム, Yはより上向き
		itemEntity.setVelocity(
				world.random.nextTriangular(0.0, 0.5),   // 横ぶれ小さめ
				world.random.nextTriangular(0.5, 0.1),    // 上方向を強めに
				world.random.nextTriangular(0.0, 0.5)
		);

		world.spawnEntity(itemEntity);
		for (int i = 0; i < this.cookingRecipe.getJuiceCost(); i++) {
			LeveledCauldronBlock.decrementFluidLevel(world.getBlockState(pos), world, pos);
		}
		world.playSound(null, pos, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 1.0f, 0.5f);
		this.cookingTicks = 0;
		this.ingredients = null;
		this.cookingRecipe = null;
		this.result = ItemStack.EMPTY;
	}
}
