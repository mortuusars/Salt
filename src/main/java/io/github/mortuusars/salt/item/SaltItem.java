package io.github.mortuusars.salt.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class SaltItem extends Item {
    public SaltItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {



        BlockPos clickedPos = context.getClickedPos();
        Level level = context.getLevel();

        if (level.isClientSide)
            return super.useOn(context);

        BlockState clickedBlockState = level.getBlockState(clickedPos);
        if (clickedBlockState.is(Blocks.WATER_CAULDRON)) {
//            new EvaporatingCauldronEntity(level)
//            level.addFreshEntity()
        }

        return super.useOn(context);
    }
}
