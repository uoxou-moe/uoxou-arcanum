package moe.uoxou.uoxou_arcanum.block.entity;

import moe.uoxou.uoxou_arcanum.UoxoUArcanum;
import moe.uoxou.uoxou_arcanum.recipe.alchemy.IAlchemyRecipe;
import moe.uoxou.uoxou_arcanum.recipe.alchemy.IAlchemyRecipeInput;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
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
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractAlchemyCauldronBlockEntity<T extends IAlchemyRecipe<I> ,I extends IAlchemyRecipeInput> extends BlockEntity implements IAlchemyCauldronBlockEntity {
	public int cookingTicks = 0;
	@Nullable public List<ItemStack> inputs = null;
	@Nullable public T currentRecipe = null;

	private boolean popAnimFlag = false;

	public AbstractAlchemyCauldronBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	protected abstract I toRecipeInput(ItemStack... stack);

	protected abstract ServerRecipeManager.MatchGetter<I, T> getMatchGetter();

	public void tick(World world, BlockPos pos) {
		this.tickForRipple();

		if (this.cookingTicks == 0 && this.inputs == null) {
			ItemStack[] stacks = world.getEntitiesByClass(ItemEntity.class, new Box(pos), (i) -> true)
					.stream()
					.map(ItemEntity::getStack)
					.toArray(ItemStack[]::new);

			if (stacks.length > 0) {
				I input = this.toRecipeInput(stacks);
				ServerRecipeManager.MatchGetter<I, T> matchGetter = this.getMatchGetter();

				matchGetter
						.getFirstMatch(input, (ServerWorld) world)
						.ifPresent(r -> {
							List<ItemStack> ingredients = r.value().getIngredients().stream()
									.map(ing -> {
										for (ItemStack stack : stacks) {
											if (ing.test(stack)) {
												return stack.split(1);
											}
										}
										return ItemStack.EMPTY;
									})
									.filter(stack -> !stack.isEmpty())
									.toList();

							this.startCooking(r, ingredients.toArray(ItemStack[]::new), world);
						});
			}
		}

		if (this.cookingTicks > 0) {
			this.cookingTicks--;
		}

		// popアニメーションが開始してからpop動作を行うまで15ticks
		if (this.cookingTicks < 15 && this.popAnimFlag) {
			this.triggerPopAnimation();
			this.popAnimFlag = false;
		}

		if (this.cookingTicks == 0 && this.inputs != null) {
			this.finishCooking(world, pos);
		}
	}

	public void startCooking(RecipeEntry<T> recipe, ItemStack[] ingredients, World world) {
		this.cookingTicks = 40;
		this.inputs = List.of(ingredients);
		this.currentRecipe = recipe.value();

		this.popAnimFlag = true;
	}

	public void finishCooking(World world, BlockPos pos) {
		if (this.inputs == null || this.currentRecipe == null ) return;

		I input = this.toRecipeInput(this.inputs.toArray(new ItemStack[0]));
		if (!this.currentRecipe.matches(input, world)) {
			UoxoUArcanum.LOGGER.warn("Recipe mismatch! This should not happen.");
			this.cookingTicks = 0;
			this.inputs = null;
			this.currentRecipe = null;
			return;
		}
		ItemStack result = this.currentRecipe.craft(input, world.getRegistryManager());

		ItemEntity itemEntity = new ItemEntity(
				world,
				pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
				result
		);

		// 速度: X/Zはほんの少しランダム, Yはより上向き
		itemEntity.setVelocity(
				world.random.nextTriangular(0.0, 0.5),   // 横ぶれ小さめ
				world.random.nextTriangular(0.5, 0.1),    // 上方向を強めに
				world.random.nextTriangular(0.0, 0.5)
		);

		world.spawnEntity(itemEntity);
		for (int i = 0; i < this.currentRecipe.getJuiceCost(); i++) {
			LeveledCauldronBlock.decrementFluidLevel(world.getBlockState(pos), world, pos);
		}
		world.playSound(null, pos, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 1.0f, 0.5f);
		this.cookingTicks = 0;
		this.inputs = null;
		this.currentRecipe = null;
	}

	@Override
	public int getContentLevel() {
		return Optional.ofNullable(this.getWorld()).map(world -> world.getBlockState(this.getPos()))
				.filter(block -> block.contains(LeveledCauldronBlock.LEVEL))
				.map(block -> block.get(LeveledCauldronBlock.LEVEL))
				.orElse(0);
	}

	private final List<Entity> lastCollidedEntities = new ArrayList<>();

	protected void tickForRipple() {
		List<Entity> collidedEntities = Optional.ofNullable(this.getWorld())
				.map(world -> world.getEntitiesByClass(Entity.class, new Box(this.getPos()), e -> true))
				.orElse(List.of());

		collidedEntities.forEach(e -> {
			if (!this.lastCollidedEntities.contains(e)) {
				this.triggerRippleAnimation();
				this.lastCollidedEntities.clear();
				this.lastCollidedEntities.addAll(collidedEntities);
			}
		});
	}

	protected final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

	private static final RawAnimation ANIMATION_IDLE = RawAnimation.begin().thenPlay("animation.mana_cauldron.set_height");
	private static final RawAnimation ANIMATION_POUR = RawAnimation.begin().thenPlay("animation.mana_cauldron.pour");
	private static final RawAnimation ANIMATION_SCOOP = RawAnimation.begin().thenPlay("animation.mana_cauldron.scoop");
	private static final RawAnimation ANIMATION_RIPPLE = RawAnimation.begin().thenPlay("animation.mana_cauldron.ripple");
	private static final RawAnimation ANIMATION_POP = RawAnimation.begin().thenPlay("animation.mana_cauldron.pop");

	private static final String KEY_CONTROLLER_IDLE = "idle";
	private static final String KEY_CONTROLLER_ADJUST = "adjust";
	private static final String KEY_CONTROLLER_RIPPLE = "ripple";
	private static final String KEY_CONTROLLER_POP = "pop";
	private static final String KEY_TRIGGER_POUR = "pour";
	private static final String KEY_TRIGGER_SCOOP = "scoop";
	private static final String KEY_TRIGGER_RIPPLE = "ripple";
	private static final String KEY_TRIGGER_POP = "pop";

	public void triggerPourAnimation() {
		this.triggerAnim(KEY_CONTROLLER_ADJUST, KEY_TRIGGER_POUR);
		this.triggerRippleAnimation();
	}

	public void triggerScoopAnimation() {
		this.triggerAnim(KEY_CONTROLLER_ADJUST, KEY_TRIGGER_SCOOP);
		this.triggerRippleAnimation();
	}

	public void triggerRippleAnimation() {
		this.triggerAnim(KEY_CONTROLLER_RIPPLE, KEY_TRIGGER_RIPPLE);
	}

	public void triggerPopAnimation() {
		this.triggerAnim(KEY_TRIGGER_POP, KEY_TRIGGER_POP);
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
		controllerRegistrar
				.add(new AnimationController<>(KEY_CONTROLLER_IDLE, animTest -> animTest.setAndContinue(ANIMATION_IDLE)))
				.add(new AnimationController<>(KEY_CONTROLLER_ADJUST, animTest -> PlayState.STOP)
						.triggerableAnim(KEY_TRIGGER_POUR, ANIMATION_POUR)
						.triggerableAnim(KEY_TRIGGER_SCOOP, ANIMATION_SCOOP))
				.add(new AnimationController<>(KEY_CONTROLLER_RIPPLE, animTest -> PlayState.STOP)
						.triggerableAnim(KEY_TRIGGER_RIPPLE, ANIMATION_RIPPLE))
				.add(new AnimationController<>(KEY_CONTROLLER_POP, animTest -> PlayState.STOP)
						.triggerableAnim(KEY_TRIGGER_POP, ANIMATION_POP))
		;
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}
}
