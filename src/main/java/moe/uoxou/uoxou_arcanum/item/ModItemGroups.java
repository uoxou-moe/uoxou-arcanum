package moe.uoxou.uoxou_arcanum.item;

import moe.uoxou.uoxou_arcanum.UoxoUArcanum;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;

public class ModItemGroups {
	public static final RegistryKey<ItemGroup> UOXOU_ARCANUM_KEY = RegistryKey.of(RegistryKeys.ITEM_GROUP, UoxoUArcanum.identifier("uoxou_arcanum"));

	public static final ItemGroup UOXOU_ARCANUM = FabricItemGroup.builder()
			.icon(ModItems.ARCANE_GOLD_INGOT::getDefaultStack)
			.displayName(Text.translatable("itemgroup.uoxou_arcanum.uoxou_arcanum"))
			.build();

	public static void init() {
		Registry.register(Registries.ITEM_GROUP, UOXOU_ARCANUM_KEY, UOXOU_ARCANUM);

		ItemGroupEvents.modifyEntriesEvent(UOXOU_ARCANUM_KEY).register(group -> {
			group.add(ModItems.ARCANE_GOLD_INGOT);
			group.add(ModItems.MANA_BOTTLE);
			group.add(ModItems.EMBELLISHED_GLASS_BOTTLE);
		});
	}
}
