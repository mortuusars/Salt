package io.github.mortuusars.salt.data;

import io.github.mortuusars.salt.Salt;
import io.github.mortuusars.salt.data.provider.*;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Salt.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGeneration
{
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        if (event.includeServer())
            generator.addProvider(event.includeServer(), new Advancements(generator, helper));
            generator.addProvider(event.includeServer(), new LootTables(generator));
            generator.addProvider(event.includeServer(), new Recipes(generator));
            BlockTags blockTags = new BlockTags(generator, helper);
            generator.addProvider(event.includeServer(), blockTags);
            generator.addProvider(event.includeServer(), new ItemTags(generator, blockTags, helper));
            generator.addProvider(event.includeServer(), new BiomeTags(generator, helper));

        BlockStatesAndModels blockStates = new BlockStatesAndModels(generator, helper);
        generator.addProvider(event.includeClient(), blockStates);
        generator.addProvider(event.includeClient(), new ItemModels(generator, blockStates.models().existingFileHelper));
        generator.addProvider(event.includeClient(), new Sounds(generator, helper));
        generator.addProvider(event.includeClient(), new Languages(generator, "en_us"));
        generator.addProvider(event.includeClient(), new Languages(generator, "uk_ua"));
    }
}