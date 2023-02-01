package io.github.mortuusars.salt;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;

public class Salting {
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

    public static int getAdditionalNutrition(ItemStack stack) {
        //TODO: Config nutrition value
        return 2;
    }

    public static float getAdditionalSaturationModifier(ItemStack stack) {
        //TODO: Config saturation value
        return 0.3f;
    }

    public static void onFoodEaten(LivingEntityUseItemEvent.Finish event) {
        ItemStack itemStack = event.getItem();
        if (itemStack.getItem().isEdible() && Salting.isSalted(itemStack) && event.getEntityLiving() instanceof Player player) {
            player.getFoodData().eat(getAdditionalNutrition(itemStack), getAdditionalSaturationModifier(itemStack));
        }
    }
}
