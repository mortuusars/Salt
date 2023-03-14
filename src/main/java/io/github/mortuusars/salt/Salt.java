package io.github.mortuusars.salt;

import io.github.mortuusars.salt.advancement.HarvestSaltCrystalTrigger;
import io.github.mortuusars.salt.advancement.SaltEvaporationTrigger;
import io.github.mortuusars.salt.advancement.SaltedFoodConsumedTrigger;
import io.github.mortuusars.salt.block.SaltBlock;
import io.github.mortuusars.salt.block.SaltCauldronBlock;
import io.github.mortuusars.salt.block.SaltClusterBlock;
import io.github.mortuusars.salt.block.SaltSandBlock;
import io.github.mortuusars.salt.configuration.Configuration;
import io.github.mortuusars.salt.crafting.recipe.SaltingRecipe;
import io.github.mortuusars.salt.event.ClientEvents;
import io.github.mortuusars.salt.event.CommonEvents;
import io.github.mortuusars.salt.item.SaltItem;
import io.github.mortuusars.salt.world.feature.MineralDepositFeature;
import io.github.mortuusars.salt.world.feature.configurations.MineralDepositConfiguration;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.Registry;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

@Mod(Salt.ID)
public class Salt
{
    public static final String ID = "salt";

    public Salt() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        Configuration.init();
        modEventBus.addListener(Configuration::onConfigLoad);
        modEventBus.addListener(Configuration::onConfigReload);

        Blocks.BLOCKS.register(modEventBus);
        Items.ITEMS.register(modEventBus);
        Sounds.SOUNDS.register(modEventBus);
        EntityTypes.ENTITY_TYPES.register(modEventBus);
        RecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);

//        Configuration.COMMON.lo

        WorldGenFeatures.FEATURES.register(modEventBus);
        ConfiguredFeatures.CONFIGURED_FEATURES.register(modEventBus);
        PlacedFeatures.PLACED_FEATURES.register(modEventBus);

        modEventBus.addListener(CommonEvents::onCommonSetup);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
//            modEventBus.register(ClientEvents::onClientSetup);
//            modEventBus.addListener(ClientEvents::onRegisterModels);
            MinecraftForge.EVENT_BUS.addListener(ClientEvents::onItemTooltipEvent);
        });
        MinecraftForge.EVENT_BUS.addListener(CommonEvents::onItemUseFinish);
