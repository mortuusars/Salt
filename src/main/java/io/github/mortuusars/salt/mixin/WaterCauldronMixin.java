package io.github.mortuusars.salt.mixin;

import com.mojang.logging.LogUtils;
import io.github.mortuusars.salt.Registry;
import io.github.mortuusars.salt.helper.Heater;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Mixin(AbstractCauldronBlock.class)
public abstract class WaterCauldronMixin extends Block {

    public WaterCauldronMixin(Properties pProperties) {
        super(pProperties);
    }

    /**
     * Handles converting water cauldron into a salt cauldron.
     * By default, 'tick' is called only when dripstone is filling the cauldron -
     * We set LayeredCauldronBlock#isRandomlyTicking to true, so it is called also on random tick.
     */
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tick(BlockState state, ServerLevel level, BlockPos pos, Random random, CallbackInfo ci) {

        if (!state.is(Blocks.WATER_CAULDRON) || state.getValue(LayeredCauldronBlock.LEVEL) == 0 || !Heater.canProduceHeat(level.getBlockState(pos.below())))
            return;

        // Because of the 'isRandomlyTicking', this method gets called more often -
        // and this causes cauldron to fill up quicker than intended.
        // But - callers of this method are different. Dripstone is calling from the 'tick' method.
        // We are interested only in calls from 'randomTick'.
        Optional<String> walk = StackWalker.getInstance()
                .walk(frames -> frames
                        .map(StackWalker.StackFrame::getMethodName)
                        .skip(2)
                        .findFirst());

        //TODO: Config chance of conversion

        if (walk.isPresent() && walk.get().equals("randomTick")) {
            BlockPos blockpos = PointedDripstoneBlock.findStalactiteTipAboveCauldron(level, pos);
            Vec3 p = Vec3.atCenterOf(pos);
            Containers.dropItemStack(level, p.x, p.y, p.z, new ItemStack(Registry.Items.SALT.get()));
            if (blockpos == null) {
                // TODO: Salt Cauldron
                level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
            }

            ci.cancel();
        }
    }
}
