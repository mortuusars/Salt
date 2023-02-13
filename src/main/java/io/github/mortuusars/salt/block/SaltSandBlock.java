package io.github.mortuusars.salt.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SandBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

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

    //TODO: dissolve
}
