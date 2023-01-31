package io.github.mortuusars.salt.mixin;

import com.mojang.logging.LogUtils;
import io.github.mortuusars.salt.LayeredBakedModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;

import java.util.List;
import java.util.Optional;

public class SaltedModelGetter {
//    public static BakedModel getModel() {
//        Optional<String> methodName = StackWalker.getInstance()
//                .walk(frames ->
//                        frames.map(StackWalker.StackFrame::getMethodName).skip(5).findFirst());
//
//        LogUtils.getLogger().info(methodName.get());
//
//        BakedModel cachedModel = LayeredBakedModel.Cache.get(getIndex(stack.getItem()));
//        if (cachedModel != null)
//            cir.setReturnValue(cachedModel);
//        else {
//            BakedModel originalModel = this.getItemModel(stack.getItem());
//            if (originalModel != null) {
//                BakedModel saltOverlayModel = Minecraft.getInstance().getModelManager().getModel(SALT_OVERLAY);
//                LayeredBakedModel layeredModel = new LayeredBakedModel(List.of(originalModel, saltOverlayModel));
//                LayeredBakedModel.Cache.add(getIndex(stack.getItem()), layeredModel);
//                cir.setReturnValue(layeredModel);
//            }
//        }
//    }
}
