package io.github.mortuusars.salt.data;

import io.github.mortuusars.salt.Salt;
import io.github.mortuusars.salt.data.provider.*;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Salt.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGeneration
{
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        if (event.includeServer()) {
//            generator.addProvider(new Advancements(generator, helper));
            generator.addProvider(new LootTables(generator));
            BlockTags blockTags = new BlockTags(generator, helper);
            generator.addProvider(blockTags);
            generator.addProvider(new ItemTags(generator, blockTags, helper));
        }
        if (event.includeClient()) {
            BlockStatesAndModels blockStates = new BlockStatesAndModels(generator, helper);
            generator.addProvider(blockStates);
            generator.addProvider(new ItemModels(generator, blockStates.models().existingFileHelper));
        }
    }
}