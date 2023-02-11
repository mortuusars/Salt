package io.github.mortuusars.salt.event;

import io.github.mortuusars.salt.Salt;
import io.github.mortuusars.salt.Salting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.List;

public class ClientEvents {

    public static void onClientSetup(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(Salt.Blocks.SALT_CLUSTER.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(Salt.Blocks.LARGE_SALT_BUD.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(Salt.Blocks.MEDIUM_SALT_BUD.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(Salt.Blocks.SMALL_SALT_BUD.get(), RenderType.cutout());
    }

    public static void onRegisterModels(ModelRegistryEvent ignoredEvent) {
        ForgeModelBakery.addSpecialModel(Salt.resource("item/salted_overlay"));
    }

    public static void onItemTooltipEvent(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        if (Salting.isSalted(itemStack)) {
            List<Component> toolTip = event.getToolTip();
            Salting.FoodValue additionalFoodValue = Salting.getAdditionalFoodValue(itemStack);
            toolTip.add(toolTip.size() >= 1 ? 1 : 0, SaltedTooltip.get(additionalFoodValue.nutrition(),
                    additionalFoodValue.saturationModifier(), Screen.hasShiftDown()));
        }
    }

    public static class SaltedTooltip {
        public static final Style SALTED_STYLE = Style.EMPTY.withColor(0xF0D8D5);
        public static final Style SALTED_EXPANDED_PART_STYLE = Style.EMPTY.withColor(0xC7B7B5);

        @SuppressWarnings("SuperfluousFormat")
        public static MutableComponent get(int nutrition, float saturationModifier, boolean isExpanded) {
            MutableComponent base = new TranslatableComponent("salt.gui.tooltip.salted").withStyle(SALTED_STYLE);
            return isExpanded ? base.append(new TranslatableComponent("salt.gui.tooltip.salted_expanded_part",
                    nutrition > 0 ? "+" + nutrition : "-" + nutrition,
                    saturationModifier > 0 ? "+" + saturationModifier : "-" + saturationModifier)
                    .withStyle(SALTED_EXPANDED_PART_STYLE))
                    : base;
        }
    }
}
