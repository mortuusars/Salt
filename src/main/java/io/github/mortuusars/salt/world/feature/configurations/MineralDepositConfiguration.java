package io.github.mortuusars.salt.world.feature.configurations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

import java.util.List;

public class MineralDepositConfiguration implements FeatureConfiguration {
    public static final Codec<MineralDepositConfiguration> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(
                        Codec.list(DepositBlockStateInfo.CODEC)
                                .fieldOf("mainStateInfos")
                                .forGetter(config -> config.mainStateInfos),
                        DepositBlockStateInfo.CODEC
                                .fieldOf("clusterStateInfo")
                                .forGetter(config -> config.clusterStateInfo),
                        Codec.intRange(0, 64)
                                .fieldOf("size")
                                .forGetter(config -> config.size),
                        Codec.floatRange(0.0F, 1.0F)
                                .fieldOf("outerChance")
                                .forGetter(config -> config.clusterChance))
                .apply(instance, MineralDepositConfiguration::new));

    public final List<DepositBlockStateInfo> mainStateInfos;
    public final DepositBlockStateInfo clusterStateInfo;
    public final int size;
    public final float clusterChance;

    public MineralDepositConfiguration(List<DepositBlockStateInfo> mainStateInfos, DepositBlockStateInfo clusterStateInfo,
                                       int size, float outerChance) {
        this.mainStateInfos = mainStateInfos;
        this.clusterStateInfo = clusterStateInfo;
        this.size = size;
        this.clusterChance = outerChance;
    }

    public static DepositBlockStateInfo blockStateInfo(BlockStateProvider blockStateProvider, RuleTest ruleTest) {
        return new DepositBlockStateInfo(blockStateProvider, ruleTest);
    }

    public static class DepositBlockStateInfo {
        public static final Codec<DepositBlockStateInfo> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        BlockStateProvider.CODEC.fieldOf("blockStateProvider")
                                .forGetter(target -> target.blockStateProvider),
                        RuleTest.CODEC.fieldOf("ruleTest")
                                .forGetter(target -> target.ruleTest))
                        .apply(instance, DepositBlockStateInfo::new));

        public final BlockStateProvider blockStateProvider;
        public final RuleTest ruleTest;

        DepositBlockStateInfo(BlockStateProvider blockStateProvider, RuleTest ruleTest) {
            this.ruleTest = ruleTest;
            this.blockStateProvider = blockStateProvider;
        }
    }
}
