package io.github.mortuusars.salt.event;

import io.github.mortuusars.salt.Salting;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;


public class ClientEvents {

    public static void onItemTooltipEvent(ItemTooltipEvent event) {
        if (Salting.isSalted(event.getItemStack())) {
            event.getToolTip()
                    .add(new TranslatableComponent("salt.gui.tooltip.salted").withStyle(Style.EMPTY.withColor(0xF0D8D5)));
        }
    }
}
