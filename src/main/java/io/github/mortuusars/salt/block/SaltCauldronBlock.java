package io.github.mortuusars.salt.block;

import io.github.mortuusars.salt.Salt;
import io.github.mortuusars.salt.helper.Heater;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
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
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class SaltCauldronBlock extends LayeredCauldronBlock {
    private final Predicate<Biome.Precipitation> fillPredicate;

    public SaltCauldronBlock(Predicate<Biome.Precipitation> fillPredicate, Map<Item, CauldronInteraction> interactions) {
        super(BlockBehaviour.Properties.copy(Blocks.CAULDRON), fillPredicate, interactions);
        this.fillPredicate = fillPredicate;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return new ItemStack(Items.CAULDRON);
    }

    @Override
    public void destroy(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockState state) {
        super.destroy(level, pos, state);
        if (level instanceof ServerLevel serverLevel)
            dropContents(serverLevel, state, pos);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult blockHitResult) {
        if (player.getItemInHand(hand).isEmpty()) {
            if (level instanceof ServerLevel serverLevel) {
                dropContents(serverLevel, state, pos);
                serverLevel.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
                serverLevel.playSound(null, pos, Salt.Sounds.SALT_CAULDRON_REMOVE_SALT.get(), SoundSource.BLOCKS, 0.8F, serverLevel.getRandom().nextFloat() * 0.2f + 0.9f);
                if (player instanceof ServerPlayer serverPlayer && !serverPlayer.isCreative())
                    Salt.Advancements.SALT_EVAPORATED.trigger(serverPlayer);
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return super.use(state, level, pos, player, hand, blockHitResult);
    }

    protected void dropContents(ServerLevel level, BlockState state, BlockPos pos) {
        ResourceLocation lootTablePath = Salt.resource("cauldron_evaporation/salt_" + getFullnessString(state));
        LootTable lootTable = level.getServer().getLootData().getLootTable(lootTablePath);

        LootParams lootParams = new LootParams.Builder(level)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                .create(LootContextParamSets.EMPTY);

        List<ItemStack> randomItems = lootTable.getRandomItems(lootParams);

        for (ItemStack itemStack : randomItems) {
            float x = pos.getX() + 0.4f + level.random.nextFloat() * 0.2f;
            float y = pos.getY() + 0.7f + level.random.nextFloat() * 0.2f;
            float z = pos.getZ() + 0.4f + level.random.nextFloat() * 0.2f;
            ItemEntity itementity = new ItemEntity(level, x, y, z, itemStack);
            itementity.setPickUpDelay(5);
            level.addFreshEntity(itementity);
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
    public void entityInside(@NotNull BlockState state, Level level, BlockPos pos, @NotNull Entity entity) {
        if (Heater.isHeatSource(level.getBlockState(pos.below()))) {
            if (!entity.fireImmune() && entity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)entity)) {
                entity.hurt(level.damageSources().onFire(), 1f);
            }
        }
    }

    @Override
    public void handlePrecipitation(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, Biome.@NotNull Precipitation precipitation) {
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
    protected boolean canReceiveStalactiteDrip(@NotNull Fluid fluid) {
        return true;
    }

    @Override
    protected void receiveStalactiteDrip(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Fluid fluid) {
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
