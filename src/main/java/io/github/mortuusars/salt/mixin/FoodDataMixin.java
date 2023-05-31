package io.github.mortuusars.salt.mixin;

import io.github.mortuusars.salt.Salt;
import io.github.mortuusars.salt.Salting;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public class FoodDataMixin {
    @Shadow
    public void eat(int foodLevel, float saturationModifier) { }

    @Inject(method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/food/FoodData;eat(IF)V"), cancellable = true)
    private void onEat(Item pItem, ItemStack pStack, LivingEntity entity, CallbackInfo ci) {
        if (!Salting.isSalted(pStack))
            return;

        FoodProperties foodProperties = pStack.getFoodProperties(entity);
        Salting.FoodValue additionalFoodValues = Salting.getAdditionalFoodValue(pStack);

        eat(foodProperties.getNutrition() + additionalFoodValues.nutrition(),
                foodProperties.getSaturationModifier() + additionalFoodValues.saturationModifier());

        if (entity instanceof ServerPlayer serverPlayer && !serverPlayer.isCreative())
                Salt.Advancements.SALTED_FOOD_CONSUMED.trigger(serverPlayer);

        ci.cancel();
    }
}
