package io.github.mortuusars.salt.world.feature;

import io.github.mortuusars.salt.Registry;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.valueproviders.BiasedToBottomInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.WeightedListInt;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class SaltPlacedFeatures {
//    public static final Holder<PlacedFeature> ROCK_SALT = PlacementUtils.register("mineral_rock_salt",
//            Registry.ConfiguredFeatures.ROCK_SALT, commonOrePlacement(4,
//                    HeightRangePlacement.uniform(VerticalAnchor.absolute(-60), VerticalAnchor.absolute(128))));

    public static final Holder<PlacedFeature> ROCK_SALT =
            PlacementUtils.register("mineral_rock_salt", Registry.ConfiguredFeatures.ROCK_SALT,
                CountPlacement.of(2),
                /*RarityFilter.onAverageOnceEvery(2),*/
                InSquarePlacement.spread(),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(5), VerticalAnchor.absolute(110)),
                BiomeFilter.biome());

//    public static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_) {
//        return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
//    }
//
//    public static List<PlacementModifier> commonOrePlacement(int pCount, PlacementModifier pHeightRange) {
//        return orePlacement(CountPlacement.of(pCount), pHeightRange);
//    }
//
//    public static List<PlacementModifier> rareOrePlacement(int pChance, PlacementModifier pHeightRange) {
//        return orePlacement(RarityFilter.onAverageOnceEvery(pChance), pHeightRange);
//    }
}
