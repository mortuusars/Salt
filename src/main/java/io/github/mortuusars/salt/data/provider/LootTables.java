package io.github.mortuusars.salt.data.provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.mortuusars.salt.Registry;
import io.github.mortuusars.salt.Salt;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ConfiguredStructureTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;

public class LootTables extends LootTableProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private final DataGenerator generator;

    public LootTables(DataGenerator generator) {
        super(generator);
        this.generator = generator;
    }

    @Override
    public void run(HashCache cache) {

        writeTable(cache, Salt.resource("cauldron/salt_level_1"),
                LootTable.lootTable().withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(Registry.Items.SALT.get())
                                        .when(LootItemRandomChanceCondition.randomChance(0.75f))))
                        .build());

        writeTable(cache, Salt.resource("cauldron/salt_level_2"),
                LootTable.lootTable().withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(Registry.Items.SALT.get())
                                        .when(LootItemRandomChanceCondition.randomChance(0.9f))))
                        .build());

        writeTable(cache, Salt.resource("cauldron/salt_full"),
                LootTable.lootTable().withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(Registry.Items.SALT.get())))
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1))
                                        .add(LootItem.lootTableItem(Registry.Items.SALT.get())
                                                .when(LootItemRandomChanceCondition.randomChance(0.25f))))
                        .build());


    }

    private void writeTable(HashCache cache, ResourceLocation location, LootTable lootTable) {
        Path outputFolder = this.generator.getOutputFolder();
        Path path = outputFolder.resolve("data/" + location.getNamespace() + "/loot_tables/" + location.getPath() + ".json");
        try {
            DataProvider.save(GSON, cache, net.minecraft.world.level.storage.loot.LootTables.serialize(lootTable), path);
        } catch (IOException e) {
            LOGGER.error("Couldn't write loot lootTable {}", path, e);
        }
    }
}
