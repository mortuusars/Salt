package io.github.mortuusars.salt.event;

import io.github.mortuusars.salt.Salt;
import io.github.mortuusars.salt.Salting;
import io.github.mortuusars.salt.client.LangKeys;
import io.github.mortuusars.salt.integration.AppleSkinHandler;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.List;

@Mod.EventBusSubscriber(modid = Salt.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvents {

    public static void onClientSetup(FMLClientSetupEvent ignoredEvent) {
        if (ModList.get().isLoaded("appleskin")) {
            MinecraftForge.EVENT_BUS.register(new AppleSkinHandler());
        }
    }

    @SubscribeEvent
    public static void registerModels(ModelEvent.RegisterAdditional event) {
        event.register(Salt.resource("item/salted_overlay"));
    }

    public static void onItemTooltipEvent(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        if (Salting.isSalted(itemStack)) {
            List<Component> toolTip = event.getToolTip();
            Salting.FoodValue additionalFoodValue = Salting.getAdditionalFoodValue(itemStack);
            toolTip.add(toolTip.size() >= 1 ? 1 : 0, SaltedTooltip.get(additionalFoodValue.nutrition(),
                    additionalFoodValue.saturationModifier(), Screen.hasShiftDown() && !ModList.get().isLoaded("appleskin")));
        }
    }

    public static class SaltedTooltip {
        public static final Style SALTED_STYLE = Style.EMPTY.withColor(0xF0D8D5);
        public static final Style SALTED_EXPANDED_PART_STYLE = Style.EMPTY.withColor(0xC7B7B5);

        @SuppressWarnings("SuperfluousFormat")
        public static MutableComponent get(int nutrition, float saturationModifier, boolean isExpanded) {
            MutableComponent base = Component.translatable(LangKeys.GUI_TOOLTIP_SALTED).withStyle(SALTED_STYLE);
            return isExpanded ? base.append(Component.translatable(LangKeys.GUI_TOOLTIP_SALTED_EXPANDED_PART,
                    nutrition > 0 ? "+" + nutrition : "-" + nutrition,
                    saturationModifier > 0 ? "+" + saturationModifier : "-" + saturationModifier)
                    .withStyle(SALTED_EXPANDED_PART_STYLE))
                    : base;
        }
    }
}
