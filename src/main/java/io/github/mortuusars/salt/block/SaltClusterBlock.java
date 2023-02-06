package io.github.mortuusars.salt.block;

import io.github.mortuusars.salt.Salt;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
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
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Random;

public class SaltClusterBlock extends Block {
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
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!level.isClientSide && !newState.getFluidState().is(Fluids.EMPTY)) {
            level.playSound(null, pos, Salt.Sounds.SALT_DISSOLVE.get(), SoundSource.BLOCKS, 0.8f,
                    level.getRandom().nextFloat() * 0.2f + 0.9f);
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public OffsetType getOffsetType() {
        return BlockBehaviour.OffsetType.XZ;
    }

    @Override
    public float getMaxHorizontalOffset() {
        return 0.1f;
    }

    @Override
    public SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, @org.jetbrains.annotations.Nullable Entity entity) {
        return super.getSoundType(state, level, pos, entity);
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRandom) {
        super.randomTick(pState, pLevel, pPos, pRandom);
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
        Vec3 vec3 = state.getOffset(level, pos);
        return shape.move(vec3.x, 0, vec3.z);
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
}
