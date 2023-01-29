package io.github.mortuusars.salt.helper;

import io.github.mortuusars.salt.Registry;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class Heater {
    public static boolean canProduceHeat(BlockState blockState) {
        if (blockState.is(Registry.BlockTags.HEATERS)) {
            if (!blockState.hasProperty(BlockStateProperties.LIT) || blockState.getValue(BlockStateProperties.LIT))
                return true;
        }

        return false;
    }
}
