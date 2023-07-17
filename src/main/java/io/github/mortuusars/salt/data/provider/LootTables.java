package io.github.mortuusars.salt.data.provider;

import io.github.mortuusars.salt.Salt;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class LootTables extends VanillaBlockLoot {
    public static class BlockLoot extends VanillaBlockLoot {
        @Override
        protected void generate() {
            dropSelf(Salt.Blocks.SALT_BLOCK.get());
            dropSelf(Salt.Blocks.RAW_ROCK_SALT_BLOCK.get());
            dropSelf(Salt.Blocks.SALT_LAMP.get());
            dropOther(Salt.Blocks.SALT_CAULDRON.get(), Blocks.CAULDRON);

            add(Salt.Blocks.ROCK_SALT_ORE.get(), block -> createOreDrop(block, Salt.Items.RAW_ROCK_SALT.get()));
            add(Salt.Blocks.DEEPSLATE_ROCK_SALT_ORE.get(), block -> createOreDrop(block, Salt.Items.RAW_ROCK_SALT.get()));

            dropWhenSilkTouch(Salt.Blocks.SMALL_SALT_BUD.get());
            dropWhenSilkTouch(Salt.Blocks.MEDIUM_SALT_BUD.get());
            dropWhenSilkTouch(Salt.Blocks.LARGE_SALT_BUD.get());
            add(Salt.Blocks.SALT_CLUSTER.get(), block -> createSilkTouchDispatchTable(block, LootItem.lootTableItem(Salt.Items.RAW_ROCK_SALT.get())
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                    .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                    .when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.CLUSTER_MAX_HARVESTABLES)))
                    .otherwise(this.applyExplosionDecay(block, LootItem.lootTableItem(Salt.Items.SALT_CLUSTER.get())
                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))))));
        }

        @Override
        protected @NotNull Iterable<Block> getKnownBlocks() {
            return ForgeRegistries.BLOCKS.getEntries().stream()
                    .filter(e -> e.getKey().location().getNamespace().equals(Salt.ID))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());
        }
    }

    public static class GameplayLoot implements LootTableSubProvider {
        @Override
        public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
            consumer.accept(Salt.resource("cauldron_evaporation/salt_level_1"),
                    LootTable.lootTable().withPool(
                                    LootPool.lootPool()
                                            .setRolls(ConstantValue.exactly(1))
                                            .add(LootItem.lootTableItem(Salt.Items.SALT.get())
                                                    .when(LootItemRandomChanceCondition.randomChance(0.75f)))));

            consumer.accept(Salt.resource("cauldron_evaporation/salt_level_2"),
                    LootTable.lootTable().withPool(
                                    LootPool.lootPool()
                                            .setRolls(ConstantValue.exactly(1))
                                            .add(LootItem.lootTableItem(Salt.Items.SALT.get())
                                                    .when(LootItemRandomChanceCondition.randomChance(0.9f)))));

            consumer.accept(Salt.resource("cauldron_evaporation/salt_full"),
                    LootTable.lootTable().withPool(
                                    LootPool.lootPool()
                                            .setRolls(ConstantValue.exactly(1))
                                            .add(LootItem.lootTableItem(Salt.Items.SALT.get())))
                            .withPool(
                                    LootPool.lootPool()
                                            .setRolls(ConstantValue.exactly(1))
                                            .add(LootItem.lootTableItem(Salt.Items.SALT.get())
                                                    .when(LootItemRandomChanceCondition.randomChance(0.25f)))));
        }
    }
}
