package io.github.mortuusars.salt;

import io.github.mortuusars.salt.block.SaltBlock;
import io.github.mortuusars.salt.block.SaltCauldronBlock;
import io.github.mortuusars.salt.block.SaltClusterBlock;
import io.github.mortuusars.salt.configuration.Configuration;
import io.github.mortuusars.salt.crafting.recipe.SaltingRecipe;
import io.github.mortuusars.salt.event.ClientEvents;
import io.github.mortuusars.salt.event.CommonEvents;
import io.github.mortuusars.salt.item.SaltItem;
import io.github.mortuusars.salt.world.feature.MineralDepositFeature;
import io.github.mortuusars.salt.world.feature.configurations.MineralDepositConfiguration;
import net.minecraft.core.Holder;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.SandBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

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
        RecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
        WorldGenFeatures.FEATURES.register(modEventBus);

        modEventBus.addListener(CommonEvents::onCommonSetup);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            modEventBus.addListener(ClientEvents::onClientSetup);
            modEventBus.addListener(ClientEvents::onRegisterModels);
            MinecraftForge.EVENT_BUS.addListener(ClientEvents::onItemTooltipEvent);
        });
        MinecraftForge.EVENT_BUS.addListener(CommonEvents::onItemUseFinish);
        MinecraftForge.EVENT_BUS.addListener(CommonEvents::onBiomeLoadingEvent);

        MinecraftForge.EVENT_BUS.register(this);

        MinecraftForge.EVENT_BUS.addListener(this::onRightClick);
    }

    public static ResourceLocation resource(String path) {
        return new ResourceLocation(ID, path);
    }

    // Testing
    private void onRightClick(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntity().getLevel().isClientSide || !(event.getEntity() instanceof Player player))
            return;
    }

    public static class Blocks {
        private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Salt.ID);

        public static final RegistryObject<SandBlock> SALT_BLOCK = BLOCKS.register("salt_block",
                () -> new SandBlock(0xe7d5cf, BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.SAND)));

        public static final RegistryObject<SaltBlock> ROCK_SALT_ORE = BLOCKS.register("rock_salt_ore",
                () -> new SaltBlock(BlockBehaviour.Properties.of(Material.STONE)
                        .color(MaterialColor.COLOR_LIGHT_GRAY)
                        .randomTicks()
                        .strength(2.5F)
                        .requiresCorrectToolForDrops()
                        .sound(Salt.Sounds.Types.SALT)));
        public static final RegistryObject<SaltBlock> DEEPSLATE_ROCK_SALT_ORE = BLOCKS.register("deepslate_rock_salt_ore",
                () -> new SaltBlock(BlockBehaviour.Properties.copy(ROCK_SALT_ORE.get())
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
    }

    public static class Sounds {
        private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Salt.ID);

        public static final RegistryObject<SoundEvent> SALT_DISSOLVE = registerBlockSound("salt.dissolve");
        public static final RegistryObject<SoundEvent> CAULDRON_EVAPORATE = registerBlockSound("cauldron.evaporate");
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
                    () -> SALT_BREAK.get(),
                    () -> SALT_STEP.get(),
                    () -> SALT_PLACE.get(),
                    () -> SALT_HIT.get(),
                    () -> SALT_FALL.get());
            public static final SoundType SALT_CLUSTER = new ForgeSoundType(1.0f, 1.0f,
                    () -> SALT_CLUSTER_BREAK.get(),
                    () -> SALT_CLUSTER_STEP.get(),
                    () -> SALT_CLUSTER_PLACE.get(),
                    () -> SALT_CLUSTER_HIT.get(),
                    () -> SALT_CLUSTER_FALL.get());

            public static final SoundType LARGE_SALT_BUD = new ForgeSoundType(1.0f, 1.0f,
                    () -> LARGE_SALT_BUD_BREAK.get(),
                    () -> LARGE_SALT_BUD_STEP.get(),
                    () -> LARGE_SALT_BUD_PLACE.get(),
                    () -> LARGE_SALT_BUD_HIT.get(),
                    () -> LARGE_SALT_BUD_FALL.get());

            public static final SoundType MEDIUM_SALT_BUD = new ForgeSoundType(1.0f, 1.0f,
                    () -> MEDIUM_SALT_BUD_BREAK.get(),
                    () -> MEDIUM_SALT_BUD_STEP.get(),
                    () -> MEDIUM_SALT_BUD_PLACE.get(),
                    () -> MEDIUM_SALT_BUD_HIT.get(),
                    () -> MEDIUM_SALT_BUD_FALL.get());

            public static final SoundType SMALL_SALT_BUD = new ForgeSoundType(1.0f, 1.0f,
                    () -> SMALL_SALT_BUD_BREAK.get(),
                    () -> SMALL_SALT_BUD_STEP.get(),
                    () -> SMALL_SALT_BUD_PLACE.get(),
                    () -> SMALL_SALT_BUD_HIT.get(),
                    () -> SMALL_SALT_BUD_FALL.get());
        }
    }

    public static class ItemTags {
        public static final TagKey<Item> SALT = net.minecraft.tags.ItemTags.create(
                new ResourceLocation("forge", "salts"));
        public static final TagKey<Item> CAN_BE_SALTED = net.minecraft.tags.ItemTags.create(
                Salt.resource("can_be_salted"));
    }

    public static class BlockTags {
        public static final TagKey<Block> HEATERS = net.minecraft.tags.BlockTags.create(
                Salt.resource("heaters"));
        public static final TagKey<Block> SALT_CLUSTER_REPLACEABLES = net.minecraft.tags.BlockTags.create(
                Salt.resource("salt_cluster_replaceables"));
    }

    public static class BiomeTags {
        public static final TagKey<Biome> HAS_ROCK_SALT_DEPOSITS = TagKey.create(net.minecraft.core.Registry.BIOME_REGISTRY,
                Salt.resource("has_rock_salt_deposits"));
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

    public static class ConfiguredFeatures {
        public static final Holder<ConfiguredFeature<MineralDepositConfiguration, ?>> ROCK_SALT;

        static {
            MineralDepositConfiguration.DepositBlockStateInfo stoneBlockStateInfo = MineralDepositConfiguration.blockStateInfo(
                    new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                            .add(Blocks.ROCK_SALT_ORE.get().defaultBlockState(), 8)
                            .add(Blocks.RAW_ROCK_SALT_BLOCK.get().defaultBlockState(), 1)
                            .build()),
                    OreFeatures.STONE_ORE_REPLACEABLES);

            MineralDepositConfiguration.DepositBlockStateInfo deepslateBlockStateInfo = MineralDepositConfiguration.blockStateInfo(
                    new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                            .add(Blocks.DEEPSLATE_ROCK_SALT_ORE.get().defaultBlockState(), 8)
                            .add(Blocks.RAW_ROCK_SALT_BLOCK.get().defaultBlockState(), 1)
                            .build()),
                    OreFeatures.DEEPSLATE_ORE_REPLACEABLES);

            MineralDepositConfiguration.DepositBlockStateInfo clusterBlockStateInfo = MineralDepositConfiguration.blockStateInfo(
                    new WeightedStateProvider(SimpleWeightedRandomList.<BlockState>builder()
                            .add(Blocks.SALT_CLUSTER.get().defaultBlockState(), 16)
                            .add(Blocks.LARGE_SALT_BUD.get().defaultBlockState(), 2)
                            .add(Blocks.MEDIUM_SALT_BUD.get().defaultBlockState(), 1)
                            .add(Blocks.SMALL_SALT_BUD.get().defaultBlockState(), 1)
                            .build()),
                    new TagMatchTest(BlockTags.SALT_CLUSTER_REPLACEABLES));

            ROCK_SALT = FeatureUtils.register("mineral_rock_salt", WorldGenFeatures.MINERAL_DEPOSIT.get(),
                    new MineralDepositConfiguration(List.of(stoneBlockStateInfo, deepslateBlockStateInfo), clusterBlockStateInfo,
                            Configuration.ROCK_SALT_SIZE.get(), (float)Configuration.ROCK_SALT_CLUSTER_CHANCE.get().doubleValue()));
        }
    }
}
