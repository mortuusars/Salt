package io.github.mortuusars.salt.advancement;

import com.google.gson.JsonObject;
import io.github.mortuusars.salt.Salt;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class SaltedFoodConsumedTrigger extends SimpleCriterionTrigger<SaltedFoodConsumedTrigger.TriggerInstance> {
    private static final ResourceLocation ID = Salt.resource("salted_food_consumed");

    @Override
    public @NotNull ResourceLocation getId() {
        return ID;
    }

    @Override
    protected SaltedFoodConsumedTrigger.@NotNull TriggerInstance createInstance(@NotNull JsonObject json, ContextAwarePredicate predicate, @NotNull DeserializationContext conditionsParser) {
        return new SaltedFoodConsumedTrigger.TriggerInstance(predicate);
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, triggerInstance -> true);
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        public TriggerInstance(ContextAwarePredicate predicate) {
            super(SaltedFoodConsumedTrigger.ID, predicate);
        }
    }
}
