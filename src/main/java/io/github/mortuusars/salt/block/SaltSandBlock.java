package io.github.mortuusars.salt.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SandBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"deprecation", "unused"})
public class SaltSandBlock extends SandBlock implements ISaltBlock {
    private final BlockState dissolvedState;

    public SaltSandBlock(BlockState dissolvedState, int dustColor, Properties properties) {
        super(dustColor, properties);
        this.dissolvedState = dissolvedState;
    }

    public SaltSandBlock(int dustColor, Properties properties) {
        super(dustColor, properties);
        this.dissolvedState = Blocks.AIR.defaultBlockState();
    }

    @Override
    public @NotNull BlockState getDissolvedState(BlockState originalState, ServerLevel level, BlockPos pos, Fluid fluid) {
        return dissolvedState;
    }

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        onSaltAnimateTick(state, level, pos, random);
    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (!onSaltRandomTick(state, level, pos, random))
            //noinspection UnnecessaryReturnStatement
            return;
    }
}
