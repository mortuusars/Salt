package io.github.mortuusars.salt.helper;

import io.github.mortuusars.salt.Salt;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class Heater {
    public static boolean isHeatSource(BlockState blockState) {
        return blockState.is(Salt.BlockTags.HEATERS) &&
                (!blockState.hasProperty(BlockStateProperties.LIT) || blockState.getValue(BlockStateProperties.LIT));
    }
}
