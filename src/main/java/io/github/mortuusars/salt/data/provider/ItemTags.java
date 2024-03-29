package io.github.mortuusars.salt.data.provider;

import io.github.mortuusars.salt.Salt;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ItemTags extends ItemTagsProvider {
    public ItemTags(DataGenerator generator, CompletableFuture<HolderLookup.Provider> pLookupProvider, BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator.getPackOutput(), pLookupProvider, blockTagsProvider.contentsGetter(), Salt.ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(Salt.ItemTags.FORGE_TORCHES)
                .add(Items.TORCH);

        tag(Salt.ItemTags.FORGE_SALTS)
                .add(Salt.Items.SALT.get());

        tag(TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), new ResourceLocation("forge:salt")))
                .add(Salt.Items.SALT.get());
        tag(TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), new ResourceLocation("forge:dusts")))
                .add(Salt.Items.SALT.get());
        tag(TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), new ResourceLocation("forge:dusts/salt")))
                .add(Salt.Items.SALT.get());
    }
}