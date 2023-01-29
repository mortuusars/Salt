package io.github.mortuusars.salt.block;

import io.github.mortuusars.salt.Registry;
import io.github.mortuusars.salt.helper.Heater;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;

public class SaltCauldronBlock extends LayeredCauldronBlock {

    private static final Map<Item, CauldronInteraction> interactions;

    static {
        interactions = Util.make(new Object2ObjectOpenHashMap<>(),
                (map) -> map.defaultReturnValue((blockState, level, blockPos, player, hand, stack) ->
                        InteractionResult.PASS));

    }

    private Predicate<Biome.Precipitation> fillPredicate;

    public SaltCauldronBlock(Predicate<Biome.Precipitation> fillPredicate) {
        super(BlockBehaviour.Properties.copy(Blocks.CAULDRON), fillPredicate, CauldronInteraction.EMPTY);
        this.fillPredicate = fillPredicate;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (player.getItemInHand(hand).isEmpty()) {
            if (!level.isClientSide) {
                Vec3 p = Vec3.atCenterOf(pos);
                Containers.dropItemStack(level, p.x, p.y + 0.2d, p.z, new ItemStack(Registry.Items.SALT.get()));

                level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
                level.playSound(null, pos, SoundEvents.DRIPSTONE_BLOCK_HIT, SoundSource.BLOCKS, 1.0F, 1.0F);
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return super.use(state, level, pos, player, hand, blockHitResult);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (Heater.isHeatSource(level.getBlockState(pos.below()))) {
            if (!entity.fireImmune() && entity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)entity)) {
                entity.hurt(DamageSource.IN_FIRE, 1f);
            }
        }
    }

    @Override
    public void handlePrecipitation(BlockState state, Level level, BlockPos pos, Biome.Precipitation precipitation) {
        if (CauldronBlock.shouldHandlePrecipitation(level, precipitation) && Heater.isHeatSource(level.getBlockState(pos.below())) && this.fillPredicate.test(precipitation)) {
            if (precipitation == Biome.Precipitation.RAIN) {
                level.setBlockAndUpdate(pos, Blocks.WATER_CAULDRON.defaultBlockState());
                level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
            } else if (precipitation == Biome.Precipitation.SNOW) {
                level.setBlockAndUpdate(pos, Blocks.POWDER_SNOW_CAULDRON.defaultBlockState());
                level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
            }
        }
    }

    @Override
    protected boolean canReceiveStalactiteDrip(Fluid fluid) {
        return true;
    }

    @Override
    protected void receiveStalactiteDrip(BlockState state, Level level, BlockPos pos, Fluid fluid) {
        if (fluid == Fluids.LAVA) {
            level.setBlockAndUpdate(pos, Blocks.LAVA_CAULDRON.defaultBlockState());
            level.levelEvent(LevelEvent.SOUND_DRIP_LAVA_INTO_CAULDRON, pos, 0);
            level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
        }
        else if (Heater.isHeatSource(level.getBlockState(pos.below()))) {
            // Fluid drop evaporates
            level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH,
                    SoundSource.BLOCKS, 0.5f, level.getRandom().nextFloat() * 0.2f + 0.9f);
        }
        else if (fluid == Fluids.WATER) {
            level.setBlockAndUpdate(pos, Blocks.WATER_CAULDRON.defaultBlockState());
            level.levelEvent(LevelEvent.SOUND_DRIP_WATER_INTO_CAULDRON, pos, 0);
            level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
        }
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRandom) {
        super.tick(pState, pLevel, pPos, pRandom);
    }
}
