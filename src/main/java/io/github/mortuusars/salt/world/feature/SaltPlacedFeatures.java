package io.github.mortuusars.salt.world.feature;

import io.github.mortuusars.salt.Salt;
import io.github.mortuusars.salt.configuration.Configuration;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.*;

public class SaltPlacedFeatures {
    public static final Holder<PlacedFeature> ROCK_SALT =
            PlacementUtils.register("mineral_rock_salt", Salt.ConfiguredFeatures.ROCK_SALT,
                CountPlacement.of(Configuration.ROCK_SALT_DEPOSIT_QUANTITY.get()),
                RarityFilter.onAverageOnceEvery(16),
                InSquarePlacement.spread(),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(Configuration.ROCK_SALT_MIN_HEIGHT.get()),
                        VerticalAnchor.absolute(Configuration.ROCK_SALT_MAX_HEIGHT.get())),
                BiomeFilter.biome());
}
