package io.github.mortuusars.salt.block;

import io.github.mortuusars.salt.Salt;
import io.github.mortuusars.salt.configuration.Configuration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public interface ISaltBlock {

    @NotNull BlockState getDissolvedState(BlockState originalState, ServerLevel level, BlockPos pos, Fluid fluid);

    default void dissolve(BlockState state, ServerLevel level, BlockPos pos, Fluid fluid, boolean isSource) {
        BlockState dissolvedState = getDissolvedState(state, level, pos, fluid);
        if (dissolvedState.isAir()) {
            if (isSource && Configuration.DISSOLVING_FLUID_SOURCE_CONVERSION.get())
                level.setBlockAndUpdate(pos, fluid.defaultFluidState().createLegacyBlock());
            else
                level.removeBlock(pos, false);
        }
        else
            level.setBlockAndUpdate(pos, dissolvedState);

        Vec3 center = Vec3.atCenterOf(pos);
        Random random = level.getRandom();

        level.playSound(null, center.x, center.y, center.z, Salt.Sounds.SALT_DISSOLVE.get(), SoundSource.BLOCKS, 1f, random.nextFloat() * 0.2f + 0.9f);

        for (int i = 0; i < 6; i++) {
            level.sendParticles(ParticleTypes.CLOUD, center.x + random.nextGaussian() * 0.35f,
                    center.y + random.nextGaussian() * 0.35f, center.z + random.nextGaussian() * 0.35f,
                    1, 0f, 0f, 0f, 0f);
        }
    }

    default void meltBlock(BlockState melterState, BlockPos melterPos, BlockPos meltedPos, ServerLevel level) {
        BlockState adjacentState = level.getBlockState(meltedPos);
        Vec3 center = Vec3.atCenterOf(meltedPos);
        Random random = level.random;

        level.removeBlock(meltedPos, false);

        level.playSound(null, center.x, center.y, center.z, Salt.Sounds.MELT.get(), SoundSource.BLOCKS, 0.9f,
                random.nextFloat() * 0.2f + 0.9f);

        level.playSound(null, center.x, center.y, center.z, Salt.Sounds.SALT_DISSOLVE.get(), SoundSource.BLOCKS, 0.9f,
                random.nextFloat() * 0.2f + 0.9f);

        BlockParticleOption particleType = new BlockParticleOption(ParticleTypes.BLOCK, adjacentState);
        for (int i = 0; i < 12; i++) {
            level.sendParticles(particleType, center.x + random.nextGaussian() * 0.45f,
                    center.y + random.nextGaussian() * 0.45f, center.z + random.nextGaussian() * 0.45f,
                    1, 0f, 0f, 0f, 0f);
        }
    }
}
