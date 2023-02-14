package io.github.mortuusars.salt.item;

import io.github.mortuusars.salt.Salt;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public class SaltItem extends Item {
    public SaltItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        BlockState clickedBlockState = level.getBlockState(clickedPos);

        if (clickedBlockState.is(Salt.BlockTags.MELTED_BY_SALT)) {
            if (level instanceof ServerLevel serverLevel)
                meltBlock(clickedPos, serverLevel);

            Player player = context.getPlayer();
            if (!player.isCreative())
                context.getItemInHand().shrink(1);
            return InteractionResult.sidedSuccess(context.getLevel().isClientSide);
        }

        return super.useOn(context);
    }

    protected void meltBlock(BlockPos pos, ServerLevel serverLevel) {
        serverLevel.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());

        Vec3 center = Vec3.atCenterOf(pos);
        Random random = serverLevel.getRandom();

        serverLevel.playSound(null, center.x, center.y, center.z, Salt.Sounds.MELT.get(), SoundSource.BLOCKS, 0.9f,
                random.nextFloat() * 0.2f + 0.9f);
        serverLevel.playSound(null, center.x, center.y, center.z, Salt.Sounds.SALT_DISSOLVE.get(), SoundSource.BLOCKS, 0.9f,
                random.nextFloat() * 0.2f + 0.9f);

        for (int i = 0; i < 6; i++) {
            serverLevel.sendParticles(ParticleTypes.CLOUD, center.x + random.nextGaussian() * 0.35f,
                    center.y + 0.35f + random.nextGaussian() * 0.35f, center.z + random.nextGaussian() * 0.35f,
                    1, 0f, 0f, 0f, 0f);
        }
    }
}
