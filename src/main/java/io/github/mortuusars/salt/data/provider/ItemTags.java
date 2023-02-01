package io.github.mortuusars.salt.data.provider;

import io.github.mortuusars.salt.Registry;
import io.github.mortuusars.salt.Salt;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
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
        tag(Registry.ItemTags.CAN_BE_SALTED)
                // Vanilla
                .add(Items.COOKED_BEEF)
                .add(Items.COOKED_CHICKEN)
                .add(Items.COOKED_MUTTON)
                .add(Items.COOKED_RABBIT)
                .add(Items.COOKED_PORKCHOP)
                .add(Items.COOKED_COD)
                .add(Items.COOKED_SALMON)

                .add(Items.BAKED_POTATO)
                .add(Items.BREAD)

                .add(Items.SUSPICIOUS_STEW)
                .add(Items.BEETROOT_SOUP)
                .add(Items.MUSHROOM_STEW)
                .add(Items.RABBIT_STEW)

                .add(Items.ROTTEN_FLESH)

                // Forge:
                .addOptional(forge("bread"))
                .addOptional(forge("cooked_beef"))
                .addOptional(forge("cooked_bacon"))
                .addOptional(forge("cooked_chicken"))
                .addOptional(forge("cooked_pork"))
                .addOptional(forge("cooked_mutton"))
                .addOptional(forge("cooked_rabbit"))
                .addOptional(forge("cooked_fishes"))

                .addOptional(forge("crops/cabbage"))
                .addOptional(forge("crops/tomato"))
                .addOptional(forge("crops/onion"))
                .addOptional(forge("salad_ingredients"))

                // Farmer's Delight:

                .addOptional(fd("fried_egg"))
                .addOptional(fd("tomato_sauce"))
                .addOptional(fd("smoked_ham"))
                .addOptional(fd("mixed_salad"))
                .addOptional(fd("barbecue_stick"))
                .addOptional(fd("egg_sandwich"))
                .addOptional(fd("chicken_sandwich"))
                .addOptional(fd("hamburger"))
                .addOptional(fd("bacon_sandwich"))
                .addOptional(fd("mutton_wrap"))
                .addOptional(fd("dumplings"))
                .addOptional(fd("stuffed_potato"))
                .addOptional(fd("cabbage_rolls"))
                .addOptional(fd("salmon_roll"))
                .addOptional(fd("cod_roll"))
                .addOptional(fd("kelp_roll"))
                .addOptional(fd("kelp_roll_slice"))
                .addOptional(fd("cooked_rice"))
                .addOptional(fd("bone_broth"))
                .addOptional(fd("beef_stew"))
                .addOptional(fd("chicken_soup"))
                .addOptional(fd("vegetable_soup"))
                .addOptional(fd("fish_stew"))
                .addOptional(fd("fried_rice"))
                .addOptional(fd("pumpkin_soup"))
                .addOptional(fd("baked_cod_stew"))
                .addOptional(fd("noodle_soup"))
                .addOptional(fd("bacon_and_eggs"))
                .addOptional(fd("pasta_with_meatballs"))
                .addOptional(fd("pasta_with_mutton_chop"))
                .addOptional(fd("mushroom_rice"))
                .addOptional(fd("roasted_mutton_chops"))
                .addOptional(fd("vegetable_noodles"))
                .addOptional(fd("steak_and_potatoes"))
                .addOptional(fd("ratatouille"))
                .addOptional(fd("squid_ink_pasta"))
                .addOptional(fd("grilled_salmon"))
                .addOptional(fd("roast_chicken"))
                .addOptional(fd("stuffed_pumpkin"))
                .addOptional(fd("honey_glazed_ham"))
                .addOptional(fd("shepherds_pie"))
        ;

    }

    private ResourceLocation forge(String path) {
        return new ResourceLocation("forge", path);
    }

    private ResourceLocation fd(String path) {
        return new ResourceLocation("farmersdelight", path);
    }
}