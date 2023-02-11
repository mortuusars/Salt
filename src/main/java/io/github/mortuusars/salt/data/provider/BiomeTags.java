package io.github.mortuusars.salt.data.provider;

import io.github.mortuusars.salt.Salt;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class BiomeTags extends BiomeTagsProvider {
    public BiomeTags(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, Salt.ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(Salt.BiomeTags.HAS_ROCK_SALT_DEPOSITS)
                .addTag(net.minecraft.tags.BiomeTags.IS_OCEAN)
                .addTag(net.minecraft.tags.BiomeTags.IS_BEACH)
                .add(Biomes.DRIPSTONE_CAVES);
    }
}
