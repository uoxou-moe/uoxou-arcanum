package moe.uoxou.uoxou_arcanum.client.datagen;

import moe.uoxou.uoxou_arcanum.UoxoUArcanum;
import moe.uoxou.uoxou_arcanum.recipe.alchemy.AlchemyResultEntry;
import moe.uoxou.uoxou_arcanum.recipe.alchemy.IAlchemyRecipe;
import moe.uoxou.uoxou_arcanum.recipe.alchemy.IAlchemyResultEntry;
import moe.uoxou.uoxou_arcanum.recipe.alchemy.ManaAlchemyRecipe;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
	public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
		return new RecipeGenerator(registryLookup, exporter) {
			@Override
			public void generate() {
				alchemy(UoxoUArcanum.identifier("redstone"))
						.input(Items.REDSTONE)
						.input(Items.AMETHYST_SHARD)
						.success(new ItemStack(Items.REDSTONE, 2), 90)
						.failure(new ItemStack(Items.REDSTONE, 1), 10)
						.needHeat()
						.juiceCost(1)
						.offerTo(this.exporter);
			}
		};
	}

	private static AlchemyRecipeBuilder alchemy(Identifier id) {
		return new AlchemyRecipeBuilder(id);
	}

	@Override
	public String getName() {
		return "UoxoU Arcanum Recipe Provider";
	}

	private static class AlchemyRecipeBuilder {
		private final Identifier id;
		private final List<Ingredient> inputs = new ArrayList<>();
		private final List<IAlchemyResultEntry> outputs = new ArrayList<>();
		private IAlchemyRecipe.HeatType heatType = IAlchemyRecipe.HeatType.NONE;
		private int juiceCost = 0;

		public AlchemyRecipeBuilder(Identifier id) {
			this.id = id;
		}

		public AlchemyRecipeBuilder input(ItemConvertible input) {
			this.inputs.add(Ingredient.ofItem(input));

			return this;
		}

		public AlchemyRecipeBuilder success(ItemStack item, int weight) {
			this.outputs.add(new AlchemyResultEntry(item, weight, false));

			return this;
		}

		public AlchemyRecipeBuilder failure(ItemStack item, int weight) {
			this.outputs.add(new AlchemyResultEntry(item, weight, true));

			return this;
		}

		public AlchemyRecipeBuilder needHeat() {
			this.heatType = IAlchemyRecipe.HeatType.FIRE;

			return this;
		}

		public AlchemyRecipeBuilder needSoulHeat() {
			this.heatType = IAlchemyRecipe.HeatType.SOUL_FIRE;

			return this;
		}

		public AlchemyRecipeBuilder juiceCost(int juiceCost) {
			this.juiceCost = juiceCost;

			return this;
		}

		public void offerTo(RecipeExporter exporter) {
			exporter.accept(
					RegistryKey.of(RegistryKeys.RECIPE, this.id),
					new ManaAlchemyRecipe(this.inputs, this.outputs, this.heatType, this.juiceCost),
					null
			);
		}
	}
}
