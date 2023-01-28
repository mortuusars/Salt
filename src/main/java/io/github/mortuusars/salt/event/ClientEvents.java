package io.github.mortuusars.salt.event;

import io.github.mortuusars.salt.Salt;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class ClientEvents {

    public static void onItemTooltipEvent(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        if (itemStack.hasTag() && itemStack.getTag().contains(Salt.SALTED_KEY)) {
            event.getToolTip()
                    .add(new TranslatableComponent("salt.gui.tooltip.salted").withStyle(ChatFormatting.ITALIC));
        }
    }
}
