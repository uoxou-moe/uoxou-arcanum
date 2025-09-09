package moe.uoxou.uoxou_arcanum.util;

import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;

public final class PotionUtils {
	public static ItemStack createPotionStack(RegistryKey<Potion> potion) {
		return PotionContentsComponent.createStack(Items.POTION, Registries.POTION.getEntry(Registries.POTION.get(potion)));
	}

	public static PotionContentsComponent createPotionComponent(RegistryKey<Potion> potion) {
		return new PotionContentsComponent(Registries.POTION.getEntry(Registries.POTION.get(potion)));
	}
}
