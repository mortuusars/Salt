package io.github.mortuusars.salt.mixin;

import io.github.mortuusars.salt.Evaporation;
import io.github.mortuusars.salt.Salt;
import io.github.mortuusars.salt.configuration.Configuration;
import io.github.mortuusars.salt.helper.CallStackHelper;
import io.github.mortuusars.salt.helper.Heater;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
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

    //TODO: Entity inside hurt

    /**
     * Handles converting water cauldron into a salt cauldron.
     * By default, 'tick' is called only when dripstone is filling the cauldron -
     * We set LayeredCauldronBlock#isRandomlyTicking to true, so it is called also on random tick.
     *
     * Another way to do this can probably be injecting in 'LayeredCauldronBlock#randomTick' and stopping it from proceeding to 'tick'.
     */
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true, require = 1)
    private void tick(BlockState state, ServerLevel level, BlockPos pos, Random random, CallbackInfo ci) {
        if (!Configuration.EVAPORATION_ENABLED.get())
            return;

        boolean hasHeatSourceBelow = Heater.isHeatSource(level.getBlockState(pos.below()));

        if (state.is(Blocks.CAULDRON) && hasHeatSourceBelow) {
            BlockPos stalactitePos = PointedDripstoneBlock.findStalactiteTipAboveCauldron(level, pos);
            if (stalactitePos != null) {
                Fluid fluid = PointedDripstoneBlock.getCauldronFillFluidType(level, stalactitePos);
                if (fluid != Fluids.LAVA) {
                    // Fluid drop evaporates
                    Vec3 center = Vec3.atCenterOf(pos);
                    level.playSound(null, center.x, center.y, center.z, Salt.Sounds.CAULDRON_EVAPORATE.get(),
                            SoundSource.BLOCKS, 0.6f, level.getRandom().nextFloat() * 0.2f + 1f);
                    level.addParticle(ParticleTypes.CLOUD, center.x, center.y + 0.2f, center.z, 0f, 0.02f, 0f);
                    ci.cancel();
                    return;
                }
            }
        }

        // Because of the 'isRandomlyTicking', this method gets called more often -
        // which causes cauldron to fill up quicker than intended.
        // But: callers of this method are different. Dripstone is calling from the 'tick' method.
        // We are interested only in calls from 'randomTick'.
        if (state.is(Blocks.WATER_CAULDRON)
                && state.getValue(LayeredCauldronBlock.LEVEL) != 0
                && hasHeatSourceBelow
                && CallStackHelper.isCalledFrom(CallStackHelper.RANDOM_TICK)
                && level.getRandom().nextDouble() < Configuration.EVAPORATION_CHANCE.get()) {
            Evaporation.evaporateWaterAndFormSalt(state, level, pos, random);
            ci.cancel();
        }
    }
}
