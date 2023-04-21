package io.github.mortuusars.salt;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class Evaporation {
    public static void onWaterCauldronAnimateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextFloat() > 0.3f)
            return;

        int waterLevel = state.getValue(LayeredCauldronBlock.LEVEL);

        float x = pos.getX() + 0.25f + random.nextFloat() * 0.5f;
        float y = pos.getY() + 0.4f + random.nextFloat() * 0.1f + waterLevel * 0.18f;
        float z = pos.getZ() + 0.25f + random.nextFloat() * 0.5f;

        level.addParticle(ParticleTypes.BUBBLE_POP, x, y, z, 0f, 0.08f, 0f);
        level.addParticle(ParticleTypes.BUBBLE, x, y, z, 0f, 0.08f, 0f);

        level.playLocalSound(x, y, z,
                Salt.Sounds.BUBBLE_POP.get(), SoundSource.BLOCKS, 0.35f, 0.4f + random.nextFloat() * 0.5f, false);
    }

    public static void evaporateWaterAndFormSalt(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        level.setBlockAndUpdate(pos, Salt.Blocks.SALT_CAULDRON.get().withPropertiesOf(state));

        level.playSound(null, pos, Salt.Sounds.CAULDRON_EVAPORATE.get(),
                SoundSource.BLOCKS, 0.8f, level.getRandom().nextFloat() * 0.2f + 0.9f);

        for (int i = 0; i < 4; i++) {
            level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    pos.getX() + 0.3f + random.nextFloat() * 0.4f,
                    pos.getY() + 1f + random.nextFloat() * 0.2f,
                    pos.getZ() + 0.3f + random.nextFloat() * 0.4f,
                    1,0, 0.02f, 0, 0.02f);
        }
    }
}
