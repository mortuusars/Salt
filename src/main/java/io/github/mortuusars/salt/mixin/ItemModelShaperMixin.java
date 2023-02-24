package io.github.mortuusars.salt.mixin;

import io.github.mortuusars.salt.client.rendering.LayeredBakedModel;
import io.github.mortuusars.salt.Salt;
import io.github.mortuusars.salt.Salting;
import io.github.mortuusars.salt.configuration.Configuration;
import io.github.mortuusars.salt.helper.CallStackHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemModelShaper.class)
public abstract class ItemModelShaperMixin {

    private static final ResourceLocation SALT_OVERLAY = Salt.resource("item/salted_overlay");

    @Shadow
    protected static int getIndex(Item pItem) {
        return 0;
    }

    @Inject(method = "getItemModel(Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/client/resources/model/BakedModel;",
            at = @At("RETURN"), cancellable = true)
    private void onGetItemModel(ItemStack stack, CallbackInfoReturnable<BakedModel> cir) {
        if (!Configuration.SALTED_OVERLAY.get() || CallStackHelper.isCalledFrom(CallStackHelper.ITEM_IN_HAND) || !Salting.isSalted(stack))
            return;

        BakedModel cachedModel = LayeredBakedModel.Cache.get(getIndex(stack.getItem()));
        if (cachedModel != null)
            cir.setReturnValue(cachedModel);
        else {
            BakedModel originalModel = cir.getReturnValue();
            BakedModel saltOverlayModel = Minecraft.getInstance().getModelManager().getModel(SALT_OVERLAY);
            LayeredBakedModel layeredModel = new LayeredBakedModel(List.of(originalModel, saltOverlayModel));
            LayeredBakedModel.Cache.add(getIndex(stack.getItem()), layeredModel);
            cir.setReturnValue(layeredModel);
        }
    }
}
