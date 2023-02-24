package io.github.mortuusars.salt.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

public class SaltLampBlock extends SaltBlock {
    private final BlockState dissolvedState;

    public SaltLampBlock(BlockState dissolvedState, Properties properties) {
        super(properties);
        this.dissolvedState = dissolvedState;
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return 15;
    }
}
