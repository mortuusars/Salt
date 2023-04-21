package io.github.mortuusars.salt.mixin;

import io.github.mortuusars.salt.Salt;
import io.github.mortuusars.salt.Salting;
import io.github.mortuusars.salt.client.rendering.LayeredBakedModel;
import io.github.mortuusars.salt.configuration.Configuration;
import io.github.mortuusars.salt.helper.CallStackHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    private static final ResourceLocation SALT_OVERLAY = Salt.resource("item/salted_overlay");

    /**
     * Combines item model with salted overlay model.
     * Does not add salted overlay if rendered in hand.
     */
    @Inject(method = "getModel", at = @At("RETURN"), cancellable = true)
    private void getModel(ItemStack stack, Level level, LivingEntity entity, int idk, CallbackInfoReturnable<BakedModel> cir) {
        if (Configuration.SALTED_OVERLAY_ENABLED.get() && Salting.isSalted(stack) && !CallStackHelper.isCalledFrom(CallStackHelper.ITEM_IN_HAND)) {
            BakedModel cachedModel = LayeredBakedModel.Cache.get(Item.getId(stack.getItem()));
            if (cachedModel != null)
                cir.setReturnValue(cachedModel);
            else {
                BakedModel originalModel = cir.getReturnValue();
                BakedModel saltOverlayModel = Minecraft.getInstance().getModelManager().getModel(SALT_OVERLAY);
                LayeredBakedModel layeredModel = new LayeredBakedModel(List.of(originalModel, saltOverlayModel));
                LayeredBakedModel.Cache.add(Item.getId(stack.getItem()), layeredModel);
                cir.setReturnValue(layeredModel);
            }
        }
    }
}
