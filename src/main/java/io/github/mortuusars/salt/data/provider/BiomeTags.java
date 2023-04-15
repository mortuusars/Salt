package io.github.mortuusars.salt.data.provider;

import io.github.mortuusars.salt.Salt;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class BiomeTags extends BiomeTagsProvider {
    public BiomeTags(DataGenerator generator, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator.getPackOutput(), provider, Salt.ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        tag(Salt.BiomeTags.HAS_ROCK_SALT_DEPOSITS)
                .addTag(net.minecraft.tags.BiomeTags.IS_OCEAN)
                .addTag(net.minecraft.tags.BiomeTags.IS_BEACH)
                .add(Biomes.DRIPSTONE_CAVES);
    }
}
