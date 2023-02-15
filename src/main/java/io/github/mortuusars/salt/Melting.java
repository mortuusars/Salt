package io.github.mortuusars.salt;

import io.github.mortuusars.salt.configuration.Configuration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class Melting {

    /**
     * Performs necessary checks and melts block at specified pos if all passes.
     * @return True if block was melted.
     */
    public static boolean maybeMeltByBlock(BlockPos pos, ServerLevel level) {
        if (Configuration.MELTING_BY_BLOCK_ENABLED.get() && level.getBlockState(pos).is(Salt.BlockTags.MELTABLES)
                && level.random.nextDouble() < Configuration.MELTING_BLOCK_CHANCE.get()) {
            Melting.meltBlock(pos, level);
            return true;
        }

        return false;
    }

    public static void meltBlock(BlockPos pos, ServerLevel level) {
        BlockState oldState = level.getBlockState(pos);
        BlockState newState = Configuration.MELTING_PLACES_WATER.get() ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState();

        level.setBlockAndUpdate(pos, newState);

        Vec3 center = Vec3.atCenterOf(pos);
        Random random = level.getRandom();

        level.playSound(null, center.x, center.y, center.z, Salt.Sounds.MELT.get(), SoundSource.BLOCKS, 0.9f,
                random.nextFloat() * 0.2f + 0.9f);

        level.playSound(null, center.x, center.y, center.z, Salt.Sounds.SALT_DISSOLVE.get(), SoundSource.BLOCKS, 0.9f,
                random.nextFloat() * 0.2f + 0.9f);

        BlockParticleOption particleType = new BlockParticleOption(ParticleTypes.BLOCK, oldState);
        for (int i = 0; i < 12; i++) {
            level.sendParticles(particleType, center.x + random.nextGaussian() * 0.45f,
                    center.y + random.nextGaussian() * 0.45f, center.z + random.nextGaussian() * 0.45f,
                    1, 0f, 0f, 0f, 0f);
        }
    }
}
