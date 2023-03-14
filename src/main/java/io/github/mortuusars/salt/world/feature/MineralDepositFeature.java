package io.github.mortuusars.salt.world.feature;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import io.github.mortuusars.salt.Salt;
import io.github.mortuusars.salt.configuration.Configuration;
import io.github.mortuusars.salt.world.feature.configurations.MineralDepositConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.BulkSectionAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.*;

public class MineralDepositFeature extends Feature<MineralDepositConfiguration> {
    public MineralDepositFeature(Codec<MineralDepositConfiguration> codec) {
        super(codec);
    }

    /**
     * Places the given feature at the given location.
     * During world generation, features are provided with a 3x3 region of chunks, centered on the chunk being generated,
     * that they can safely generate into.
     * @param context A context object with a reference to the level and the position the feature is being placed at
     */
    public boolean place(FeaturePlaceContext<MineralDepositConfiguration> context) {
        RandomSource random = context.random();
        BlockPos origin = context.origin();
        WorldGenLevel worldgenlevel = context.level();

        if (!worldgenlevel.getBiome(origin).is(Salt.BiomeTags.HAS_ROCK_SALT_DEPOSITS))
            return false;

        MineralDepositConfiguration configuration = context.config();

        float f = random.nextFloat() * (float)Math.PI;
        float f1 = configuration.getSize() / 8.0F;
        int i = Mth.ceil((configuration.getSize() / 16.0F * 2.0F + 1.0F) / 2.0F);
        double minX = (double)origin.getX() + Math.sin(f) * (double)f1;
        double maxX = (double)origin.getX() - Math.sin(f) * (double)f1;
        double minZ = (double)origin.getZ() + Math.cos(f) * (double)f1;
        double maxZ = (double)origin.getZ() - Math.cos(f) * (double)f1;
        double mixY = (origin.getY() + random.nextInt(3) - 2);
        double maxY = (origin.getY() + random.nextInt(3) - 2);
        int x = origin.getX() - Mth.ceil(f1) - i;
        int y = origin.getY() - 2 - i;
        int z = origin.getZ() - Mth.ceil(f1) - i;
        int width = 2 * (Mth.ceil(f1) + i);
        int height = 2 * (2 + i);

        for(int l1 = x; l1 <= x + width; ++l1) {
            for(int i2 = z; i2 <= z + width; ++i2) {
                if (y <= worldgenlevel.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, l1, i2)) {
                    return this.doPlace(worldgenlevel, random, configuration, minX, maxX, minZ, maxZ, mixY, maxY, x, y, z, width, height);
                }
            }
        }

