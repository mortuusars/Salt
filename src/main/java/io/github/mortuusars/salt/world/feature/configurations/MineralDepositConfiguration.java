package io.github.mortuusars.salt.world.feature.configurations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

import java.util.List;

public class MineralDepositConfiguration implements FeatureConfiguration {
    public static final Codec<MineralDepositConfiguration> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                        Codec.list(TargetBlockState.CODEC)
                                .fieldOf("targets")
                                .forGetter(config -> config.targetStates),
                        BlockStateProvider.CODEC.fieldOf("outerStateProvider")
                                .forGetter(config -> config.outerStateProvider),
                        RuleTest.CODEC.fieldOf("outerTest")
                                .forGetter(target -> target.outerTest),
                        Codec.intRange(0, 64)
                                .fieldOf("size")
                                .forGetter(config -> config.size),
                        Codec.floatRange(0.0F, 1.0F)
                                .fieldOf("outerChance")
                                .forGetter(config -> config.outerChance))
                .apply(instance, MineralDepositConfiguration::new));

    public final List<MineralDepositConfiguration.TargetBlockState> targetStates;
    public final BlockStateProvider outerStateProvider;
    public RuleTest outerTest;
    public final int size;
    public final float outerChance;

    public MineralDepositConfiguration(List<MineralDepositConfiguration.TargetBlockState> targetBlockStates,
                                       BlockStateProvider outerStateProvider, RuleTest outerTest, int size, float outerChance) {
        this.targetStates = targetBlockStates;
        this.outerStateProvider = outerStateProvider;
        this.outerTest = outerTest;
        this.size = size;
        this.outerChance = outerChance;
    }

    public static MineralDepositConfiguration.TargetBlockState target(RuleTest pTarget, BlockState pState) {
        return new MineralDepositConfiguration.TargetBlockState(pTarget, pState);
    }

    public static class TargetBlockState {
        public static final Codec<MineralDepositConfiguration.TargetBlockState> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        RuleTest.CODEC.fieldOf("target")
                                .forGetter(target -> target.target),
                        BlockState.CODEC.fieldOf("state")
                                .forGetter(target -> target.state))
                        .apply(instance, TargetBlockState::new));
        public final RuleTest target;
        public final BlockState state;

        TargetBlockState(RuleTest target, BlockState state) {
            this.target = target;
            this.state = state;
        }
    }
}
