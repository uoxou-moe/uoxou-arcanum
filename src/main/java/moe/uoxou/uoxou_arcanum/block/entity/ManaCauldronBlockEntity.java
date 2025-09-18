package moe.uoxou.uoxou_arcanum.block.entity;

import moe.uoxou.uoxou_arcanum.recipe.ModRecipeTypes;
import moe.uoxou.uoxou_arcanum.recipe.alchemy.AlchemyRecipeInput;
import moe.uoxou.uoxou_arcanum.recipe.alchemy.IAlchemyRecipeInput;
import moe.uoxou.uoxou_arcanum.recipe.alchemy.ManaAlchemyRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.ServerRecipeManager;
import net.minecraft.util.math.BlockPos;

import java.util.Optional;

public class ManaCauldronBlockEntity extends AbstractAlchemyCauldronBlockEntity<ManaAlchemyRecipe, IAlchemyRecipeInput> {
	public ManaCauldronBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.MANA_CAULDRON, pos, state);
	}

	@Override
	protected IAlchemyRecipeInput toRecipeInput(ItemStack... stack) {
		return new AlchemyRecipeInput(
				Optional.ofNullable(this.getWorld()).map(w -> w.getBlockState(this.getPos().down())).orElse(null),
				this.getContentLevel(),
				stack
		);
	}

	@Override
	protected ServerRecipeManager.MatchGetter<IAlchemyRecipeInput, ManaAlchemyRecipe> getMatchGetter() {
		return ServerRecipeManager.createCachedMatchGetter(ModRecipeTypes.MANA_ALCHEMY);
	}
}