        return false;
    }

    protected boolean doPlace(WorldGenLevel level, RandomSource random, MineralDepositConfiguration configuration, double pMinX, double pMaxX, double pMinZ, double pMaxZ, double pMinY, double pMaxY, int pX, int pY, int pZ, int pWidth, int pHeight) {
        int placedBlocks = 0;
        BitSet bitset = new BitSet(pWidth * pHeight * pWidth);
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int size = configuration.getSize();
        double[] adouble = new double[size * 4];

        for(int k = 0; k < size; ++k) {
            float f = (float)k / (float)size;
            double d0 = Mth.lerp(f, pMinX, pMaxX);
            double d1 = Mth.lerp(f, pMinY, pMaxY);
            double d2 = Mth.lerp(f, pMinZ, pMaxZ);
            double d3 = random.nextDouble() * (double)size / 16.0D;
            double d4 = ((double)(Mth.sin((float)Math.PI * f) + 1.0F) * d3 + 1.0D) / 2.0D;
            adouble[k * 4 + 0] = d0;
            adouble[k * 4 + 1] = d1;
            adouble[k * 4 + 2] = d2;
            adouble[k * 4 + 3] = d4;
        }

        for(int l3 = 0; l3 < size - 1; ++l3) {
            if (!(adouble[l3 * 4 + 3] <= 0.0D)) {
                for(int i4 = l3 + 1; i4 < size; ++i4) {
                    if (!(adouble[i4 * 4 + 3] <= 0.0D)) {
                        double d8 = adouble[l3 * 4 + 0] - adouble[i4 * 4 + 0];
                        double d10 = adouble[l3 * 4 + 1] - adouble[i4 * 4 + 1];
                        double d12 = adouble[l3 * 4 + 2] - adouble[i4 * 4 + 2];
                        double d14 = adouble[l3 * 4 + 3] - adouble[i4 * 4 + 3];
                        if (d14 * d14 > d8 * d8 + d10 * d10 + d12 * d12) {
                            if (d14 > 0.0D) {
                                adouble[i4 * 4 + 3] = -1.0D;
                            } else {
                                adouble[l3 * 4 + 3] = -1.0D;
                            }
                        }
                    }
                }
            }
        }

        BulkSectionAccess bulksectionaccess = new BulkSectionAccess(level);

        try {
            for(int j4 = 0; j4 < size; ++j4) {
                double d9 = adouble[j4 * 4 + 3];
                if (!(d9 < 0.0D)) {
                    double d11 = adouble[j4 * 4 + 0];
                    double d13 = adouble[j4 * 4 + 1];
                    double d15 = adouble[j4 * 4 + 2];
                    int k4 = Math.max(Mth.floor(d11 - d9), pX);
                    int l = Math.max(Mth.floor(d13 - d9), pY);
                    int i1 = Math.max(Mth.floor(d15 - d9), pZ);
                    int j1 = Math.max(Mth.floor(d11 + d9), k4);
                    int k1 = Math.max(Mth.floor(d13 + d9), l);
                    int l1 = Math.max(Mth.floor(d15 + d9), i1);


                    for(int absoluteX = k4; absoluteX <= j1; ++absoluteX) {
                        double d5 = ((double)absoluteX + 0.5D - d11) / d9;
                        if (d5 * d5 < 1.0D) {
                            for(int absoluteY = l; absoluteY <= k1; ++absoluteY) {
                                double d6 = ((double)absoluteY + 0.5D - d13) / d9;
                                if (d5 * d5 + d6 * d6 < 1.0D) {
                                    for(int absoluteZ = i1; absoluteZ <= l1; ++absoluteZ) {
                                        double d7 = ((double)absoluteZ + 0.5D - d15) / d9;
                                        if (d5 * d5 + d6 * d6 + d7 * d7 < 1.0D && !level.isOutsideBuildHeight(absoluteY)) {
                                            int l2 = absoluteX - pX + (absoluteY - pY) * pWidth + (absoluteZ - pZ) * pWidth * pHeight;
                                            if (!bitset.get(l2)) {
                                                bitset.set(l2);
                                                mutableBlockPos.set(absoluteX, absoluteY, absoluteZ);

                                                if (level.ensureCanWrite(mutableBlockPos))
                                                    if (placeBlock(level, random, configuration, mutableBlockPos, bulksectionaccess, absoluteX, absoluteY, absoluteZ))
                                                        placedBlocks++;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Throwable throwable1) {
            try {
                bulksectionaccess.close();
            } catch (Throwable throwable) {
                throwable1.addSuppressed(throwable);
            }

            throw throwable1;
        }

        bulksectionaccess.close();
        return placedBlocks > 0;
    }

    private boolean placeBlock(WorldGenLevel level, RandomSource random, MineralDepositConfiguration configuration, BlockPos.MutableBlockPos mutableBlockPos, BulkSectionAccess bulksectionaccess, int absoluteX, int absoluteY, int absoluteZ) {

        LevelChunkSection levelchunksection = bulksectionaccess.getSection(mutableBlockPos);
        if (levelchunksection == null)
            return false;

        int relativeX = SectionPos.sectionRelative(absoluteX);
        int relativeY = SectionPos.sectionRelative(absoluteY);
        int relativeZ = SectionPos.sectionRelative(absoluteZ);

        BlockState oldBlockstate = levelchunksection.getBlockState(relativeX, relativeY, relativeZ);

        boolean mainBlockPlaced = false;

        for (MineralDepositConfiguration.DepositBlockStateInfo mainStateInfo : configuration.mainStateInfos) {
            if (mainStateInfo.ruleTest.test(oldBlockstate, random)) {
                BlockState newBlockState = mainStateInfo.blockStateProvider.getState(random, mutableBlockPos);
                // Place ore:
                levelchunksection.setBlockState(relativeX, relativeY, relativeZ, newBlockState, false);
                mainBlockPlaced = true;
            }
        }

        if (!mainBlockPlaced)
            return false;

        // Place cluster:

        List<Direction> clusterDirections = getValidSidesForCluster(random, configuration, levelchunksection, relativeX, relativeY, relativeZ);

        try {
            for (int i = 0; i < clusterDirections.size(); i++) {
                Direction randomDirection = clusterDirections.get(random.nextInt(clusterDirections.size()));
                clusterDirections.remove(randomDirection);

                if (random.nextFloat() >= configuration.getClusterChance())
                    continue;

                int cX = relativeX + randomDirection.getStepX();
                int cY = relativeY + randomDirection.getStepY();
                int cZ = relativeZ + randomDirection.getStepZ();

                BlockState state = configuration.clusterStateInfo.blockStateProvider.getState(random, mutableBlockPos);
                if (state.hasProperty(BlockStateProperties.FACING))
                    state = state.setValue(BlockStateProperties.FACING, randomDirection);

                levelchunksection.setBlockState(cX, cY, cZ, state, false);
            }
        }
        catch (Exception e) {
            LogUtils.getLogger().error(e.toString());
        }

        return true;
    }

    protected List<Direction> getValidSidesForCluster(RandomSource random, MineralDepositConfiguration configuration, LevelChunkSection levelchunksection, int relativeX, int relativeY, int relativeZ) {
        List<Direction> airDirections = new ArrayList<>();

        for (Direction dir : Direction.values()) {
            int cX = relativeX + dir.getStepX();
            int cY = relativeY + dir.getStepY();
            int cZ = relativeZ + dir.getStepZ();

            // After stepping to the air block - pos can be outside the chunk and will cause errors:
            if ((cX < 0 || cX > 15) || (cY < 0 || cY > 15) || (cZ < 0 || cZ > 15))
                continue;

            BlockState blockStateAtPos = levelchunksection.getBlockState(cX, cY, cZ);
            if (configuration.clusterStateInfo.ruleTest.test(blockStateAtPos, random))
                airDirections.add(dir);
        }

        return airDirections;
    }
}
