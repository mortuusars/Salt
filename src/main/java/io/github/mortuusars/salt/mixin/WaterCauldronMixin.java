package io.github.mortuusars.salt.mixin;

import com.mojang.logging.LogUtils;
import io.github.mortuusars.salt.Evaporation;
import io.github.mortuusars.salt.Salt;
import io.github.mortuusars.salt.configuration.Configuration;
import io.github.mortuusars.salt.helper.CallStackHelper;
import io.github.mortuusars.salt.helper.Heater;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(AbstractCauldronBlock.class)
public abstract class WaterCauldronMixin extends Block {

    public WaterCauldronMixin(Properties pProperties) {
        super(pProperties);
    }

    /**
     * BlockBehaviourMixin handles randomTick, so this method is only called by scheduling ticks.
     * Cancels filling cauldron with dripping liquid when heated.
     */
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (Configuration.EVAPORATION_ENABLED.get() && state.is(Blocks.CAULDRON) && Heater.isHeatSource(level.getBlockState(pos.below()))) {
            BlockPos stalactitePos = PointedDripstoneBlock.findStalactiteTipAboveCauldron(level, pos);
            if (stalactitePos == null)
                return;

            Fluid fluid = PointedDripstoneBlock.getCauldronFillFluidType(level, stalactitePos);
            if (fluid != Fluids.LAVA) {
                // Fluid drop evaporates
                Vec3 center = Vec3.atCenterOf(pos);
                level.playSound(null, center.x, center.y, center.z, Salt.Sounds.CAULDRON_EVAPORATE.get(),
                        SoundSource.BLOCKS, 0.6f, level.getRandom().nextFloat() * 0.2f + 1f);
                level.addParticle(ParticleTypes.CLOUD, center.x, center.y + 0.2f, center.z, 0f, 0.02f, 0f);
                ci.cancel();
            }
        }
    }
}
