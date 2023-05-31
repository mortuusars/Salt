package io.github.mortuusars.salt.integration;

import io.github.mortuusars.salt.Salting;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import squeek.appleskin.api.event.FoodValuesEvent;
import squeek.appleskin.api.food.FoodValues;

public class AppleSkinHandler {
    @SubscribeEvent
    public void onFoodValuesGetEvent(FoodValuesEvent event) {
        ItemStack foodStack = event.itemStack;
        if (!Salting.isSalted(foodStack))
            return;

        Salting.FoodValue additionalFoodValue = Salting.getAdditionalFoodValue(foodStack);

        event.modifiedFoodValues = new FoodValues(event.modifiedFoodValues.hunger + additionalFoodValue.nutrition(),
                event.modifiedFoodValues.saturationModifier + additionalFoodValue.saturationModifier());
    }
}
