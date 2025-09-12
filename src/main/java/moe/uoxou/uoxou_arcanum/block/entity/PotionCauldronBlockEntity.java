package moe.uoxou.uoxou_arcanum.block.entity;

import moe.uoxou.uoxou_arcanum.util.PotionUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.potion.Potion;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;

public class PotionCauldronBlockEntity extends BlockEntity implements GeoBlockEntity, ILeveledCauldronBlockEntity {
	@Nullable private RegistryKey<Potion> potion;

	public PotionCauldronBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.POTION_CAULDRON, pos, state);

		this.potion = null;
	}

	public Optional<RegistryKey<Potion>> getPotion() {
		return Optional.ofNullable(this.potion);
	}

	public void setPotion(@NotNull RegistryKey<Potion> potion) {
		this.potion = potion;
	}


	@Override
	protected void readData(ReadView readView) {
		super.readData(readView);

		this.potion = readView.getOptionalString("Potion").map(Identifier::tryParse)
				.map(id -> RegistryKey.of(RegistryKeys.POTION, id))
				.orElse(null);
	}

	@Override
	protected void writeData(WriteView writeView) {
		super.writeData(writeView);

		if (this.potion != null) {
			writeView.putString("Potion", this.potion.getValue().toString());
		}
	}

	// これなに
	@Override
	protected void readComponents(ComponentsAccess components) {
		super.readComponents(components);

		this.potion = Optional.ofNullable(components.get(DataComponentTypes.POTION_CONTENTS))
				.flatMap(PotionContentsComponent::potion)
				.flatMap(RegistryEntry::getKey)
				.orElse(null);
	}

	@Override
	protected void addComponents(ComponentMap.Builder componentMapBuilder) {
		super.addComponents(componentMapBuilder);

		if (this.potion != null) {
			componentMapBuilder.add(DataComponentTypes.POTION_CONTENTS, PotionUtils.createPotionComponent(this.potion));
		}
	}

	@Override
	public BlockEntityUpdateS2CPacket toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
		return this.createComponentlessNbt(registryLookup);
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
