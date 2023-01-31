package io.github.mortuusars.salt.helper;

import com.mojang.logging.LogUtils;

import java.util.function.Predicate;

public class CallStackHelper {
    public static final Predicate<String> RANDOM_TICK = s -> s.equals("randomTick");
    public static final Predicate<String> ITEM_IN_HAND = s -> s.equals("renderArmWithItem") || s.equals("renderHandsWithItems") || s.equals("renderItemInHand");

    public static boolean isCalledFrom(Predicate<String> methodNamePredicate) {
        try {
            return StackWalker.getInstance()
                    .walk(f -> f
                            .filter(frameInfo -> methodNamePredicate.test(frameInfo.getMethodName()))
                            .findAny()).isPresent();
        }
        catch (Exception e) {
            LogUtils.getLogger().error(e.toString());
            return false;
        }
    }
}
