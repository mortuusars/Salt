package io.github.mortuusars.salt;

import io.github.mortuusars.salt.configuration.Configuration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class Melting {
    private static class SaltItemDispenseBehavior extends DefaultDispenseItemBehavior {
        @Override
        protected ItemStack execute(BlockSource source, ItemStack stack) {
            if (!stack.is(Salt.ItemTags.SALT))
                return stack;

            Direction direction = source.getBlockState().getValue(DispenserBlock.FACING);
            ItemStack itemstack = stack.split(1);

            BlockPos targetPos = source.getPos().relative(direction);
            BlockState targetState = source.getLevel().getBlockState(targetPos);

            if (!tryMeltFromItem(targetState, targetPos, source.getLevel())) {
                Position position = DispenserBlock.getDispensePosition(source);
                spawnItem(source.getLevel(), itemstack, 6, direction, position);
            }

            return stack;
        }
    }

    public static final DispenseItemBehavior SALT_DISPENSER_BEHAVIOR = new SaltItemDispenseBehavior();

    public static boolean tryMeltFromItem(BlockState targetState, BlockPos targetPos, Level level) {
        if (Configuration.MELTING_ITEM_ENABLED.get() && targetState.is(Salt.BlockTags.MELTABLES)) {
            if (level instanceof ServerLevel serverLevel) {
                Melting.meltBlock(targetPos, serverLevel);
            }
            else {
                Vec3 center = Vec3.atCenterOf(targetPos);
                Random random = level.getRandom();
                for (int i = 0; i < 6; i++) {
                    level.addParticle(ParticleTypes.SPIT,
                            center.x + random.nextGaussian() * 0.35f,
                            center.y + 0.35f + random.nextGaussian() * 0.35f,
                            center.z + random.nextGaussian() * 0.35f,
                            0f, 0f, 0f);
                }
            }

            return true;
        }

        return false;
    }

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
