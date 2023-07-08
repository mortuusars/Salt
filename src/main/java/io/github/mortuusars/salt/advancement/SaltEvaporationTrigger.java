package io.github.mortuusars.salt.advancement;

import com.google.gson.JsonObject;
import io.github.mortuusars.salt.Salt;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class SaltEvaporationTrigger extends SimpleCriterionTrigger<SaltEvaporationTrigger.TriggerInstance> {
    private static final ResourceLocation ID = Salt.resource("salt_evaporated");

    @Override
    public @NotNull ResourceLocation getId() {
        return ID;
    }

    @Override
    protected SaltEvaporationTrigger.@NotNull TriggerInstance createInstance(@NotNull JsonObject json, ContextAwarePredicate predicate, @NotNull DeserializationContext conditionsParser) {
        return new SaltEvaporationTrigger.TriggerInstance(predicate);
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, triggerInstance -> true);
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        public TriggerInstance(ContextAwarePredicate predicate) {
            super(SaltEvaporationTrigger.ID, predicate);
        }
    }
}
