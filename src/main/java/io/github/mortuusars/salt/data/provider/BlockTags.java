package io.github.mortuusars.salt.data.provider;

import io.github.mortuusars.salt.Salt;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BlockTags extends BlockTagsProvider {
    public BlockTags(DataGenerator generator, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator.getPackOutput(), lookupProvider, Salt.ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(net.minecraft.tags.BlockTags.MINEABLE_WITH_PICKAXE)
                .add(Salt.Blocks.ROCK_SALT_ORE.get())
                .add(Salt.Blocks.DEEPSLATE_ROCK_SALT_ORE.get())
                .add(Salt.Blocks.RAW_ROCK_SALT_BLOCK.get())
                .add(Salt.Blocks.SALT_CLUSTER.get())
                .add(Salt.Blocks.LARGE_SALT_BUD.get())
                .add(Salt.Blocks.MEDIUM_SALT_BUD.get())
                .add(Salt.Blocks.SMALL_SALT_BUD.get())
                .add(Salt.Blocks.SALT_CAULDRON.get());

        tag(net.minecraft.tags.BlockTags.MINEABLE_WITH_SHOVEL)
                .add(Salt.Blocks.SALT_BLOCK.get());

        tag(Salt.BlockTags.HEATERS)
                .add(Blocks.CAMPFIRE)
                .add(Blocks.SOUL_CAMPFIRE)
                .add(Blocks.FIRE)
                .add(Blocks.SOUL_FIRE)
                .add(Blocks.MAGMA_BLOCK);

        tag(Salt.BlockTags.SALT_CLUSTER_GROWABLES)
                .add(Salt.Blocks.RAW_ROCK_SALT_BLOCK.get());

        tag(Salt.BlockTags.SALT_DISSOLVABLES)
                .add(Blocks.WATER);

        tag(Salt.BlockTags.MELTABLES)
                .addTag(net.minecraft.tags.BlockTags.ICE)
                .addTag(net.minecraft.tags.BlockTags.SNOW)
                .remove(Blocks.FROSTED_ICE)
                .remove(Blocks.PACKED_ICE);

        tag(Salt.BlockTags.SALT_CLUSTER_REPLACEABLES)
                .add(Blocks.AIR)
                .add(Blocks.CAVE_AIR)
                .add(Blocks.VOID_AIR);
    }
}
