package io.github.mortuusars.salt.configuration;

import com.mojang.logging.LogUtils;
import io.github.mortuusars.salt.Salting;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration {
    public static final ForgeConfigSpec COMMON;
    public static final ForgeConfigSpec CLIENT;

    // Salting:
    public static final ForgeConfigSpec.IntValue SALTING_ADDITIONAL_NUTRITION;
    public static final ForgeConfigSpec.DoubleValue SALTING_ADDITIONAL_SATURATION_MODIFIER;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> SALTING_INDIVIDUAL_VALUES;
    public static final Map<String, Salting.FoodValue> FOOD_VALUES = new HashMap<>();

    // Dissolving
    public static final ForgeConfigSpec.BooleanValue DISSOLVING_ENABLED;
    public static final ForgeConfigSpec.DoubleValue DISSOLVING_CHANCE;
    public static final ForgeConfigSpec.BooleanValue DISSOLVING_FLUID_SOURCE_CONVERSION;
    public static final ForgeConfigSpec.BooleanValue DISSOLVING_IN_RAIN;
    public static final ForgeConfigSpec.DoubleValue DISSOLVING_IN_RAIN_CHANCE;

    // Melting
    public static final ForgeConfigSpec.BooleanValue MELTING_ITEM_ENABLED;
    public static final ForgeConfigSpec.BooleanValue MELTING_BY_BLOCK_ENABLED;
    public static final ForgeConfigSpec.DoubleValue MELTING_BLOCK_CHANCE;
    public static final ForgeConfigSpec.BooleanValue MELTING_PLACES_WATER;

    // Evaporation
    public static final ForgeConfigSpec.BooleanValue EVAPORATION_ENABLED;
    public static final ForgeConfigSpec.DoubleValue EVAPORATION_CHANCE;

    // Cluster Growing
    public static final ForgeConfigSpec.BooleanValue SALT_CLUSTER_GROWING_ENABLED;
    public static final ForgeConfigSpec.DoubleValue SALT_CLUSTER_GROWING_CHANCE;

    // Rock Salt:
    public static final ForgeConfigSpec.IntValue ROCK_SALT_SIZE;
    public static final ForgeConfigSpec.DoubleValue ROCK_SALT_CLUSTER_CHANCE;

    // Villager Trades:
    public static final ForgeConfigSpec.BooleanValue BUTCHER_SALT_TRADES_ENABLED;


    // CLIENT

    public static final ForgeConfigSpec.BooleanValue SALTED_OVERLAY_ENABLED;

    // JEI:
    public static final ForgeConfigSpec.BooleanValue JEI_SALT_EVAPORATION_ENABLED;
    public static final ForgeConfigSpec.BooleanValue JEI_SALT_CRYSTAL_GROWING_ENABLED;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("Salting foods to add extra nutrition. Can be disabled by removing 'salt:salting' recipe " +
                "or removing all items from 'salt/tags/items/can_be_salted' tag.").push("Salting");

        SALTING_ADDITIONAL_NUTRITION = builder
                .comment("Amount of additional nutrition that salted food provides. 1 nutrition = half of hunger shank.",
                        "Negative values will decrease hunger restored.")
                .defineInRange("SaltingNutrition", 2, -20, 20);
        SALTING_ADDITIONAL_SATURATION_MODIFIER = builder
                .comment("Amount of additional saturation modifier to apply when food is eaten.")
                .defineInRange("SaltingSaturationModifier", 0.1f, -10.0, 10.0f);

        SALTING_INDIVIDUAL_VALUES = builder
                .comment("Additional nutrition and saturation values per food. Overrides default values (defined above).",
                        "Format: itemRegistryName,nutrition,[saturationModifier].",
                        "Separated by commas. Saturation is optional (will use default value if not specified)")
                .defineList("SaltingFoodValues", List.of("minecraft:rotten_flesh,1,0.05"), o -> true);

        builder.pop();



        builder.push("Dissolving");

        DISSOLVING_ENABLED = builder
                .comment("Salt blocks will dissolve if adjacent to blocks defined in the tag 'salt/tags/blocks/salt_dissolvables'")
                .define("SaltDissolvingEnabled", true);

        DISSOLVING_CHANCE = builder
                .comment("Chance of water dissolving in fluid on random tick. 1.0 = first random tick. 0.0 = never.")
                .defineInRange("SaltDissolvingChance", 0.35d, 0.0d, 1.0d);

        DISSOLVING_FLUID_SOURCE_CONVERSION = builder
                .comment("If dissolved by a fluid source block - salt will convert to a fluid source block instead of air.")
                .define("SaltDissolvesIntoSourceBlocks", true);

        DISSOLVING_IN_RAIN = builder
                .comment("Salt blocks will dissolve when exposed to rain")
                .define("SaltDissolvingInRain", true);

        DISSOLVING_IN_RAIN_CHANCE = builder
                .comment("Chance of salt blocks dissolving in rain on random tick. 1.0 = first random tick. 0.0 = never.")
                .defineInRange("SaltDissolvingInRainChance", 0.15d, 0.0d, 1.0d);

        builder.pop();



        builder.push("Melting");

        MELTING_ITEM_ENABLED = builder
                .comment("Salt (item) will melt clicked blocks defined in tag 'salt/tags/blocks/meltables'.")
                .define("SaltItemMeltingEnabled", true);

        MELTING_BY_BLOCK_ENABLED = builder
                .comment("Salt blocks will melt adjacent blocks defined in tag 'salt/tags/blocks/meltables' on random tick.")
                .define("SaltBlockMeltingEnabled", true);

        MELTING_BLOCK_CHANCE = builder
                .comment("Chance of block melting on random tick. 1.0 = first random tick. 0.0 = never.")
                .defineInRange("SaltBlockMeltingChance", 0.4d, 0.0d, 1.0d);

        MELTING_PLACES_WATER = builder
                .comment("Melted block will be replaced with Water source block.")
                .define("SaltMeltingPlaceWater", true);

        builder.pop();



        builder.push("Evaporation");

        EVAPORATION_ENABLED = builder
                .comment("Water in a cauldron with a heat source beneath (defined in tag 'salt/tags/blocks/heaters') " +
                        "will evaporate and salt will be formed in the cauldron")
                .define("EvaporationEnabled", true);
        EVAPORATION_CHANCE = builder
                .comment("Chance of water evaporating on random tick. 1.0 = first random tick. 0.0 = never.")
                .defineInRange("EvaporationChance", 0.3d, 0.0d, 1.0d);

        builder.pop();



        builder.push("GrowingSaltClusters");

        SALT_CLUSTER_GROWING_ENABLED = builder
                .comment("Water dripping from a Pointed Dripstone on a blocks tagged as 'salt/tags/blocks/salt_cluster_growables' will grow Salt Clusters")
                .define("SaltClusterGrowingEnabled", true);
        SALT_CLUSTER_GROWING_CHANCE = builder
                .comment("Chance of cluster growing by one stage on random tick. 1.0 = first random tick. 0.0 = never.")
                .defineInRange("SaltClusterGrowingChance", 0.1d, 0.0d, 1.0d);

        builder.pop();




        builder.comment("Rock Salt Deposits will generate in biomes defined in tag 'salt/tags/worldgen/biome/has_rock_salt_deposits'",
                        "Since 1.19 - parts of the Rock Salt generation is defined in jsons.",
                        "To enable/disable the generation or configure the generation chances you'll need to create a datapack.",
                        "Enable/disable generation - 'salt/worldgen/biome_modifier/add_rock_salt_deposit.json' - {\"type\": \"forge:none\"} will disable the generation.",
                        "Changing generation chances - 'salt/worldgen/placed_feature/mineral_rock_salt.json'")
                .push("RockSalt");

        ROCK_SALT_SIZE = builder
                .comment("Size of the Rock Salt deposit")
                .defineInRange("RockSaltSize", 24, 1, 64);

        ROCK_SALT_CLUSTER_CHANCE = builder
                .comment("Chance of the Salt Clusters generating on the deposits (per side)")
                .defineInRange("RockSaltClusterChance", 0.15f, 0.0, 1.0);

        builder.pop();




        builder.push("VillagerTrades");

        BUTCHER_SALT_TRADES_ENABLED = builder
                .comment("Butcher will buy and sell Salt.")
                .define("ButcherSaltTradesEnabled", true);

        builder.pop();


        COMMON = builder.build();



        builder = new ForgeConfigSpec.Builder();

        builder.push("JEI/REI");

        JEI_SALT_EVAPORATION_ENABLED = builder
                .comment("Water Evaporation category will be shown in JEI/REI. ",
                        "*Will be disabled automatically if evaporation is disabled or salt:heaters tag has no blocks.")
                .define("JeiWaterEvaporationEnabled", true);

        JEI_SALT_CRYSTAL_GROWING_ENABLED = builder
                .comment("Growing Salt Crystals category will be shown in JEI/REI.",
                        "*Will be disabled automatically if Growing Crystals is disabled.")
                .define("JeiGrowingSaltCrystalsEnabled", true);

        builder.pop();

        builder.push("Compatibility");

        SALTED_OVERLAY_ENABLED = builder
                .comment("Overlay texture will be drawn over salted foods. Disable if there's an issue with rendering items.")
                .define("SaltingOverlayEnabled", true);

        builder.pop();

        CLIENT = builder.build();
    }

    public static void init() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT);
    }

    public static void onConfigReload(final ModConfigEvent.Reloading ignoredEvent) {
        updateIndividualFoodValues();
    }

    public static void onConfigLoad(final ModConfigEvent.Loading ignoredEvent) {
        updateIndividualFoodValues();
    }

    private static void updateIndividualFoodValues() {
        FOOD_VALUES.clear();

        boolean loaded = Configuration.COMMON.isLoaded();

        if (!loaded)
            return;

        for (String entry : SALTING_INDIVIDUAL_VALUES.get()) {
            try {
                String[] parts = entry.split(",");

                @Nullable String itemPath = null;

                if (parts.length >= 1)
                    itemPath = parts[0];

                @Nullable Integer nutrition = null;
                @Nullable Float saturationMod = null;

                if (parts.length >= 2)
                    nutrition = Integer.parseInt(parts[1].trim());

                if (parts.length >= 3)
                    saturationMod = Float.parseFloat(parts[2].trim());

                if (itemPath != null && nutrition != null) {
                    FOOD_VALUES.put(itemPath, new Salting.FoodValue(nutrition, saturationMod != null ?
                            saturationMod
                            : SALTING_ADDITIONAL_SATURATION_MODIFIER.get().floatValue()));
                }
            }
            catch (Throwable e) {
                LogUtils.getLogger().error("Failed to parse food value '" + entry + "': " + e);
            }
        }
    }
}
