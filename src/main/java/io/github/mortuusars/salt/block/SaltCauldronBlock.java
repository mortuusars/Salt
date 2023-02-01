package io.github.mortuusars.salt.block;

import io.github.mortuusars.salt.Salt;
import io.github.mortuusars.salt.helper.Heater;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
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
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;

public class SaltCauldronBlock extends LayeredCauldronBlock {
    private Predicate<Biome.Precipitation> fillPredicate;

    public SaltCauldronBlock(Predicate<Biome.Precipitation> fillPredicate, Map<Item, CauldronInteraction> interactions) {
        super(BlockBehaviour.Properties.copy(Blocks.CAULDRON), fillPredicate, interactions);
        this.fillPredicate = fillPredicate;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return new ItemStack(Items.CAULDRON);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (player.getItemInHand(hand).isEmpty()) {
            if (!level.isClientSide) {
                dropContents(state, (ServerLevel)level, pos, player, hand, blockHitResult);
                level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
                level.playSound(null, pos, SoundEvents.DRIPSTONE_BLOCK_HIT, SoundSource.BLOCKS, 0.8F, level.getRandom().nextFloat() * 0.2f + 0.9f);
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return super.use(state, level, pos, player, hand, blockHitResult);
    }

    protected void dropContents(BlockState state, ServerLevel level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        ResourceLocation lootTablePath = Salt.resource("cauldron/salt_" + getFullnessString(state));
        LootTable lootTable = level.getServer().getLootTables().get(lootTablePath);

        LootContext.Builder lootContextBuilder = (new LootContext.Builder(level))
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos));

        List<ItemStack> randomItems = lootTable.getRandomItems(lootContextBuilder.create(LootContextParamSets.EMPTY));

        Vec3 center = Vec3.atCenterOf(pos);
        Random random = level.getRandom();

        for (ItemStack itemStack : randomItems) {
            Containers.dropItemStack(level, (random.nextGaussian() * 0.2d) + center.x,
                    (random.nextGaussian() * 0.2d) + center.y + 0.2d, (random.nextGaussian() * 0.2d) + center.z, itemStack);
        }
    }

    protected String getFullnessString(BlockState state) {
        return switch (state.getValue(LEVEL)) {
            case 1 -> "level_1";
            case 2 -> "level_2";
            default -> "full";
        };
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
}
