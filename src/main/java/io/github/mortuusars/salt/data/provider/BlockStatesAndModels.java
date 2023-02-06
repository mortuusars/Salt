package io.github.mortuusars.salt.data.provider;

import io.github.mortuusars.salt.Salt;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStatesAndModels extends BlockStateProvider {

    public BlockStatesAndModels(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Salt.ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        String rockSaltPath = Salt.Blocks.ROCK_SALT.get().getRegistryName().getPath();

        BlockModelBuilder rockSaltDefaultModel1 = models().cubeAll(rockSaltPath + "_1",
                Salt.resource("block/" + rockSaltPath + "_1"));
        BlockModelBuilder rockSaltMirroredModel1 = models().withExistingParent(rockSaltPath + "_1_mirrored", mcLoc("block/cube_mirrored_all"))
                .texture("all", Salt.resource("block/" + rockSaltPath + "_1"));

        BlockModelBuilder rockSaltDefaultModel2 = models().cubeAll(rockSaltPath + "_2",
                Salt.resource("block/" + rockSaltPath + "_2"));
        BlockModelBuilder rockSaltMirroredModel2 = models().withExistingParent(rockSaltPath + "_2_mirrored", mcLoc("block/cube_mirrored_all"))
                .texture("all", Salt.resource("block/" + rockSaltPath + "_2"));


        simpleBlock(Salt.Blocks.SALT_BLOCK.get(),
                models()
                    .cubeAll(Salt.Blocks.SALT_BLOCK.get().getRegistryName().getPath(), blockTexture(Salt.Blocks.SALT_BLOCK.get())));

        simpleBlock(Salt.Blocks.RAW_ROCK_SALT_BLOCK.get(),
                models()
                    .cubeAll(Salt.Blocks.RAW_ROCK_SALT_BLOCK.get().getRegistryName().getPath(), blockTexture(Salt.Blocks.RAW_ROCK_SALT_BLOCK.get())));

        directionalBlock(Salt.Blocks.SALT_CLUSTER.get(), models().cross(Salt.Blocks.SALT_CLUSTER.get().getRegistryName().getPath(),
                blockTexture(Salt.Blocks.SALT_CLUSTER.get())));
        directionalBlock(Salt.Blocks.LARGE_SALT_BUD.get(), models().cross(Salt.Blocks.LARGE_SALT_BUD.get().getRegistryName().getPath(),
                blockTexture(Salt.Blocks.LARGE_SALT_BUD.get())));
        directionalBlock(Salt.Blocks.MEDIUM_SALT_BUD.get(), models().cross(Salt.Blocks.MEDIUM_SALT_BUD.get().getRegistryName().getPath(),
                blockTexture(Salt.Blocks.MEDIUM_SALT_BUD.get())));
        directionalBlock(Salt.Blocks.SMALL_SALT_BUD.get(), models().cross(Salt.Blocks.SMALL_SALT_BUD.get().getRegistryName().getPath(),
                blockTexture(Salt.Blocks.SMALL_SALT_BUD.get())));

//        MultiVariantGenerator rockSaltVariants = MultiVariantGenerator.multiVariant(Salt.Blocks.ROCK_SALT.get(),
//                Variant.variant().with(VariantProperties.MODEL, rockSaltDefaultModel1.getLocation()),
//                Variant.variant().with(VariantProperties.MODEL, rockSaltMirroredModel1.getLocation()),
//                Variant.variant().with(VariantProperties.MODEL, rockSaltDefaultModel1.getLocation())
//                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180),
//                Variant.variant().with(VariantProperties.MODEL, rockSaltMirroredModel1.getLocation())
//                        .with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180));

//        getVariantBuilder(Salt.Blocks.ROCK_SALT.get())
//                .addModels(ConfiguredModel.builder()
//                        .modelFile(rockSaltDefaultModel1)
//                        .modelFile(models()
//                                .withExistingParent(rockSaltPath + "_2", mcLoc("block/cube_mirrored_all"))
//                                .texture("all", Salt.resource("block/" + rockSaltPath + "_2")))
//                        .build()).;
    }
}