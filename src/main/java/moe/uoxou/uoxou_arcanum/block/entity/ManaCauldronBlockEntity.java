package moe.uoxou.uoxou_arcanum.block.entity;

import moe.uoxou.uoxou_arcanum.UoxoUArcanum;
import moe.uoxou.uoxou_arcanum.recipe.ModRecipeTypes;
import moe.uoxou.uoxou_arcanum.recipe.alchemy.AlchemyRecipeInput;
import moe.uoxou.uoxou_arcanum.recipe.alchemy.IAlchemyRecipeInput;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.Optional;

public class ManaCauldronBlockEntity extends BlockEntity {
	public ManaCauldronBlockEntity(BlockPos pos, BlockState state) {
		super(ModBlockEntities.MANA_CAULDRON, pos, state);
	}

	public void tick(World world, BlockPos pos) {
		ItemStack[] stacks = world.getEntitiesByClass(ItemEntity.class, new Box(pos), (i) -> true)
				.stream()
				.map(ItemEntity::getStack)
				.toArray(ItemStack[]::new);
		IAlchemyRecipeInput input = new AlchemyRecipeInput(stacks);

		Optional<ItemStack> result = world.getRecipeManager()
				.getFirstMatch(ModRecipeTypes.ALCHEMY, input, world)
				.map(r -> r.value().craft(input, world.getRegistryManager()));

		if (result.isPresent()) {
			for (ItemStack stack : stacks) {
				stack.decrement(1);
			}
			ItemEntity entity = new ItemEntity(world, pos.getX() + 2, pos.getY() + 1, pos.getZ() + 0.5, result.get());
			world.spawnEntity(entity);
		}
	}
}
