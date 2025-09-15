package moe.uoxou.uoxou_arcanum.recipe.alchemy;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public class AlchemyResultEntry implements IAlchemyResultEntry {
	private final ItemStack item;
	private final int weight;
	private final boolean isFailure;

	public AlchemyResultEntry(ItemStack item, int weight, boolean isFailure) {
		this.item = item;
		this.weight = weight;
		this.isFailure = isFailure;
	}

	@Override
	public ItemStack getItem() {
		return this.item;
	}

	@Override
	public int getWeight() {
		return this.weight;
	}

	@Override
	public boolean isFailure() {
		return this.isFailure;
	}

	public static final Codec<IAlchemyResultEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ItemStack.CODEC.fieldOf("item").forGetter(IAlchemyResultEntry::getItem),
			Codec.INT.fieldOf("weight").orElse(1).forGetter(IAlchemyResultEntry::getWeight),
			Codec.BOOL.fieldOf("is_failure").orElse(false).forGetter(IAlchemyResultEntry::isFailure)
	).apply(instance, AlchemyResultEntry::new));

	public static final PacketCodec<RegistryByteBuf, IAlchemyResultEntry> PACKET_CODEC = PacketCodec.tuple(
			ItemStack.PACKET_CODEC,
			IAlchemyResultEntry::getItem,
			PacketCodecs.VAR_INT,
			IAlchemyResultEntry::getWeight,
			PacketCodecs.BOOLEAN,
			IAlchemyResultEntry::isFailure,
			AlchemyResultEntry::new
	);
}
