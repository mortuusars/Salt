package io.github.mortuusars.salt.event;

import io.github.mortuusars.salt.Salt;
import io.github.mortuusars.salt.Salting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import java.util.List;

public class ClientEvents {

    public static void onRegisterModels(ModelRegistryEvent ignoredEvent) {
        ForgeModelBakery.addSpecialModel(Salt.resource("item/salted_overlay"));
    }

    public static void onItemTooltipEvent(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        if (Salting.isSalted(itemStack)) {
            List<Component> toolTip = event.getToolTip();
            toolTip.add(toolTip.size() >= 1 ? 1 : 0, SaltedTooltip.get(Salting.getAdditionalNutrition(itemStack),
                    Salting.getAdditionalSaturationModifier(itemStack), Screen.hasShiftDown()));
        }
    }

    public static class SaltedTooltip {
        public static final Style SALTED_STYLE = Style.EMPTY.withColor(0xF0D8D5);
        public static final Style SALTED_EXPANDED_PART_STYLE = Style.EMPTY.withColor(0xC7B7B5);

        public static MutableComponent get(int nutrition, float saturationModifier, boolean isExpanded) {
            MutableComponent base = new TranslatableComponent("salt.gui.tooltip.salted").withStyle(SALTED_STYLE);
            return isExpanded ? base.append(new TranslatableComponent("salt.gui.tooltip.salted_expanded_part",
                    nutrition, saturationModifier).withStyle(SALTED_EXPANDED_PART_STYLE))
                    : base;
        }
    }
}
