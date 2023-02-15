package io.github.mortuusars.salt.item;

import io.github.mortuusars.salt.Melting;
import io.github.mortuusars.salt.Salt;
import io.github.mortuusars.salt.configuration.Configuration;
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

        if (Configuration.MELTING_ITEM_ENABLED.get() && clickedBlockState.is(Salt.BlockTags.MELTABLES)) {
            if (level instanceof ServerLevel serverLevel) {
                Melting.meltBlock(clickedPos, serverLevel);
            }
            else {
                Vec3 center = Vec3.atCenterOf(clickedPos);
                Random random = level.getRandom();
                for (int i = 0; i < 6; i++) {
                    level.addParticle(ParticleTypes.SPIT,
                            center.x + random.nextGaussian() * 0.35f,
                            center.y + 0.35f + random.nextGaussian() * 0.35f,
                            center.z + random.nextGaussian() * 0.35f,
                            0f, 0f, 0f);
                }
            }

            Player player = context.getPlayer();
            if (!player.isCreative())
                context.getItemInHand().shrink(1);
            return InteractionResult.sidedSuccess(context.getLevel().isClientSide);
        }

        return super.useOn(context);
    }
}
