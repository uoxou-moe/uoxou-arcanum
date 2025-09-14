package moe.uoxou.uoxou_arcanum.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractAlchemyCauldronBlockEntity extends BlockEntity implements IAlchemyCauldronBlockEntity {
	public AbstractAlchemyCauldronBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
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
