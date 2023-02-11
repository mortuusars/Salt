package io.github.mortuusars.salt.configuration;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class Configuration {
    public static final ForgeConfigSpec COMMON;
    public static final ForgeConfigSpec CLIENT;

    // Salting:
    public static final ForgeConfigSpec.IntValue SALTING_ADDITIONAL_NUTRITION;
    public static final ForgeConfigSpec.DoubleValue SALTING_ADDITIONAL_SATURATION_MODIFIER;

    // Evaporation
    public static final ForgeConfigSpec.BooleanValue EVAPORATION_ENABLED;
    public static final ForgeConfigSpec.DoubleValue EVAPORATION_CHANCE;

    // Rock Salt:
    public static final ForgeConfigSpec.BooleanValue GENERATE_ROCK_SALT;

    public static final ForgeConfigSpec.IntValue ROCK_SALT_DEPOSIT_QUANTITY;
    public static final ForgeConfigSpec.IntValue ROCK_SALT_SIZE;
    public static final ForgeConfigSpec.IntValue ROCK_SALT_MIN_HEIGHT;
    public static final ForgeConfigSpec.IntValue ROCK_SALT_MAX_HEIGHT;
    public static final ForgeConfigSpec.DoubleValue ROCK_SALT_CLUSTER_CHANCE;

    // CLIENT

    public static final ForgeConfigSpec.BooleanValue SALTED_OVERLAY;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("Salting foods to add extra nutrition. Can be disabled by removing 'salt:salting' recipe " +
                "or removing all items from 'salt:can_be_salted' tag.").push("Salting");

        SALTING_ADDITIONAL_NUTRITION = builder
                .comment("Amount of additional nutrition that salted food provides. 1 nutrition = half of hunger shank.",
                        "Negative values will decrease hunger restored.")
                .defineInRange("SaltingNutrition", 2, -20, 20);
        SALTING_ADDITIONAL_SATURATION_MODIFIER = builder
                .comment("Amount of additional saturation that salted food provides.")
                .defineInRange("SaltingSaturation", 0.5f, -10.0, 10.0f);

        builder.pop();



        builder.push("Evaporation");

        EVAPORATION_ENABLED = builder
                .comment("Water in a cauldron with a heat source beneath will evaporate and salt will be formed in the cauldron")
                .define("EvaporationEnabled", true);
        EVAPORATION_CHANCE = builder
                .comment("Chance of water evaporating on random tick. 1.0 = first random tick. 0.0 = never.")
                .defineInRange("EvaporationChance", 0.5d, 0.0d, 1.0d);

        builder.pop();



        builder.push("RockSalt");

        GENERATE_ROCK_SALT = builder
                .comment("Rock Salt Deposits will generate in biomes defined in tag 'salt/tags/worldgen/biome/has_rock_salt_deposits'")
                .define("GenerateRockSalt", true);

        builder.push("Deposit");

        ROCK_SALT_DEPOSIT_QUANTITY = builder
                .comment("Quantity of Rock Salt deposits generated. Larger number = more deposits.")
                .defineInRange("RockSaltDepositQuantity", 6, 1, 999);

        ROCK_SALT_SIZE = builder
                .comment("Size of the Rock Salt deposit")
                .defineInRange("RockSaltSize", 64, 1, 64);

        ROCK_SALT_MIN_HEIGHT = builder
                .comment("Lowest y-level at which Rock Salt would be able to generate")
                .defineInRange("RockSaltMinHeight", 5, -60, 300);

        ROCK_SALT_MAX_HEIGHT = builder
                .comment("Highest y-level at which Rock Salt would be able to generate")
                .defineInRange("RockSaltMaxHeight", 110, -60, 300);

        ROCK_SALT_CLUSTER_CHANCE = builder
                .comment("Chance of the Salt Clusters generating on the deposits (per side)")
                .defineInRange("RockSaltClusterChance", 0.15f, 0.0, 1.0);

        builder.pop();

        builder.pop();

        COMMON = builder.build();



        builder = new ForgeConfigSpec.Builder();

        builder.push("Compatibility");

        SALTED_OVERLAY = builder
                .comment("Overlay texture will be drawn over salted foods. Disable if theres an issue with rendering items.")
                .define("SaltingOverlayEnabled", true);

        builder.pop();

        CLIENT = builder.build();
    }

    public static void init() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT);
    }
}
