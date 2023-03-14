package io.github.mortuusars.salt.data.provider;

import com.google.common.collect.Sets;
import io.github.mortuusars.salt.Salt;
import io.github.mortuusars.salt.advancement.HarvestSaltCrystalTrigger;
import io.github.mortuusars.salt.advancement.SaltEvaporationTrigger;
import io.github.mortuusars.salt.advancement.SaltedFoodConsumedTrigger;
import io.github.mortuusars.salt.client.LangKeys;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.function.Consumer;

public class Advancements extends AdvancementProvider {
    private final Path PATH;
    private ExistingFileHelper existingFileHelper;
    public static final Logger LOGGER = LogManager.getLogger();

    public Advancements(DataGenerator dataGenerator, ExistingFileHelper existingFileHelper) {
        super(dataGenerator);
        PATH = dataGenerator.getOutputFolder();
        this.existingFileHelper = existingFileHelper;
    }

    @Override
    public void run(CachedOutput cache) {
        Set<ResourceLocation> set = Sets.newHashSet();
        Consumer<Advancement> consumer = (advancement) -> {
            if (!set.add(advancement.getId())) {
                throw new IllegalStateException("Duplicate advancement " + advancement.getId());
            } else {
                Path path = getPath(PATH, advancement);

                try {
                    DataProvider.saveStable(cache, advancement.deconstruct().serializeToJson(), path);
                }
                catch (IOException ioexception) {
                    LOGGER.error("Couldn't save advancement {}", path, ioexception);
                }
            }
        };

        new SaltAdvancements(existingFileHelper).accept(consumer);
    }

    private static Path getPath(Path pathIn, Advancement advancementIn) {
        return pathIn.resolve("data/" + advancementIn.getId().getNamespace() + "/advancements/" + advancementIn.getId().getPath() + ".json");
    }

    public static class SaltAdvancements implements Consumer<Consumer<Advancement>> {
        private ExistingFileHelper existingFileHelper;

        public SaltAdvancements(ExistingFileHelper existingFileHelper) {
            this.existingFileHelper = existingFileHelper;
        }

        @Override
        public void accept(Consumer<Advancement> advancementConsumer) {
            Advancement taste_explosion = Advancement.Builder.advancement()
                    .parent(new ResourceLocation("minecraft:husbandry/root"))
                    .display(new ItemStack(Salt.Items.SALT.get()),
                            Salt.translate(LangKeys.ADVANCEMENT_TASTE_EXPLOSION_TITLE),
                            Salt.translate(LangKeys.ADVANCEMENT_TASTE_EXPLOSION_DESCRIPTION),
                            null, FrameType.TASK, true, false, false)
                    .addCriterion("eat_salted_food", new SaltedFoodConsumedTrigger.TriggerInstance(EntityPredicate.Composite.ANY))
                    .save(advancementConsumer, Salt.resource("adventure/taste_explosion"), existingFileHelper);

            Advancement boil_off = Advancement.Builder.advancement()
                    .parent(taste_explosion)
                    .display(new ItemStack(Items.CAULDRON),
                            Salt.translate(LangKeys.ADVANCEMENT_BOIL_OFF_TITLE),
                            Salt.translate(LangKeys.ADVANCEMENT_BOIL_OFF_DESCRIPTION),
                            null,  FrameType.TASK, true, false, false)
                    .addCriterion("evaporate_water_to_form_salt", new SaltEvaporationTrigger.TriggerInstance(EntityPredicate.Composite.ANY))
                    .save(advancementConsumer, Salt.resource("adventure/boil_off"), existingFileHelper);

            Advancement crystal_garden = Advancement.Builder.advancement()
                    .parent(taste_explosion)
                    .display(new ItemStack(Salt.Items.SALT_CLUSTER.get()),
                            Salt.translate(LangKeys.ADVANCEMENT_CRYSTAL_GARDEN_TITLE),
                            Salt.translate(LangKeys.ADVANCEMENT_CRYSTAL_GARDEN_DESCRIPTION),
                            null,  FrameType.TASK, true, false, false)
                    .addCriterion("harvest_salt_crystal", new HarvestSaltCrystalTrigger.TriggerInstance(EntityPredicate.Composite.ANY))
                    .save(advancementConsumer, Salt.resource("adventure/crystal_garden"), existingFileHelper);
        }
    }
}
