package io.github.mortuusars.salt.block;

import io.github.mortuusars.salt.Salt;
import io.github.mortuusars.salt.configuration.Configuration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SandBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;

public class SaltSandBlock extends SandBlock implements ISaltBlock {
    private BlockState dissolvedState;

    public SaltSandBlock(BlockState dissolvedState, int dustColor, Properties properties) {
        super(dustColor, properties);
        this.dissolvedState = dissolvedState;
    }

    public SaltSandBlock(int dustColor, Properties properties) {
        super(dustColor, properties);
        this.dissolvedState = Blocks.AIR.defaultBlockState();
    }

    @Override
    public BlockState getDissolvedState(BlockState originalState, ServerLevel level, BlockPos pos, Fluid fluid) {
        return dissolvedState;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, Random random) {
        if (random.nextInt(2) == 0 && level.isRainingAt(pos.above())) {
            Direction direction = Direction.getRandom(random);
            if (direction != Direction.UP) {
                BlockPos blockpos = pos.relative(direction);
                BlockState blockstate = level.getBlockState(blockpos);
                if (!state.canOcclude() || !blockstate.isFaceSturdy(level, blockpos, direction.getOpposite())) {
                    double d0 = direction.getStepX() == 0 ? random.nextDouble() : 0.5D + (double)direction.getStepX() * 0.65D;
                    double d1 = direction.getStepY() == 0 ? random.nextDouble() : 0.5D + (double)direction.getStepY() * 0.65D;
                    double d2 = direction.getStepZ() == 0 ? random.nextDouble() : 0.5D + (double)direction.getStepZ() * 0.65D;
                    level.addParticle(ParticleTypes.SPIT, (double)pos.getX() + d0, (double)pos.getY() + d1, (double)pos.getZ() + d2, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        ArrayList<Direction> directions = Arrays.stream(Direction.values())
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(directions);

        for (Direction direction : directions) {
            BlockPos adjacentPos = pos.relative(direction);
            BlockState adjacentState = level.getBlockState(adjacentPos);

            if (Configuration.MELTING_ENABLED.get() && adjacentState.is(Salt.BlockTags.MELTED_BY_SALT)
                    && level.random.nextDouble() < Configuration.MELTING_CHANCE.get()) {
                meltBlock(state, pos, adjacentPos, level);
                break;
            }

            if (Configuration.DISSOLVING_ENABLED.get() && Direction.DOWN != direction
                    && adjacentState.is(Salt.BlockTags.DISSOLVES_SALT) && level.random.nextDouble() < Configuration.DISSOLVING_CHANCE.get()) {
                FluidState fluidState = adjacentState.getFluidState();
                dissolve(state, level, pos, fluidState.getType(), fluidState.isSource());
                return;
            }
        }

        if (Configuration.DISSOLVING_ENABLED.get() && Configuration.DISSOLVING_IN_RAIN.get() && level.isRainingAt(pos.above())
                && level.random.nextDouble() < Configuration.DISSOLVING_IN_RAIN_CHANCE.get()) {
            dissolve(state, level, pos, Fluids.EMPTY, false);
            return;
        }

        SaltBlock.maybeGrowCluster(state, pos, level);
    }
}
