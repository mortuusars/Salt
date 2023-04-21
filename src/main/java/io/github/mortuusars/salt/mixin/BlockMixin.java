package io.github.mortuusars.salt.mixin;

import io.github.mortuusars.salt.Evaporation;
import io.github.mortuusars.salt.configuration.Configuration;
import io.github.mortuusars.salt.helper.Heater;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(method = "animateTick", at = @At("HEAD"))
    private void onAnimateTick(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        if (Configuration.EVAPORATION_ENABLED.get() && state.is(Blocks.WATER_CAULDRON) && Heater.isHeatSource(level.getBlockState(pos.below())))
            Evaporation.onWaterCauldronAnimateTick(state, level, pos, random);
    }
}
