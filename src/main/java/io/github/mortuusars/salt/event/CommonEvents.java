package io.github.mortuusars.salt.event;

import io.github.mortuusars.salt.Salting;
import net.minecraft.world.entity.animal.Animal;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;

public class CommonEvents {
    public static void onItemUseFinish(LivingEntityUseItemEvent.Finish event) {
        Salting.onFoodEaten(event);
    }

    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof Animal animal) {

        }
    }
}
