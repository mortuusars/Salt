package io.github.mortuusars.salt.data.provider;


import io.github.mortuusars.salt.Salt;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

@SuppressWarnings("UnusedReturnValue")
public class ItemModels extends ItemModelProvider {
    public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator.getPackOutput(), Salt.ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        singleTexture("salted_overlay", mcLoc("item/generated"), "layer0", modLoc("item/salted_overlay"));

        withExistingParent(Salt.Blocks.SALT_CLUSTER.getId().getPath(), mcLoc("item/amethyst_cluster"))
                .texture("layer0", modLoc("block/" + Salt.Blocks.SALT_CLUSTER.getId().getPath()));
        withExistingParent(Salt.Blocks.LARGE_SALT_BUD.getId().getPath(), mcLoc("item/large_amethyst_bud"))
                .texture("layer0", modLoc("block/" + Salt.Blocks.LARGE_SALT_BUD.getId().getPath()));
        withExistingParent(Salt.Blocks.MEDIUM_SALT_BUD.getId().getPath(), mcLoc("item/medium_amethyst_bud"))
                .texture("layer0", modLoc("block/" + Salt.Blocks.MEDIUM_SALT_BUD.getId().getPath()));
        withExistingParent(Salt.Blocks.SMALL_SALT_BUD.getId().getPath(), mcLoc("item/small_amethyst_bud"))
                .texture("layer0", modLoc("block/" + Salt.Blocks.SMALL_SALT_BUD.getId().getPath()));

        singleTextureItem(Salt.Items.SALT.get());
        singleTextureItem(Salt.Items.RAW_ROCK_SALT.get());
        blockItem(Salt.Items.SALT_BLOCK.get());
        withExistingParent(Salt.Blocks.ROCK_SALT_ORE.getId().getPath(),
                modLoc("block/" + Salt.Blocks.ROCK_SALT_ORE.getId().getPath() + "_1"));
        withExistingParent(Salt.Blocks.DEEPSLATE_ROCK_SALT_ORE.getId().getPath(),
                modLoc("block/" + Salt.Blocks.DEEPSLATE_ROCK_SALT_ORE.getId().getPath() + "_1"));
        blockItem(Salt.Items.RAW_ROCK_SALT_BLOCK.get());

        blockItem(Salt.Items.SALT_LAMP.get());
    }

    private ItemModelBuilder blockItem(BlockItem item) {
        return withExistingParent(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).getPath(),
                modLoc("block/" + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(item.getBlock())).getPath()));
    }

    private ItemModelBuilder singleTextureItem(Item item) {
        String itemPath = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).getPath();
        return singleTexture(itemPath,
                mcLoc("item/generated"), "layer0",
                modLoc("item/" + itemPath));
    }
}
