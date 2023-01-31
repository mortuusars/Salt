package io.github.mortuusars.salt;

import com.mojang.logging.LogUtils;
import io.github.mortuusars.salt.event.ClientEvents;
import io.github.mortuusars.salt.event.CommonEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mod(Salt.ID)
public class Salt
{
    public static final String ID = "salt";

    public Salt() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Registry.register(modEventBus);

        modEventBus.addListener(CommonEvents::onCommonSetup);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            modEventBus.addListener(ClientEvents::onRegisterModels);
            MinecraftForge.EVENT_BUS.addListener(ClientEvents::onItemTooltipEvent);
        });
        MinecraftForge.EVENT_BUS.addListener(CommonEvents::onItemUseFinish);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static ResourceLocation resource(String path) {
        return new ResourceLocation(ID, path);
    }

    public static boolean isForHoldingInArms() {
        try {
            return StackWalker.getInstance()
                    .walk(frames -> frames
                            .map(StackWalker.StackFrame::getMethodName)
                            .skip(7)
                            .findFirst()).orElse("").equals("renderHandsWithItems");
        }
        catch (Exception e) {
            LogUtils.getLogger().error(e.toString());
            return false;
        }
    }
}
