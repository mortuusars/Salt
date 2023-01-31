package io.github.mortuusars.salt.client.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LayeredBakedModel implements BakedModel {
    private List<BakedModel> layers;

    public LayeredBakedModel(List<BakedModel> layers) {
        this.layers = layers;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
        return this.getQuads(state, side, rand, EmptyModelData.INSTANCE);
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull Random rand, @NotNull IModelData extraData) {
        List<BakedQuad> quads = new ArrayList<>();

        for (BakedModel layer : layers) {
            List<BakedQuad> layerQuads = layer.getQuads(state, side, rand, extraData);
            quads.addAll(layerQuads);
        }

        return quads;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return layers.get(0).useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return layers.get(0).isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return layers.get(0).usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return layers.get(0).isCustomRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return layers.get(0).getParticleIcon();
    }

    @Override
    public ItemOverrides getOverrides() {
        return layers.get(0).getOverrides();
    }

    @Override
    public ItemTransforms getTransforms() {
        return layers.get(0).getTransforms();
    }

    @Override
    public BakedModel handlePerspective(ItemTransforms.TransformType cameraTransformType, PoseStack poseStack) {
        return BakedModel.super.handlePerspective(cameraTransformType, poseStack);
    }

    public static class Cache {
        private static final Int2ObjectMap<BakedModel> cache = new Int2ObjectOpenHashMap<>(256);

        public static void add(int key, BakedModel model) {
            cache.put(key, model);
        }

        public static @Nullable BakedModel get(int key) {
            return cache.get(key);
        }

        public static void clear() {
            cache.clear();
        }
    }
}
