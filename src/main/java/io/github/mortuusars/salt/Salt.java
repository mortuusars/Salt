package io.github.mortuusars.salt;

import io.github.mortuusars.salt.configuration.Configuration;
import io.github.mortuusars.salt.event.ClientEvents;
import io.github.mortuusars.salt.event.CommonEvents;
import net.minecraft.SharedConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.stream.Collectors;

@Mod(Salt.ID)
public class Salt
{
    public static final String ID = "salt";

    public Salt() {
        Configuration.init();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Registry.register(modEventBus);

        modEventBus.addListener(CommonEvents::onCommonSetup);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            modEventBus.addListener(ClientEvents::onRegisterModels);
            MinecraftForge.EVENT_BUS.addListener(ClientEvents::onItemTooltipEvent);
        });
        MinecraftForge.EVENT_BUS.addListener(CommonEvents::onItemUseFinish);

        if (SharedConstants.IS_RUNNING_IN_IDE) {
            MinecraftForge.EVENT_BUS.addListener(this::onRightClick);
        }

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void onRightClick(PlayerInteractEvent.RightClickItem event) {
        if (!SharedConstants.IS_RUNNING_IN_IDE || event.getEntity().getLevel().isClientSide || !(event.getEntity() instanceof Player player))
            return;

        if (event.getItemStack().is(Items.DEBUG_STICK)) {
//            Registry.Blocks.SALT_CAULDRON.get().defaultBlockState().getDestroySpeed()


//            ForgeRegistries.ITEMS.getValues()
//                    .stream()
//                    .filter(i -> i.isEdible())
//                    .forEach(i -> player.drop(new ItemStack(i), false));
        }
    }

    public static ResourceLocation resource(String path) {
        return new ResourceLocation(ID, path);
    }
}
