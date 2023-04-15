package io.github.mortuusars.salt.event;

import io.github.mortuusars.salt.Salt;
import io.github.mortuusars.salt.Salting;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class CommonEvents {
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // 'randomTick' is used in mixin to convert Water Cauldron to Salt Cauldron:
            Blocks.WATER_CAULDRON.isRandomlyTicking = true;

            Salt.Advancements.register();
            Salt.registerDispenserBehaviors();
        });
    }

    public static void onItemUseFinish(LivingEntityUseItemEvent.Finish event) {
        Salting.onFoodEaten(event);
    }
}
