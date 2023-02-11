package io.github.mortuusars.salt;

import io.github.mortuusars.salt.configuration.Configuration;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;

import javax.annotation.Nullable;

public class Salting {

    public record FoodValue(int nutrition, float saturationModifier) {
        @Override
        public String toString() {
            return "{Nutrition:" + nutrition + ",Saturation:" + saturationModifier + "}";
        }
    }

    private static final String SALTED_KEY = "Salted";

    public static boolean isSalted(ItemStack itemStack) {
        return itemStack.hasTag() && itemStack.getTag().contains(SALTED_KEY);
    }

    /**
     * Same ItemStack is returned.
     */
    public static ItemStack setSalted(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();
        tag.putBoolean(SALTED_KEY, true);
        return itemStack;
    }

    public static FoodValue getAdditionalFoodValue(ItemStack stack) {
        @Nullable FoodValue foodValue = Configuration.FOOD_VALUES.get(stack.getItem().getRegistryName().toString());
        return foodValue != null ? foodValue : new FoodValue(Configuration.SALTING_ADDITIONAL_NUTRITION.get(),
                Configuration.SALTING_ADDITIONAL_SATURATION_MODIFIER.get().floatValue());
    }

//    public static int getAdditionalNutrition(ItemStack stack) {
//        return Configuration.SALTING_ADDITIONAL_NUTRITION.get();
//    }
//
//    public static float getAdditionalSaturationModifier(ItemStack stack) {
//        return (float)Configuration.SALTING_ADDITIONAL_SATURATION_MODIFIER.get().doubleValue();
//    }

    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        ItemStack itemStack = event.getItem();
        if (itemStack.getItem().isEdible() && Salting.isSalted(itemStack) && event.getEntityLiving() instanceof Player player) {
            FoodValue additionalFoodValue = getAdditionalFoodValue(itemStack);
            player.getFoodData().eat(additionalFoodValue.nutrition, additionalFoodValue.saturationModifier);
        }
    }
}
