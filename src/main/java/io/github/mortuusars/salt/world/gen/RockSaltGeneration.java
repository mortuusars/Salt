package io.github.mortuusars.salt.world.gen;

import io.github.mortuusars.salt.configuration.Configuration;
import io.github.mortuusars.salt.world.feature.SaltPlacedFeatures;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.event.world.BiomeLoadingEvent;

import java.util.List;

public class RockSaltGeneration {
    public static void addFeaturesToGeneration(final BiomeLoadingEvent event) {
        if (Configuration.GENERATE_ROCK_SALT.get()) {
            // Adding to every biome. Feature will check for biome tag when generating.
            event.getGeneration()
                    .getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES)
                    .add(SaltPlacedFeatures.ROCK_SALT);
        }
    }
}
