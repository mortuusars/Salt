package io.github.mortuusars.salt.data.provider;

import io.github.mortuusars.salt.Salt;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ItemTags extends ItemTagsProvider {
    public ItemTags(DataGenerator generator, BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, blockTagsProvider, Salt.ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(Salt.ItemTags.FORGE_TORCHES)
                .add(Items.TORCH);

        tag(Salt.ItemTags.FORGE_SALTS)
                .add(Salt.Items.SALT.get());

        TagAppender<Item> canBeSaltedTag = tag(Salt.ItemTags.CAN_BE_SALTED);

        // Vanilla
        canBeSaltedTag
                .add(// Meat
                     Items.COOKED_BEEF,
                     Items.COOKED_CHICKEN,
                     Items.COOKED_MUTTON,
                     Items.COOKED_RABBIT,
                     Items.COOKED_PORKCHOP,
                     Items.COOKED_COD,
                     Items.COOKED_SALMON,
                     // Vegetables
                     Items.BAKED_POTATO,
                     Items.BREAD,
                     // Soups
                     Items.SUSPICIOUS_STEW,
                     Items.BEETROOT_SOUP,
                     Items.MUSHROOM_STEW,
                     Items.RABBIT_STEW,
                     // Misc
                     Items.ROTTEN_FLESH);

        optionalTags(canBeSaltedTag, "forge",
                // Meat
                "bread",
                "cooked_beef",
                "cooked_bacon",
                "cooked_chicken",
                "cooked_pork",
                "cooked_mutton",
                "cooked_rabbit",
                "cooked_fishes",
                // Vegetables
                "crops/cabbage",
                "crops/tomato",
                "crops/onion",
                "salad_ingredients");

        optionalItems(canBeSaltedTag, "farmersdelight",
                "fried_egg",
                "tomato_sauce",
                "smoked_ham",
                "mixed_salad",
                "barbecue_stick",
                "egg_sandwich",
                "chicken_sandwich",
                "hamburger",
                "bacon_sandwich",
                "mutton_wrap",
                "dumplings",
                "stuffed_potato",
                "cabbage_rolls",
                "salmon_roll",
                "cod_roll",
                "kelp_roll",
                "kelp_roll_slice",
                "cooked_rice",
                "bone_broth",
                "beef_stew",
                "chicken_soup",
                "vegetable_soup",
                "fish_stew",
                "fried_rice",
                "pumpkin_soup",
                "baked_cod_stew",
                "noodle_soup",
                "bacon_and_eggs",
                "pasta_with_meatballs",
                "pasta_with_mutton_chop",
                "mushroom_rice",
                "roasted_mutton_chops",
                "vegetable_noodles",
                "steak_and_potatoes",
                "ratatouille",
                "squid_ink_pasta",
                "grilled_salmon",
                "roast_chicken",
                "stuffed_pumpkin",
                "honey_glazed_ham",
                "shepherds_pie");

        optionalItems(canBeSaltedTag, "miners_delight",
                "bat_wing",
                "smoked_bat_wing",
                "squid",
                "glow_squid",
                "baked_squid",
                "tentacles",
                "baked_tentacles",
                "improvised_barbecue_stick",
                "pasta_with_veggieballs",
                "cave_soup",
                "bowl_of_stuffed_squid",
                "beetroot_soup_cup",
                "mushroom_stew_cup",
                "rabbit_stew_cup",
                "baked_cod_stew_cup",
                "noodle_stew_cup",
                "beef_stew_cup",
                "cave_stew_cup",
                "chicken_stew_cup",
                "fish_stew_cup",
                "pumpkin_soup_cup",
                "vegetable_soup_cup");

        optionalTags(canBeSaltedTag, "butchercraft",
//                "any_offal",
//                "any_meat_raw",
                "any_meat_cooked");
        optionalItems(canBeSaltedTag, "butchercraft",
                "pork_stew",
                "lamb_stew",
                "sausage_roll",
                "sos",
                "liver_onions",
                "fries",
                "fried_fish",
                "chicken_fried_steak",
                "pork_tenderloin",
                "fried_chicken",
                "stuffed_heart",
                "fried_brains",
                "oxtail_soup",
                "hash",
                "potroast",
                "salisbury_steak",
                "bbq_ribs",
                "meat_pie_slice",
                "pulled_pork_sandwich",
                "mashed_potato_gravy",
                "rack_lamb",
                "stirfry",
                "beef_wellington",
                "haggis");

        optionalItems(canBeSaltedTag, "quark",
                "cooked_frog_leg",
                "cooked_crab_leg");

        optionalItems(canBeSaltedTag, "aquaculture",
                "algae",
                "fish_fillet_cooked",
                "frog_legs_cooked",
                "turtle_soup",
                "sushi");

        optionalItems(canBeSaltedTag, "croptopia",
                "baked_beans",
                "baked_sweet_potato",
                "baked_yam",
                "cooked_anchovy",
                "cooked_calamari",
                "cooked_shrimp",
                "cooked_tuna",
                "popcorn",
                "toast",
                "popcorn",
                "tofu",
                "pepperoni",
                "beef_jerky",
                "pork_jerky",
                "cucumber_salad",
                "caesar_salad",
                "leafy_salad",
                "veggie_salad",
                "pork_and_beans",
                "leek_soup",
                "roasted_nuts",
                "scrambled_eggs",
                "buttered_toast",
                "ham_sandwich",
                "blt",
                "grilled_cheese",
                "tuna_sandwich",
                "cheeseburger",
                "hamburger",
                "tofuburger",
                "pizza",
                "supreme_pizza",
                "cheese_pizza",
                "pineapple_pepperoni_pizza",
                "lemon_chicken",
                "fried_chicken",
                "chicken_and_noodles",
                "chicken_and_dumplings",
                "tofu_and_dumplings",
                "spaghetti_squash",
                "chicken_and_rice",
                "taco",
                "sushi",
                "egg_roll",
                "cashew_chicken",
                "burrito",
                "tostada",
                "carnitas",
                "fajitas",
                "enchilada",
                "stuffed_poblanos",
                "refried_beans",
                "quesadilla",
                "beef_wellington",
                "cornish_pasty",
                "avocado_toast",
                "beef_stew",
                "beef_stir_fry",
                "cheesy_asparagus",
                "eggplant_parmesan",
                "potato_soup",
                "ratatouille",
                "steamed_broccoli",
                "steamed_green_beans",
                "stir_fry",
                "toast_sandwich",
                "corn_bread",
                "cabbage_roll",
                "borscht",
                "goulash",
                "beetroot_salad",
                "steamed_crab",
                "deep_fried_shrimp",
                "tuna_roll",
                "fried_calamari",
                "anchovy_pizza",
                "baked_crepes",
                "croque_madame",
                "croque_monsieur",
                "dauphine_potatoes",
                "fried_frog_legs",
                "hashed_brown",
                "quiche",
                "sunny_side_eggs",
                "the_big_breakfast");

        optionalItems(canBeSaltedTag, "netherdepthsupgrade",
                "cooked_lava_pufferfish_slice",
                "cooked_obsidianfish_slice",
                "cooked_magmacubefish_slice",
                "cooked_glowdine_slice",
                "cooked_soulsucker_slice",
                "lava_pufferfish_roll",
                "obsidianfish_roll",
                "searing_cod_roll",
                "blazefish_roll",
                "magmacubefish_roll",
                "glowdine_roll",
                "soulsucker_roll",
                "warped_kelp_roll",
                "warped_kelp_roll_slice",
                "grilled_lava_pufferfish",
                "grilled_obsidianfish",
                "grilled_searing_cod",
                "grilled_blazefish",
                "grilled_magmacubefish",
                "grilled_glowdine",
                "grilled_soulsucker",
                "baked_lava_pufferfish_stew",
                "baked_obsidianfish_stew",
                "baked_searing_cod_stew",
                "baked_blazefish_stew",
                "baked_magmacubefish_stew",
                "baked_glowdine_stew",
                "baked_soulsucker_stew");

        optionalItems(canBeSaltedTag, "delightful",
                "field_salad",
                "cheeseburger",
                "deluxe_cheeseburger",
                "cooked_venison_chops",
                "cooked_goat");

        optionalItems(canBeSaltedTag, "additionaladditions",
                "fried_egg",
                "chicken_nugget"
        );
    }

    private void optionalTags(TagAppender<Item> tag, String namespace, String... items) {
        for (String item : items) {
            tag.addOptionalTag(new ResourceLocation(namespace, item));
        }
    }

    private void optionalItems(TagAppender<Item> tag, String namespace, String... items) {
        for (String item : items) {
            tag.addOptional(new ResourceLocation(namespace, item));
        }
    }
}