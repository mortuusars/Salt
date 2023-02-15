package io.github.mortuusars.salt.block;

import io.github.mortuusars.salt.Dissolving;
import io.github.mortuusars.salt.Melting;
import io.github.mortuusars.salt.Salt;
import io.github.mortuusars.salt.configuration.Configuration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Random;

public class SaltClusterBlock extends Block implements ISaltBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    protected final VoxelShape northAabb;
    protected final VoxelShape southAabb;
    protected final VoxelShape eastAabb;
    protected final VoxelShape westAabb;
    protected final VoxelShape upAabb;
    protected final VoxelShape downAabb;

    public SaltClusterBlock(int size, int offset, Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.UP));
        this.upAabb = Block.box(offset, 0.0D, offset, (16 - offset), size, (16 - offset));
        this.downAabb = Block.box(offset, (16 - size), offset, (16 - offset), 16.0D, (16 - offset));
        this.northAabb = Block.box(offset, offset, (16 - size), (16 - offset), (16 - offset), 16.0D);
        this.southAabb = Block.box(offset, offset, 0.0D, (16 - offset), (16 - offset), size);
        this.eastAabb = Block.box(0.0D, offset, offset, size, (16 - offset), (16 - offset));
        this.westAabb = Block.box((16 - size), offset, offset, 16.0D, (16 - offset), (16 - offset));
    }

    @Override
    public void onProjectileHit(Level level, BlockState blockState, BlockHitResult blockHitResult, Projectile projectile) {
        BlockPos blockpos = blockHitResult.getBlockPos();
        if (!level.isClientSide && projectile.mayInteract(level, blockpos) && projectile instanceof ThrownTrident
                && projectile.getDeltaMovement().length() > 0.6D) {
            level.destroyBlock(blockpos, true);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide && !newState.getFluidState().is(Fluids.EMPTY)) {
            level.playSound(null, pos, Salt.Sounds.SALT_DISSOLVE.get(), SoundSource.BLOCKS, 0.8f,
                    level.getRandom().nextFloat() * 0.2f + 0.9f);
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, Random random) {
        onSaltAnimateTick(state, level, pos, random);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {

        // Cluster does not call ISaltBlock#onSaltRandomTick because functionality differs a little

        BlockPos basePos = pos.relative(state.getValue(FACING).getOpposite());
        if (Melting.maybeMeltByBlock(basePos, level))
            return; // Base block is melted - which means cluster cannot is destroyed too.

        if (Dissolving.maybeDissolve(Blocks.AIR.defaultBlockState(), pos, level.getBlockState(basePos), basePos, level))
            return;

        if (Dissolving.maybeDissolveInRain(getDissolvedState(state, level, pos, Fluids.WATER), level, pos))
            return;

        if (state.getValue(FACING) == Direction.UP) {
            BlockPos belowPos = pos.below();
            ISaltBlock.maybeGrowCluster(level.getBlockState(belowPos), belowPos, level);
        }
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        VoxelShape shape = switch (direction) {
            case DOWN -> this.downAabb;
            case UP -> this.upAabb;
            case NORTH -> this.northAabb;
            case SOUTH -> this.southAabb;
            case WEST -> this.westAabb;
            case EAST -> this.eastAabb;
        };
        return shape;
    }

    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockPos blockpos = pos.relative(direction.getOpposite());
        return level.getBlockState(blockpos).isFaceSturdy(level, blockpos, direction);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        return direction == state.getValue(FACING).getOpposite() && !state.canSurvive(pLevel, pCurrentPos) ?
                Blocks.AIR.defaultBlockState() :
                super.updateShape(state, direction, neighborState, pLevel, pCurrentPos, pNeighborPos);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getClickedFace());
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    @Override
    public @NotNull BlockState getDissolvedState(BlockState originalState, ServerLevel level, BlockPos pos, Fluid fluid) {
        return Blocks.AIR.defaultBlockState();
    }
}
