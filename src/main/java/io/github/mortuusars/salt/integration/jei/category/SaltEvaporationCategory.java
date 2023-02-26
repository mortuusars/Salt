package io.github.mortuusars.salt.integration.jei.category;

import io.github.mortuusars.salt.Salt;
import io.github.mortuusars.salt.client.LangKeys;
import io.github.mortuusars.salt.integration.jei.resource.SaltEvaporationDummy;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.List;

public class SaltEvaporationCategory implements IRecipeCategory<SaltEvaporationDummy> {
    public static final ResourceLocation UID = Salt.resource("salt_evaporation");
    private static final ResourceLocation TEXTURE = Salt.resource("textures/gui/jei/salt_evaporation.png");
    private final Component title;
    private final IDrawable background;
    private final IDrawable icon;

    private final List<Component> waterCauldronTooltip = List.of(new TranslatableComponent("block.minecraft.water_cauldron"));
    private final List<Component> saltCauldronTooltip = List.of(new TranslatableComponent("block.salt.salt_cauldron"));
    private final List<Component> heatSourceTooltip = List.of(
            Salt.translate(LangKeys.JEI_CATEGORY_SALT_EVAPORATION_HEAT_SOURCE_TOOLTIP),
            Salt.translate(LangKeys.JEI_CATEGORY_SALT_EVAPORATION_HEAT_SOURCE_TOOLTIP_2).withStyle(ChatFormatting.GRAY)
                    .append(new TextComponent(Salt.BlockTags.HEATERS.location().toString()).withStyle(ChatFormatting.GOLD)));

    public SaltEvaporationCategory(IGuiHelper guiHelper) {
        title = Salt.translate(LangKeys.JEI_CATEGORY_SALT_EVAPORATION);
        background = guiHelper.createDrawable(TEXTURE, 0, 0, 168, 90);
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Salt.Items.SALT.get()));
    }

    @Override
    public List<Component> getTooltipStrings(SaltEvaporationDummy recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if ( (mouseX >= 15 && mouseX < 60) && (mouseY >= 8 && mouseY < 50))
            return waterCauldronTooltip;
        else if ( (mouseX >= 15 && mouseX < 60) && (mouseY >= 50 && mouseY < 85))
            return heatSourceTooltip;
        else if ( (mouseX >= 106 && mouseX < 153) && (mouseY >= 31 && mouseY < 76))
            return saltCauldronTooltip;

        return Collections.emptyList();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SaltEvaporationDummy recipe, IFocusGroup focuses) {
        Item salt = ForgeRegistries.ITEMS.tags().getTag(Salt.ItemTags.FORGE_SALTS).stream().findFirst()
                .orElse(Salt.Items.SALT.get());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 122, 14)
                .addItemStack(new ItemStack(salt));
    }

    @Override
    public Component getTitle() {
        return title;
    }
    @Override
    public IDrawable getBackground() {
        return background;
    }
    @Override
    public IDrawable getIcon() {
        return icon;
    }
    @SuppressWarnings("removal")
    @Override
    public ResourceLocation getUid() {
        return UID;
    }
    @SuppressWarnings("removal")
    @Override
    public Class<? extends SaltEvaporationDummy> getRecipeClass() {
        return SaltEvaporationDummy.class;
    }
}
