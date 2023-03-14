package io.github.mortuusars.salt.world.feature.configurations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.mortuusars.salt.configuration.Configuration;
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
                                .forGetter(config -> config.clusterStateInfo))
                .apply(instance, MineralDepositConfiguration::new));

    public final List<DepositBlockStateInfo> mainStateInfos;
    public final DepositBlockStateInfo clusterStateInfo;

    public MineralDepositConfiguration(List<DepositBlockStateInfo> mainStateInfos, DepositBlockStateInfo clusterStateInfo) {
        this.mainStateInfos = mainStateInfos;
        this.clusterStateInfo = clusterStateInfo;
    }

    public static DepositBlockStateInfo blockStateInfo(BlockStateProvider blockStateProvider, RuleTest ruleTest) {
        return new DepositBlockStateInfo(blockStateProvider, ruleTest);
    }

    public int getSize() {
        return Configuration.ROCK_SALT_SIZE.get();
    }

    public float getClusterChance() {
        return Configuration.ROCK_SALT_CLUSTER_CHANCE.get().floatValue();
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
