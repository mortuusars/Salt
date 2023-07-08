package io.github.mortuusars.salt.data.provider;

import io.github.mortuusars.salt.Salt;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings({"unused", "SameParameterValue"})
public class LootTables extends LootTableProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    private final DataGenerator generator;

    public LootTables(DataGenerator generator) {
        super(generator.getPackOutput(), BuiltInLootTables.all(), Collections.emptyList());
        this.generator = generator;
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cache) {
        // Not using CompletableFuture because some loot tables not generating. ¯\_(ツ)_/¯
//        return CompletableFuture.runAsync(() -> {

            // Blocks:

            writeTable(cache, Salt.Blocks.SALT_CAULDRON.getId(),
                    LootTable.lootTable()
                            .setParamSet(LootContextParamSets.BLOCK)
                            .withPool(
                                    LootPool.lootPool()
                                            .setRolls(ConstantValue.exactly(1))
                                            .add(LootItem.lootTableItem(Items.CAULDRON))
                                            .when(ExplosionCondition.survivesExplosion()))
                            .build());

            dropsSelf(cache, Salt.Items.SALT_BLOCK.get());
            dropsSelf(cache, Salt.Items.RAW_ROCK_SALT_BLOCK.get());

            writeTable(cache, new ResourceLocation("salt:blocks/" + Salt.Blocks.ROCK_SALT_ORE.getId().getPath()),
                    silkTouchOrDefaultTable(Salt.Blocks.ROCK_SALT_ORE.get(), Salt.Items.RAW_ROCK_SALT.get(), 1, 3)
                            .build());

            writeTable(cache, new ResourceLocation("salt:blocks/" + Salt.Blocks.DEEPSLATE_ROCK_SALT_ORE.getId()
                            .getPath()),
                    silkTouchOrDefaultTable(Salt.Blocks.DEEPSLATE_ROCK_SALT_ORE.get(), Salt.Items.RAW_ROCK_SALT.get(), 1, 3)
                            .build());

            writeTable(cache, new ResourceLocation("salt:blocks/" + Salt.Blocks.SALT_CLUSTER.getId().getPath()),
                    silkTouchOrDefaultTable(Salt.Blocks.SALT_CLUSTER.get(), Salt.Items.RAW_ROCK_SALT.get(), 1, 3)
                            .build());

            writeTable(cache, new ResourceLocation("salt:blocks/" + Salt.Blocks.LARGE_SALT_BUD.getId()
                    .getPath()), silkTouchTable(Salt.Items.LARGE_SALT_BUD.get()).build());
            writeTable(cache, new ResourceLocation("salt:blocks/" + Salt.Blocks.MEDIUM_SALT_BUD.getId()
                    .getPath()), silkTouchTable(Salt.Items.MEDIUM_SALT_BUD.get()).build());
            writeTable(cache, new ResourceLocation("salt:blocks/" + Salt.Blocks.SMALL_SALT_BUD.getId()
                    .getPath()), silkTouchTable(Salt.Items.SMALL_SALT_BUD.get()).build());

            dropsSelf(cache, Salt.Items.SALT_LAMP.get());

            // Evaporated salt:

            writeTable(cache, Salt.resource("cauldron_evaporation/salt_level_1"),
                    LootTable.lootTable().withPool(
                                    LootPool.lootPool()
                                            .setRolls(ConstantValue.exactly(1))
                                            .add(LootItem.lootTableItem(Salt.Items.SALT.get())
                                                    .when(LootItemRandomChanceCondition.randomChance(0.75f))))
                            .build());

            writeTable(cache, Salt.resource("cauldron_evaporation/salt_level_2"),
                    LootTable.lootTable().withPool(
                                    LootPool.lootPool()
                                            .setRolls(ConstantValue.exactly(1))
                                            .add(LootItem.lootTableItem(Salt.Items.SALT.get())
                                                    .when(LootItemRandomChanceCondition.randomChance(0.9f))))
                            .build());

            writeTable(cache, Salt.resource("cauldron_evaporation/salt_full"),
                    LootTable.lootTable().withPool(
                                    LootPool.lootPool()
                                            .setRolls(ConstantValue.exactly(1))
                                            .add(LootItem.lootTableItem(Salt.Items.SALT.get())))
                            .withPool(
                                    LootPool.lootPool()
                                            .setRolls(ConstantValue.exactly(1))
                                            .add(LootItem.lootTableItem(Salt.Items.SALT.get())
                                                    .when(LootItemRandomChanceCondition.randomChance(0.25f))))
                            .build());
//        });

        return CompletableFuture.runAsync(() -> {});
    }

    private void dropsSelf(CachedOutput cache, BlockItem blockItem) {
        writeTable(cache, new ResourceLocation("salt:blocks/" + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(blockItem.getBlock()))
                        .getPath()),
                LootTable.lootTable()
                        .setParamSet(LootContextParamSets.BLOCK)
                        .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(blockItem)))
                        .build());
    }

    protected LootTable.Builder silkTouchTable(Item lootItem) {
        return LootTable.lootTable()
                .setParamSet(LootContextParamSets.BLOCK)
                .withPool(
                LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(lootItem)
                                .when(MatchTool.toolMatches(ItemPredicate.Builder.item()
                                        .hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1)))))));
    }

    protected LootTable.Builder silkTouchOrDefaultTable(Block block, Item lootItem, float min, float max) {
        LootPool.Builder builder = LootPool.lootPool()
                .name(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath())
                .setRolls(ConstantValue.exactly(1))
                .add(AlternativesEntry.alternatives(
                                LootItem.lootTableItem(block)
                                        .when(MatchTool.toolMatches(ItemPredicate.Builder.item()
                                                .hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))))),
                                LootItem.lootTableItem(lootItem)
                                        .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                                        .apply(ApplyExplosionDecay.explosionDecay())));
        return LootTable.lootTable().setParamSet(LootContextParamSets.BLOCK).withPool(builder);
    }

    private void writeTable(CachedOutput cache, ResourceLocation location, LootTable lootTable) {
        Path outputFolder = this.generator.getPackOutput().getOutputFolder();
        Path path = outputFolder.resolve("data/" + location.getNamespace() + "/loot_tables/" + location.getPath() + ".json");
        DataProvider.saveStable(cache, LootDataType.TABLE.parser().toJsonTree(lootTable), path);
    }
}
