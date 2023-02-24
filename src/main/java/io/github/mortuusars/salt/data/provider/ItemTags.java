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

        optional(canBeSaltedTag, "farmersdelight",
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

        optional(canBeSaltedTag, "miners_delight",
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
        optional(canBeSaltedTag, "butchercraft",
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

        optional(canBeSaltedTag, "quark",
                "cooked_frog_leg",
                "cooked_crab_leg");

        optional(canBeSaltedTag, "aquaculture",
                "algae",
                "fish_fillet_cooked",
                "frog_legs_cooked",
                "turtle_soup",
                "sushi");
    }

    private void optionalTags(TagAppender<Item> tag, String namespace, String... items) {
        for (String item : items) {
            tag.addOptionalTag(new ResourceLocation(namespace, item));
        }
    }

    private void optional(TagAppender<Item> tag, String namespace, String... items) {
        for (String item : items) {
            tag.addOptional(new ResourceLocation(namespace, item));
        }
    }
}