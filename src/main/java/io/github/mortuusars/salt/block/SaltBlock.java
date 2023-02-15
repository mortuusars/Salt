package io.github.mortuusars.salt.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

import java.util.Random;

public class SaltBlock extends Block implements ISaltBlock {
    private final BlockState dissolvedState;

    public SaltBlock(BlockState dissolvedState, Properties properties) {
        super(properties);
        this.dissolvedState = dissolvedState;
    }

    public SaltBlock(Properties properties) {
        super(properties);
        this.dissolvedState = Blocks.AIR.defaultBlockState();
    }

    @Override
    public BlockState getDissolvedState(BlockState originalState, ServerLevel level, BlockPos pos, Fluid fluid) {
        return dissolvedState;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, Random random) {
        onSaltAnimateTick(state, level, pos, random);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        if (!onSaltRandomTick(state, level, pos, random))
            return;
    }
}
