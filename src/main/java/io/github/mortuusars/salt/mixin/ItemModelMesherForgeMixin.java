package io.github.mortuusars.salt.mixin;

import io.github.mortuusars.salt.LayeredBakedModel;
import net.minecraftforge.client.ItemModelMesherForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemModelMesherForge.class)
public class ItemModelMesherForgeMixin {
    @Inject(method = "rebuildCache", at = @At("HEAD"))
    private void onRebuildCache(CallbackInfo ci) {
        LayeredBakedModel.Cache.clear();
    }
}
