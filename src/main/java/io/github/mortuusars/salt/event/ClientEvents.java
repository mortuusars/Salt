package io.github.mortuusars.salt.event;

import io.github.mortuusars.salt.Salt;
import io.github.mortuusars.salt.Salting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import java.util.List;

public class ClientEvents {

    public static void onRegisterModels(ModelRegistryEvent ignoredEvent) {
        ForgeModelBakery.addSpecialModel(Salt.resource("item/salted_overlay"));
    }

    public static void onItemTooltipEvent(ItemTooltipEvent event) {
        if (Salting.isSalted(event.getItemStack())) {
            List<Component> toolTip = event.getToolTip();
            toolTip.add(toolTip.size() >= 1 ? 1 : 0, new TranslatableComponent("salt.gui.tooltip.salted").withStyle(Style.EMPTY.withColor(0xF0D8D5)));
        }
    }
}
