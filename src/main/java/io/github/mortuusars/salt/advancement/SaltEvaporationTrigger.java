package io.github.mortuusars.salt.advancement;

import com.google.gson.JsonObject;
import io.github.mortuusars.salt.Salt;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class SaltEvaporationTrigger extends SimpleCriterionTrigger<SaltEvaporationTrigger.TriggerInstance> {
    private static final ResourceLocation ID = Salt.resource("salt_evaporated");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    protected SaltEvaporationTrigger.TriggerInstance createInstance(JsonObject json, EntityPredicate.Composite player, DeserializationContext conditionsParser) {
        return new SaltEvaporationTrigger.TriggerInstance(player);
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, triggerInstance -> true);
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        public TriggerInstance(EntityPredicate.Composite player) {
            super(SaltEvaporationTrigger.ID, player);
        }
    }
}
