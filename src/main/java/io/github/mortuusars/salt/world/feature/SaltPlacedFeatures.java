package io.github.mortuusars.salt.world.feature;

import io.github.mortuusars.salt.Salt;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.*;

public class SaltPlacedFeatures {
    public static final Holder<PlacedFeature> ROCK_SALT =
            PlacementUtils.register("mineral_rock_salt", Salt.ConfiguredFeatures.ROCK_SALT,
                CountPlacement.of(2),
                /*RarityFilter.onAverageOnceEvery(2),*/
                InSquarePlacement.spread(),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(5), VerticalAnchor.absolute(110)),
                BiomeFilter.biome());
}
