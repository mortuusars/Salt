package io.github.mortuusars.salt.event;

import io.github.mortuusars.salt.Salting;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class CommonEvents {

    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Blocks.WATER_CAULDRON.isRandomlyTicking = true;
        });
    }

    public static void onItemUseFinish(LivingEntityUseItemEvent.Finish event) {
        Salting.onFoodEaten(event);
    }

//    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
//        if (event.getEntity() instanceof Animal animal) {
//
//        }
//    }
}
