package io.github.mortuusars.salt.integration.jei.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.EmptyModelData;

public class GuiBlockRenderer {
    public static void renderBlockInGui(BlockState state, PoseStack poseStack, int xOffset, int yOffset, int tintColor, int xRot, int yRot, int zRot, float scale) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();

//        float xScale = 2, yScale = 30, zScale = 30;

        Minecraft.getInstance().textureManager.getTexture(TextureAtlas.LOCATION_BLOCKS).setFilter(false, false);
        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        PoseStack stack = RenderSystem.getModelViewStack();
        stack.pushPose();
        stack.mulPoseMatrix(poseStack.last().pose());
//        stack.translate(-(scale / 2), (scale / 2), 0.0D);
//        stack.translate(xOffset, yOffset, 100.0F + itemRenderer.blitOffset);
//        stack.translate(0, 0, 100.0F + itemRenderer.blitOffset);
//        stack.scale(16, -16, 16);

//        stack.mulPose(Vector3f.XP.rotationDegrees(xRot));
//        stack.mulPose(Vector3f.YP.rotationDegrees(yRot));

        RenderSystem.applyModelViewMatrix();
        PoseStack renderPoseStack = new PoseStack();
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

//        renderPoseStack.translate(0, 1d, 0);




        BakedModel model = blockRenderer.getBlockModel(state);

        boolean usesBlockLight = !model.usesBlockLight();
        if (!usesBlockLight) {
            Lighting.setupForFlatItems();
        }

        float r = (float)(tintColor >> 16 & 255) / 255.0F;
        float g = (float)(tintColor >> 8 & 255) / 255.0F;
        float b = (float)(tintColor & 255) / 255.0F;

        blockRenderer.getModelRenderer()
            .renderModel(renderPoseStack.last(),
                bufferSource.getBuffer(ItemBlockRenderTypes.getRenderType(state, false)),
                state, model, r, g, b, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);

//        renderPoseStack.popPose();
//
//        renderPoseStack.translate(3d, -0.5d, 0);
//
//        blockRenderer.renderSingleBlock(Salt.Blocks.SALT_CAULDRON.get().defaultBlockState().setValue(SaltCauldronBlock.LEVEL, 3), renderPoseStack, bufferSource,
//                LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);

        bufferSource.endBatch();
        RenderSystem.enableDepthTest();

        if (!usesBlockLight) {
            Lighting.setupFor3DItems();
        }

        stack.popPose();
        RenderSystem.applyModelViewMatrix();
    }
}
