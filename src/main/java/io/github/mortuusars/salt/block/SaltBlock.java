package io.github.mortuusars.salt.block;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import java.util.Random;

public class SaltBlock extends Block {
    public SaltBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {

//        boolean isClientSide = pLevel.isClientSide;
//
//        LogUtils.getLogger().info("Changed. Client = " + isClientSide);

        super.neighborChanged(pState, pLevel, pPos, pBlock, pFromPos, pIsMoving);
    }

    @Override
    public void onBlockStateChange(LevelReader level, BlockPos pos, BlockState oldState, BlockState newState) {

//        LogUtils.getLogger().info("State Changed");

        super.onBlockStateChange(level, pos, oldState, newState);
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, Random pRandom) {
        super.randomTick(pState, pLevel, pPos, pRandom);
    }
}
