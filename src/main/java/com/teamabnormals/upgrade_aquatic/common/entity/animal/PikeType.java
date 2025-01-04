package com.teamabnormals.upgrade_aquatic.common.entity.animal;

import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.Tags;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;

/**
 * @author SmellyModder (Luke Tonon)
 */
public enum PikeType {
	AMUR(1, PikeSize.SMALL, PikeRarity.UNCOMMON, null, Tags.Biomes.IS_SWAMP),
	REDFIN_PICKEREL(2, PikeSize.SMALL, PikeRarity.COMMON, null, null),
	BROWN_NORTHERN(3, PikeSize.LARGE, PikeRarity.COMMON, 8, null),
	MAHOGANY_NORTHERN(4, PikeSize.LARGE, PikeRarity.UNCOMMON, 9, null),
	JADE_NORTHERN(5, PikeSize.LARGE, PikeRarity.RARE, 10, null),
	OLIVE_NORTHERN(6, PikeSize.LARGE, PikeRarity.EPIC, 11, null),
	SPECTRAL(7, PikeSize.LARGE, PikeRarity.LEGENDARY, null, null),
	SPOTTED_BROWN_NORTHERN(8, PikeSize.LARGE, PikeRarity.COMMON, null, null),
	SPOTTED_MAHOGANY_NORTHERN(9, PikeSize.LARGE, PikeRarity.UNCOMMON, null, null),
	SPOTTED_JADE_NORTHERN(10, PikeSize.LARGE, PikeRarity.RARE, null, null),
	SPOTTED_OLIVE_NORTHERN(11, PikeSize.LARGE, PikeRarity.EPIC, null, null),
	SUPERCHARGED(12, PikeSize.LARGE, PikeRarity.LEGENDARY, null, null),
	OBSIDIAN(13, PikeSize.LARGE, PikeRarity.LEGENDARY, null, null),
	MUSKELLUNGE(14, PikeSize.HUGE, PikeRarity.EPIC, null, BiomeTags.IS_RIVER),
	CHAIN_PICKEREL(15, PikeSize.SMALL, PikeRarity.COMMON, null, BiomeTags.IS_RIVER),
	GRASS_PICKEREL(16, PikeSize.SMALL, PikeRarity.COMMON, null, Tags.Biomes.IS_SWAMP),
	BLACK_SOUTHERN(17, PikeSize.MEDIUM, PikeRarity.COMMON, null, null),
	EBONY_SOUTHERN(18, PikeSize.MEDIUM, PikeRarity.UNCOMMON, null, null),
	MUSTARD_SOUTHERN(19, PikeSize.MEDIUM, PikeRarity.RARE, null, null),
	LEMON_SOUTHERN(20, PikeSize.MEDIUM, PikeRarity.RARE, null, null),
	GOLDEN_SOUTHERN(21, PikeSize.MEDIUM, PikeRarity.EPIC, null, null);

	public final int id;
	public final PikeSize pikeSize;
	public final PikeRarity rarity;
	@Nullable
	private final Integer spottedVariant;
	@Nullable
	private final TagKey<Biome> biomeCategory;

	PikeType(int id, PikeSize pikeSize, PikeRarity rarity, @Nullable Integer spottedVariant, @Nullable TagKey<Biome> biomeCategory) {
		this.id = id;
		this.pikeSize = pikeSize;
		this.rarity = rarity;
		this.spottedVariant = spottedVariant;
		this.biomeCategory = biomeCategory;
	}

	public static PikeType getTypeById(int id) {
		for (PikeType type : values()) {
			if (type.id == id) return type;
		}
		return PikeType.AMUR;
	}

	public static PikeType getRandom(RandomSource rand, Holder<Biome> category, boolean fromBucket) {
		List<PikeType> possibleTypes = getPossibleTypes(category, WeightedRandomList.create(PikeRarity.values()).getRandom(rand).orElseThrow(), fromBucket);
		PikeType type = possibleTypes.get(rand.nextInt(possibleTypes.size()));
		Integer spottedVariant = type.spottedVariant;
		return spottedVariant != null && rand.nextFloat() < 0.2F ? PikeType.getTypeById(spottedVariant) : type;
	}

	private static List<PikeType> getPossibleTypes(Holder<Biome> category, PikeRarity rarity, boolean fromBucket) {
		List<PikeType> pikeTypes = Lists.newArrayList();
		HashSet<PikeType> spotted = new HashSet<>();
		for (PikeType type : PikeType.values()) {
			if ((fromBucket || type.biomeCategory == null || category.is(type.biomeCategory)) && type.rarity == rarity && !spotted.contains(type)) {
				Integer spottedVariant = type.spottedVariant;
				if (spottedVariant != null) {
					spotted.add(PikeType.getTypeById(spottedVariant));
				}
				pikeTypes.add(type);
			}
		}
		return pikeTypes;
	}

	public enum PikeSize {
		SMALL(1.2F, 1.4F),
		MEDIUM(1.5F, 1.7F),
		LARGE(1.7F, 1.9F),
		HUGE(2.3F, 2.5F);

		public final float renderSize;
		public final float boxSize;

		PikeSize(float renderSize, float boxSize) {
			this.renderSize = renderSize;
			this.boxSize = boxSize;
		}
	}

	public enum PikeRarity implements WeightedEntry {
		COMMON(ChatFormatting.GRAY, 55),
		UNCOMMON(ChatFormatting.GREEN, 25),
		RARE(ChatFormatting.BLUE, 15),
		EPIC(ChatFormatting.LIGHT_PURPLE, 5),
		LEGENDARY(ChatFormatting.GOLD, 1);

		public final ChatFormatting formatting;
		private final Weight weight;

		PikeRarity(ChatFormatting formatting, int weight) {
			this.formatting = formatting;
			this.weight = Weight.of(weight);
		}

		@Override
		public Weight getWeight() {
			return this.weight;
		}
	}
}