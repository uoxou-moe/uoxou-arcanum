package moe.uoxou.uoxou_arcanum.block.entity;

import moe.uoxou.uoxou_arcanum.recipe.ModRecipeTypes;
import moe.uoxou.uoxou_arcanum.recipe.alchemy.AlchemyRecipeInput;
import moe.uoxou.uoxou_arcanum.recipe.alchemy.IAlchemyRecipe;
import moe.uoxou.uoxou_arcanum.recipe.alchemy.IAlchemyRecipeInput;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
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
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;

public class ManaCauldronBlockEntity extends BlockEntity implements GeoBlockEntity, ILeveledCauldronBlockEntity {
	public int cookingTicks = 0;
	@Nullable public IAlchemyRecipeInput ingredients = null;
	public ItemStack result = ItemStack.EMPTY;

	public ManaCauldronBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.MANA_CAULDRON, pos, state);
	}

	public void tick(World world, BlockPos pos) {
		ItemStack[] stacks = world.getEntitiesByClass(ItemEntity.class, new Box(pos), (i) -> true)
				.stream()
				.map(ItemEntity::getStack)
				.toArray(ItemStack[]::new);
		IAlchemyRecipeInput input = new AlchemyRecipeInput(stacks);

		ServerRecipeManager.MatchGetter<IAlchemyRecipeInput, IAlchemyRecipe> matchGetter = ServerRecipeManager.createCachedMatchGetter(
				ModRecipeTypes.ALCHEMY
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

		if (this.cookingTicks > 0) {
			this.cookingTicks--;
		}

		if (this.cookingTicks == 0 && this.ingredients != null) {
			this.finishCooking(world, pos);
		}
	}

	public void startCooking(RecipeEntry<IAlchemyRecipe> recipe, IAlchemyRecipeInput input, World world) {
		this.cookingTicks = 40;
		this.ingredients = input;
		this.result = recipe.value().craft(input, world.getRegistryManager());
	}

	public void finishCooking(World world, BlockPos pos) {
		if (this.result.isEmpty()) return;

		ItemEntity entity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ(), this.result);
		double random = world.random.nextDouble() * 2.0 * Math.PI;
		entity.addVelocity(Math.sin(random) * 0.1, 0.1, Math.cos(random) * 0.1);
		world.spawnEntity(entity);
		world.playSound(null, pos, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 1.0f, 0.5f);
		this.cookingTicks = 0;
		this.ingredients = null;
		this.result = ItemStack.EMPTY;
	}

	public Optional<BlockState> getBlock() {
		return Optional.ofNullable(this.world).map(w -> w.getBlockState(this.pos));
	}

	// GeckoLib
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

	private static final RawAnimation POUR_ANIMS = RawAnimation.begin().thenPlay("animation.mana_cauldron.pour");
	private static final RawAnimation SCOOP_ANIMS = RawAnimation.begin().thenPlay("animation.mana_cauldron.scoop");
	private static final RawAnimation RIPPLE_ANIMS = RawAnimation.begin().thenPlay("animation.mana_cauldron.ripple");

	private int lastLevelForPourAnim = 0;
	private int lastLevelForRippleAnim = 0;
	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
		controllerRegistrar.add(new AnimationController<>("pour", animTest -> {
			this.getBlock().ifPresent(b -> {
				if (b.contains(net.minecraft.block.LeveledCauldronBlock.LEVEL)) {
					int newLevel = b.get(net.minecraft.block.LeveledCauldronBlock.LEVEL);

					if (this.lastLevelForPourAnim < newLevel) {
						animTest.resetCurrentAnimation();
						animTest.setAnimation(POUR_ANIMS);
					} else if (this.lastLevelForPourAnim > newLevel) {
						animTest.resetCurrentAnimation();
						animTest.setAnimation(SCOOP_ANIMS);
					}

					this.lastLevelForPourAnim = newLevel;
				}
			});
			return PlayState.CONTINUE;
		})).add(new AnimationController<>("ripple", animTest -> {
			this.getBlock().ifPresent(b -> {
				if (b.contains(net.minecraft.block.LeveledCauldronBlock.LEVEL)) {
					if (this.lastLevelForRippleAnim != b.get(net.minecraft.block.LeveledCauldronBlock.LEVEL)) {
						this.lastLevelForRippleAnim = b.get(net.minecraft.block.LeveledCauldronBlock.LEVEL);
						animTest.resetCurrentAnimation();
					}
				}
			});

			return animTest.setAndContinue(RIPPLE_ANIMS);
		}));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}
}
