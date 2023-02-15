package io.github.mortuusars.salt.event;

import io.github.mortuusars.salt.Salt;
import io.github.mortuusars.salt.Salting;
import io.github.mortuusars.salt.world.feature.SaltPlacedFeatures;
import io.github.mortuusars.salt.world.gen.RockSaltGeneration;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.List;

public class CommonEvents {
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // 'randomTick' is used in mixin to convert Water Cauldron to Salt Cauldron:
            Blocks.WATER_CAULDRON.isRandomlyTicking = true;

            Salt.registerDispenserBehaviors();
        });
    }

    public static void onItemUseFinish(LivingEntityUseItemEvent.Finish event) {
        Salting.onFoodEaten(event);
    }

    public static void onBiomeLoadingEvent(final BiomeLoadingEvent event) {
        RockSaltGeneration.addFeaturesToGeneration(event);
    }
}
