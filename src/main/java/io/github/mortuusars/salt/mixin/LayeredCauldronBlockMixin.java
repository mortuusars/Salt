package io.github.mortuusars.salt.mixin;

import io.github.mortuusars.salt.configuration.Configuration;
import io.github.mortuusars.salt.helper.Heater;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayeredCauldronBlock.class)
public class LayeredCauldronBlockMixin {
    @Inject(method = "entityInside", at = @At("HEAD"))
    private void onEntityInside(BlockState state, Level level, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (Configuration.EVAPORATION_ENABLED.get() && state.is(Blocks.WATER_CAULDRON) && Heater.isHeatSource(level.getBlockState(pos.below()))) {
            if (!entity.fireImmune() && entity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity) entity)) {
                entity.hurt(level.damageSources().onFire(), 1f);
            }
        }
    }
}