//        MinecraftForge.EVENT_BUS.addListener(CommonEvents::onBiomeLoadingEvent);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static MutableComponent translate(String key, String... args) {
        return Component.translatable(key, args);
    }

    public static ResourceLocation resource(String path) {
        return new ResourceLocation(ID, path);
    }

    public static void registerDispenserBehaviors() {
        DispenserBlock.registerBehavior(Items.SALT.get(), Melting.SALT_DISPENSER_BEHAVIOR);
    }

    public static class Blocks {
        private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Salt.ID);

        public static final RegistryObject<SandBlock> SALT_BLOCK = BLOCKS.register("salt_block",
                () -> new SaltSandBlock(0xe7d5cf, BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.SAND)));

        public static final RegistryObject<SaltBlock> ROCK_SALT_ORE = BLOCKS.register("rock_salt_ore",
                () -> new SaltBlock(net.minecraft.world.level.block.Blocks.STONE.defaultBlockState(), BlockBehaviour.Properties.of(Material.STONE)
                        .color(MaterialColor.COLOR_LIGHT_GRAY)
                        .randomTicks()
                        .strength(2.5F)
                        .requiresCorrectToolForDrops()
                        .sound(Salt.Sounds.Types.SALT)));
        public static final RegistryObject<SaltBlock> DEEPSLATE_ROCK_SALT_ORE = BLOCKS.register("deepslate_rock_salt_ore",
                () -> new SaltBlock(net.minecraft.world.level.block.Blocks.DEEPSLATE.defaultBlockState(), BlockBehaviour.Properties.copy(ROCK_SALT_ORE.get())
                        .color(MaterialColor.COLOR_GRAY)));

        public static final RegistryObject<SaltBlock> RAW_ROCK_SALT_BLOCK = BLOCKS.register("raw_rock_salt_block",
                () -> new SaltBlock(BlockBehaviour.Properties.copy(ROCK_SALT_ORE.get())
                        .sound(Sounds.Types.SALT_CLUSTER)));

        public static final RegistryObject<SaltClusterBlock> SALT_CLUSTER = BLOCKS.register("salt_cluster",
                () -> new SaltClusterBlock(7, 3, BlockBehaviour.Properties.of(Material.DECORATION)
                        .color(MaterialColor.COLOR_LIGHT_GRAY)
                        .randomTicks()
                        .strength(1.5F)
                        .sound(Salt.Sounds.Types.SALT_CLUSTER)
                        .lightLevel(state -> 3)
                        .dynamicShape()));

        public static final RegistryObject<SaltClusterBlock> LARGE_SALT_BUD = BLOCKS.register("large_salt_bud",
                () -> new SaltClusterBlock(5, 3, BlockBehaviour.Properties.copy(SALT_CLUSTER.get())
                        .lightLevel(state -> 2)
                        .sound(Sounds.Types.LARGE_SALT_BUD)));

        public static final RegistryObject<SaltClusterBlock> MEDIUM_SALT_BUD = BLOCKS.register("medium_salt_bud",
                () -> new SaltClusterBlock(4, 3, BlockBehaviour.Properties.copy(SALT_CLUSTER.get())
                        .lightLevel(state -> 2)
                        .sound(Sounds.Types.MEDIUM_SALT_BUD)));

        public static final RegistryObject<SaltClusterBlock> SMALL_SALT_BUD = BLOCKS.register("small_salt_bud",
                () -> new SaltClusterBlock(3, 4, BlockBehaviour.Properties.copy(SALT_CLUSTER.get())
                        .lightLevel(state -> 1)
                        .sound(Sounds.Types.SMALL_SALT_BUD)));

        public static final RegistryObject<SaltCauldronBlock> SALT_CAULDRON = BLOCKS.register("salt_cauldron",
                () -> new SaltCauldronBlock(LayeredCauldronBlock.RAIN, CauldronInteraction.EMPTY));

        public static final RegistryObject<SaltBlock> SALT_LAMP = BLOCKS.register("salt_lamp",
                () -> new SaltBlock(net.minecraft.world.level.block.Blocks.SPRUCE_SLAB.defaultBlockState(),
                        BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_ORANGE)
                                .sound(Salt.Sounds.Types.SALT)
                                .randomTicks()
                                .strength(2f)
                                .lightLevel(blockState -> 15)));
    }

    public static class Items {
        private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Salt.ID);
        public static final RegistryObject<Item> SALT = ITEMS.register("salt",
                () -> new SaltItem(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
        public static final RegistryObject<Item> RAW_ROCK_SALT = ITEMS.register("raw_rock_salt",
                () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));

        public static final RegistryObject<BlockItem> SALT_BLOCK = ITEMS.register("salt_block",
                () -> new BlockItem(Salt.Blocks.SALT_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
        public static final RegistryObject<BlockItem> ROCK_SALT_ORE = ITEMS.register("rock_salt_ore",
                () -> new BlockItem(Salt.Blocks.ROCK_SALT_ORE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
        public static final RegistryObject<BlockItem> DEEPSLATE_ROCK_SALT_ORE = ITEMS.register("deepslate_rock_salt_ore",
                () -> new BlockItem(Blocks.DEEPSLATE_ROCK_SALT_ORE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));
        public static final RegistryObject<BlockItem> RAW_ROCK_SALT_BLOCK = ITEMS.register("raw_rock_salt_block",
                () -> new BlockItem(Salt.Blocks.RAW_ROCK_SALT_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));

        public static final RegistryObject<BlockItem> SALT_CLUSTER = ITEMS.register("salt_cluster",
                () -> new BlockItem(Salt.Blocks.SALT_CLUSTER.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
        public static final RegistryObject<BlockItem> LARGE_SALT_BUD = ITEMS.register("large_salt_bud",
                () -> new BlockItem(Salt.Blocks.LARGE_SALT_BUD.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
        public static final RegistryObject<BlockItem> MEDIUM_SALT_BUD = ITEMS.register("medium_salt_bud",
                () -> new BlockItem(Salt.Blocks.MEDIUM_SALT_BUD.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
        public static final RegistryObject<BlockItem> SMALL_SALT_BUD = ITEMS.register("small_salt_bud",
                () -> new BlockItem(Salt.Blocks.SMALL_SALT_BUD.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));

        public static final RegistryObject<BlockItem> SALT_LAMP = ITEMS.register("salt_lamp",
                () -> new BlockItem(Blocks.SALT_LAMP.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    }

    public static class Advancements {
        public static SaltEvaporationTrigger SALT_EVAPORATED = new SaltEvaporationTrigger();
        public static SaltedFoodConsumedTrigger SALTED_FOOD_CONSUMED = new SaltedFoodConsumedTrigger();
        public static HarvestSaltCrystalTrigger HARVEST_SALT_CRYSTAL = new HarvestSaltCrystalTrigger();

        public static void register() {
            CriteriaTriggers.register(SALT_EVAPORATED);
            CriteriaTriggers.register(SALTED_FOOD_CONSUMED);
            CriteriaTriggers.register(HARVEST_SALT_CRYSTAL);
        }
    }

    public static class Sounds {
        private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Salt.ID);

        public static final RegistryObject<SoundEvent> SALT_DISSOLVE = registerBlockSound("salt.dissolve");
        public static final RegistryObject<SoundEvent> MELT = registerBlockSound("melt");
        public static final RegistryObject<SoundEvent> CAULDRON_EVAPORATE = registerBlockSound("cauldron.evaporate");
        public static final RegistryObject<SoundEvent> BUBBLE_POP = registerBlockSound("bubble_pop");
        public static final RegistryObject<SoundEvent> SALT_CAULDRON_REMOVE_SALT = registerBlockSound("salt_cauldron.remove_salt");

        public static final RegistryObject<SoundEvent> SALT_BREAK = registerBlockSound("salt.break");
        public static final RegistryObject<SoundEvent> SALT_STEP = registerBlockSound("salt.step");
        public static final RegistryObject<SoundEvent> SALT_PLACE = registerBlockSound("salt.place");
        public static final RegistryObject<SoundEvent> SALT_HIT = registerBlockSound("salt.hit");
        public static final RegistryObject<SoundEvent> SALT_FALL = registerBlockSound("salt.fall");

        public static final RegistryObject<SoundEvent> SALT_CLUSTER_BREAK = registerBlockSound("salt_cluster.break");
        public static final RegistryObject<SoundEvent> SALT_CLUSTER_STEP = registerBlockSound("salt_cluster.step");
        public static final RegistryObject<SoundEvent> SALT_CLUSTER_PLACE = registerBlockSound("salt_cluster.place");
        public static final RegistryObject<SoundEvent> SALT_CLUSTER_HIT = registerBlockSound("salt_cluster.hit");
        public static final RegistryObject<SoundEvent> SALT_CLUSTER_FALL = registerBlockSound("salt_cluster.fall");

        public static final RegistryObject<SoundEvent> LARGE_SALT_BUD_BREAK = registerBlockSound("large_salt_bud.break");
        public static final RegistryObject<SoundEvent> LARGE_SALT_BUD_STEP = registerBlockSound("large_salt_bud.step");
        public static final RegistryObject<SoundEvent> LARGE_SALT_BUD_PLACE = registerBlockSound("large_salt_bud.place");
        public static final RegistryObject<SoundEvent> LARGE_SALT_BUD_HIT = registerBlockSound("large_salt_bud.hit");
        public static final RegistryObject<SoundEvent> LARGE_SALT_BUD_FALL = registerBlockSound("large_salt_bud.fall");

        public static final RegistryObject<SoundEvent> MEDIUM_SALT_BUD_BREAK = registerBlockSound("medium_salt_bud.break");
        public static final RegistryObject<SoundEvent> MEDIUM_SALT_BUD_STEP = registerBlockSound("medium_salt_bud.step");
        public static final RegistryObject<SoundEvent> MEDIUM_SALT_BUD_PLACE = registerBlockSound("medium_salt_bud.place");
        public static final RegistryObject<SoundEvent> MEDIUM_SALT_BUD_HIT = registerBlockSound("medium_salt_bud.hit");
        public static final RegistryObject<SoundEvent> MEDIUM_SALT_BUD_FALL = registerBlockSound("medium_salt_bud.fall");

        public static final RegistryObject<SoundEvent> SMALL_SALT_BUD_BREAK = registerBlockSound("small_salt_bud.break");
        public static final RegistryObject<SoundEvent> SMALL_SALT_BUD_STEP = registerBlockSound("small_salt_bud.step");
        public static final RegistryObject<SoundEvent> SMALL_SALT_BUD_PLACE = registerBlockSound("small_salt_bud.place");
        public static final RegistryObject<SoundEvent> SMALL_SALT_BUD_HIT = registerBlockSound("small_salt_bud.hit");
        public static final RegistryObject<SoundEvent> SMALL_SALT_BUD_FALL = registerBlockSound("small_salt_bud.fall");

        private static RegistryObject<SoundEvent> registerBlockSound(String name) {
            return SOUNDS.register("block.salt." + name, () -> new SoundEvent(Salt.resource("block.salt." + name)));
        }

        public static class Types {
            public static final SoundType SALT = new ForgeSoundType(1.0f, 1.0f,
                    SALT_BREAK::get,
                    SALT_STEP::get,
                    SALT_PLACE::get,
                    SALT_HIT::get,
                    SALT_FALL::get);
            public static final SoundType SALT_CLUSTER = new ForgeSoundType(1.0f, 1.0f,
                    SALT_CLUSTER_BREAK::get,
                    SALT_CLUSTER_STEP::get,
                    SALT_CLUSTER_PLACE::get,
                    SALT_CLUSTER_HIT::get,
                    SALT_CLUSTER_FALL::get);

            public static final SoundType LARGE_SALT_BUD = new ForgeSoundType(1.0f, 1.0f,
                    LARGE_SALT_BUD_BREAK::get,
                    LARGE_SALT_BUD_STEP::get,
                    LARGE_SALT_BUD_PLACE::get,
                    LARGE_SALT_BUD_HIT::get,
                    LARGE_SALT_BUD_FALL::get);

            public static final SoundType MEDIUM_SALT_BUD = new ForgeSoundType(1.0f, 1.0f,
                    MEDIUM_SALT_BUD_BREAK::get,
                    MEDIUM_SALT_BUD_STEP::get,
                    MEDIUM_SALT_BUD_PLACE::get,
                    MEDIUM_SALT_BUD_HIT::get,
                    MEDIUM_SALT_BUD_FALL::get);

            public static final SoundType SMALL_SALT_BUD = new ForgeSoundType(1.0f, 1.0f,
                    SMALL_SALT_BUD_BREAK::get,
                    SMALL_SALT_BUD_STEP::get,
                    SMALL_SALT_BUD_PLACE::get,
                    SMALL_SALT_BUD_HIT::get,
                    SMALL_SALT_BUD_FALL::get);
        }
    }

    public static class ItemTags {
        public static final TagKey<Item> CAN_BE_SALTED = net.minecraft.tags.ItemTags.create(
                Salt.resource("can_be_salted"));

        public static final TagKey<Item> FORGE_SALTS = net.minecraft.tags.ItemTags.create(
                new ResourceLocation("forge", "salts"));
        public static final TagKey<Item> FORGE_TORCHES = net.minecraft.tags.ItemTags.create(
                new ResourceLocation("forge:torches"));
    }

    public static class BlockTags {
        public static final TagKey<Block> HEATERS = net.minecraft.tags.BlockTags.create(
                Salt.resource("heaters"));
        public static final TagKey<Block> SALT_CLUSTER_GROWABLES = net.minecraft.tags.BlockTags.create(
                Salt.resource("salt_cluster_growables"));
        public static final TagKey<Block> SALT_DISSOLVABLES = net.minecraft.tags.BlockTags.create(
                Salt.resource("salt_dissolvables"));
        public static final TagKey<Block> MELTABLES = net.minecraft.tags.BlockTags.create(
                Salt.resource("meltables"));
        public static final TagKey<Block> SALT_CLUSTER_REPLACEABLES = net.minecraft.tags.BlockTags.create(
                Salt.resource("salt_cluster_replaceables"));
    }

    public static class BiomeTags {
        public static final TagKey<Biome> HAS_ROCK_SALT_DEPOSITS = TagKey.create(net.minecraft.core.Registry.BIOME_REGISTRY,
                Salt.resource("has_rock_salt_deposits"));
    }

    public static class EntityTypes {
        private static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
                DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Salt.ID);

//        public static final RegistryObject<EntityType<MeltingEntity>> MELTING = ENTITY_TYPES.register("melting_entity",
//                () -> EntityType.Builder.<MeltingEntity>of(MeltingEntity::new, MobCategory.MISC).build(Salt.ID + "melting_entity"));
    }

    public static class RecipeSerializers {
        private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
                DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Salt.ID);

        public static final RegistryObject<RecipeSerializer<?>> SALTING = RECIPE_SERIALIZERS.register("salting",
                () -> new SaltingRecipe.Serializer());
    }

    public static class WorldGenFeatures {
        private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Salt.ID);
        public static final RegistryObject<Feature<MineralDepositConfiguration>> MINERAL_DEPOSIT = FEATURES.register("mineral_deposit",
                () -> new MineralDepositFeature(MineralDepositConfiguration.CODEC));
    }

    public static class PlacedFeatures {
        private static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, Salt.ID);

//        public static final RegistryObject<PlacedFeature> ROCK_SALT_DEPOSIT = PLACED_FEATURES.register("mineral_rock_salt",
//                SaltPlacedFeatures::newRockSaltFeature); /*new PlacedFeature(ConfiguredFeatures.ROCK_SALT.getHolder().get(), List.of(CountPlacement.of(10*//*Configuration.ROCK_SALT_DEPOSIT_QUANTITY.get()*//*),
//                        RarityFilter.onAverageOnceEvery(16),
//                        InSquarePlacement.spread(),
//                        HeightRangePlacement.uniform(VerticalAnchor.absolute(0*//*Configuration.ROCK_SALT_MIN_HEIGHT.get()*//*),
//                                VerticalAnchor.absolute(110*//*Configuration.ROCK_SALT_MAX_HEIGHT.get()*//*)),
//                        BiomeFilter.biome())));*/
    }

    public static class ConfiguredFeatures {
        private static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, Salt.ID);

        private static final Supplier<MineralDepositConfiguration.DepositBlockStateInfo> ROCK_SALT_STONE_BLOCKS = () -> MineralDepositConfiguration.blockStateInfo(
                new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                        .add(Blocks.ROCK_SALT_ORE.get().defaultBlockState(), 8)
                        .add(Blocks.RAW_ROCK_SALT_BLOCK.get().defaultBlockState(), 1)
                        .build()),
                OreFeatures.STONE_ORE_REPLACEABLES);

        private static final Supplier<MineralDepositConfiguration.DepositBlockStateInfo> ROCK_SALT_DEEPSLATE_BLOCKS = () -> MineralDepositConfiguration.blockStateInfo(
                new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                        .add(Blocks.DEEPSLATE_ROCK_SALT_ORE.get().defaultBlockState(), 8)
                        .add(Blocks.RAW_ROCK_SALT_BLOCK.get().defaultBlockState(), 1)
                        .build()),
                OreFeatures.DEEPSLATE_ORE_REPLACEABLES);

        private static final Supplier<MineralDepositConfiguration.DepositBlockStateInfo> ROCK_SALT_CLUSTERS = () -> MineralDepositConfiguration.blockStateInfo(
                new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                        .add(Blocks.SALT_CLUSTER.get().defaultBlockState(), 16)
                        .add(Blocks.LARGE_SALT_BUD.get().defaultBlockState(), 2)
                        .add(Blocks.MEDIUM_SALT_BUD.get().defaultBlockState(), 1)
                        .add(Blocks.SMALL_SALT_BUD.get().defaultBlockState(), 1)
                        .build()),
                new TagMatchTest(BlockTags.SALT_CLUSTER_REPLACEABLES));

        public static final RegistryObject<ConfiguredFeature<?, ?>> ROCK_SALT = CONFIGURED_FEATURES.register("mineral_rock_salt",
                () -> new ConfiguredFeature<>(WorldGenFeatures.MINERAL_DEPOSIT.get(), new MineralDepositConfiguration(
                        List.of(ROCK_SALT_STONE_BLOCKS.get(), ROCK_SALT_DEEPSLATE_BLOCKS.get()), ROCK_SALT_CLUSTERS.get())));
    }
}
