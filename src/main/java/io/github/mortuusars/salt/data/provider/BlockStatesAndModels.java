package io.github.mortuusars.salt.data.provider;

import io.github.mortuusars.salt.Salt;
import io.github.mortuusars.salt.block.SaltCauldronBlock;
import net.minecraft.data.DataGenerator;
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
        String rockSaltPath = Salt.Blocks.ROCK_SALT_ORE.get().getRegistryName().getPath();

        BlockModelBuilder rockSalt1 = models().cubeAll(rockSaltPath + "_1",
                Salt.resource("block/" + rockSaltPath + "_1"));
        BlockModelBuilder rockSaltMirrored1 = models().withExistingParent(rockSaltPath + "_1_mirrored", mcLoc("block/cube_mirrored_all"))
                .texture("all", Salt.resource("block/" + rockSaltPath + "_1"));

        BlockModelBuilder rockSalt2 = models().cubeAll(rockSaltPath + "_2",
                Salt.resource("block/" + rockSaltPath + "_2"));
        BlockModelBuilder rockSaltMirrored2 = models().withExistingParent(rockSaltPath + "_2_mirrored", mcLoc("block/cube_mirrored_all"))
                .texture("all", Salt.resource("block/" + rockSaltPath + "_2"));

        simpleBlock(Salt.Blocks.ROCK_SALT_ORE.get(), ConfiguredModel.builder()
                .modelFile(rockSalt1)
                .nextModel()
                .modelFile(rockSalt1).rotationY(180)
                .nextModel()
                .modelFile(rockSaltMirrored1)
                .nextModel()
                .modelFile(rockSaltMirrored1).rotationY(180)
                .nextModel()
                .modelFile(rockSalt2)
                .nextModel()
                .modelFile(rockSalt2).rotationY(180)
                .nextModel()
                .modelFile(rockSaltMirrored2)
                .nextModel()
                .modelFile(rockSaltMirrored2).rotationY(180)
                .build());

        String deepslateRockSaltPath = Salt.Blocks.DEEPSLATE_ROCK_SALT_ORE.get().getRegistryName().getPath();

        BlockModelBuilder deepslateRockSalt1 = models().cubeAll(deepslateRockSaltPath + "_1",
                Salt.resource("block/" + deepslateRockSaltPath + "_1"));
        BlockModelBuilder deepslateRockSaltMirrored1 = models().withExistingParent(deepslateRockSaltPath + "_1_mirrored", mcLoc("block/cube_mirrored_all"))
                .texture("all", Salt.resource("block/" + deepslateRockSaltPath + "_1"));

        BlockModelBuilder deepslateRockSalt2 = models().cubeAll(deepslateRockSaltPath + "_2",
                Salt.resource("block/" + deepslateRockSaltPath + "_2"));
        BlockModelBuilder deepslateRockSaltMirrored2 = models().withExistingParent(deepslateRockSaltPath + "_2_mirrored", mcLoc("block/cube_mirrored_all"))
                .texture("all", Salt.resource("block/" + deepslateRockSaltPath + "_2"));

        simpleBlock(Salt.Blocks.DEEPSLATE_ROCK_SALT_ORE.get(), ConfiguredModel.builder()
                .modelFile(deepslateRockSalt1)
                .nextModel()
                .modelFile(deepslateRockSalt1).rotationY(180)
                .nextModel()
                .modelFile(deepslateRockSaltMirrored1)
                .nextModel()
                .modelFile(deepslateRockSaltMirrored1).rotationY(180)
                .nextModel()
                .modelFile(deepslateRockSalt2)
                .nextModel()
                .modelFile(deepslateRockSalt2).rotationY(180)
                .nextModel()
                .modelFile(deepslateRockSaltMirrored2)
                .nextModel()
                .modelFile(deepslateRockSaltMirrored2).rotationY(180)
                .build());

        simpleBlock(Salt.Blocks.SALT_BLOCK.get());

        BlockModelBuilder rawRockSaltBlock = models().cubeAll(Salt.Blocks.RAW_ROCK_SALT_BLOCK.get().getRegistryName()
                .getPath(), blockTexture(Salt.Blocks.RAW_ROCK_SALT_BLOCK.get()));

        BlockModelBuilder rawRockSaltBlockMirrored = models()
                .withExistingParent(Salt.Blocks.RAW_ROCK_SALT_BLOCK.get().getRegistryName().getPath() + "_mirrored", mcLoc("block/cube_mirrored_all"))
                .texture("all", modLoc("block/" + Salt.Blocks.RAW_ROCK_SALT_BLOCK.get().getRegistryName().getPath()));

        simpleBlock(Salt.Blocks.RAW_ROCK_SALT_BLOCK.get(), ConfiguredModel.builder()
                .modelFile(rawRockSaltBlock)
                .nextModel()
                .modelFile(rawRockSaltBlock).rotationY(180)
                .nextModel()
                .modelFile(rawRockSaltBlockMirrored)
                .nextModel()
                .modelFile(rawRockSaltBlockMirrored).rotationY(180)
                .build());

        directionalBlock(Salt.Blocks.SALT_CLUSTER.get(), models().cross(Salt.Blocks.SALT_CLUSTER.get().getRegistryName().getPath(),
                blockTexture(Salt.Blocks.SALT_CLUSTER.get())));
        directionalBlock(Salt.Blocks.LARGE_SALT_BUD.get(), models().cross(Salt.Blocks.LARGE_SALT_BUD.get().getRegistryName().getPath(),
                blockTexture(Salt.Blocks.LARGE_SALT_BUD.get())));
        directionalBlock(Salt.Blocks.MEDIUM_SALT_BUD.get(), models().cross(Salt.Blocks.MEDIUM_SALT_BUD.get().getRegistryName().getPath(),
                blockTexture(Salt.Blocks.MEDIUM_SALT_BUD.get())));
        directionalBlock(Salt.Blocks.SMALL_SALT_BUD.get(), models().cross(Salt.Blocks.SMALL_SALT_BUD.get().getRegistryName().getPath(),
                blockTexture(Salt.Blocks.SMALL_SALT_BUD.get())));

        getVariantBuilder(Salt.Blocks.SALT_CAULDRON.get())
                .partialState().with(SaltCauldronBlock.LEVEL, 1)
                .modelForState().modelFile(
                        models().getExistingFile(modLoc("block/salt_cauldron_level_1"))).addModel()
                .partialState().with(SaltCauldronBlock.LEVEL, 2)
                .modelForState().modelFile(
                        models().getExistingFile(modLoc("block/salt_cauldron_level_2"))).addModel()
                .partialState().with(SaltCauldronBlock.LEVEL, 3)
                .modelForState().modelFile(
                        models().getExistingFile(modLoc("block/salt_cauldron_full"))).addModel();

        simpleBlock(Salt.Blocks.SALT_LAMP.get(), models().cubeBottomTop("salt_lamp",
                Salt.resource("block/salt_lamp_side"),
                Salt.resource("block/salt_lamp_bottom"),
                Salt.resource("block/salt_lamp_top")));
    }
}