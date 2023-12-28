package io.github.mortuusars.salt.integration.jei.category;

import io.github.mortuusars.salt.Salt;
import io.github.mortuusars.salt.client.LangKeys;
import io.github.mortuusars.salt.integration.jei.SaltJeiPlugin;
import io.github.mortuusars.salt.integration.jei.resource.SaltEvaporationDummy;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SaltEvaporationCategory implements IRecipeCategory<SaltEvaporationDummy> {
    public static final ResourceLocation UID = Salt.resource("salt_evaporation");
    private static final ResourceLocation TEXTURE = Salt.resource("textures/gui/jei/salt_evaporation.png");
    private final Component title;
    private final IDrawable background;
    private final IDrawable icon;

    private final List<Component> waterCauldronTooltip = List.of(Salt.translate("block.minecraft.water_cauldron"));
    private final List<Component> saltCauldronTooltip = List.of(Salt.translate("block.salt.salt_cauldron"));
    private final List<Component> heatSourceTooltip = List.of(
            Salt.translate(LangKeys.JEI_CATEGORY_SALT_EVAPORATION_HEAT_SOURCE_TOOLTIP),
            Salt.translate(LangKeys.JEI_CATEGORY_SALT_EVAPORATION_HEAT_SOURCE_TOOLTIP_2).withStyle(ChatFormatting.GRAY)
                    .append(Component.literal(Salt.BlockTags.HEATERS.location().toString()).withStyle(ChatFormatting.GOLD)));

    public SaltEvaporationCategory(IGuiHelper guiHelper) {
        title = Salt.translate(LangKeys.JEI_CATEGORY_SALT_EVAPORATION);
        background = guiHelper.createDrawable(TEXTURE, 0, 0, 168, 90);
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Salt.Items.SALT.get()));
    }

    @Override
    public @NotNull List<Component> getTooltipStrings(@NotNull SaltEvaporationDummy recipe, @NotNull IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if ( (mouseX >= 15 && mouseX < 60) && (mouseY >= 8 && mouseY < 50))
            return waterCauldronTooltip;
        else if ( (mouseX >= 15 && mouseX < 60) && (mouseY >= 50 && mouseY < 85))
            return heatSourceTooltip;
        else if ( (mouseX >= 106 && mouseX < 153) && (mouseY >= 31 && mouseY < 76))
            return saltCauldronTooltip;

        return Collections.emptyList();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, @NotNull SaltEvaporationDummy recipe, @NotNull IFocusGroup focuses) {
        Item salt = Objects.requireNonNull(ForgeRegistries.ITEMS.tags()).getTag(Salt.ItemTags.FORGE_SALTS).stream().findFirst()
                .orElse(Salt.Items.SALT.get());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 122, 14)
                .addItemStack(new ItemStack(salt));
    }

    @Override
    public @NotNull RecipeType<SaltEvaporationDummy> getRecipeType() {
        return SaltJeiPlugin.SALT_EVAPORATION_RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return title;
    }
    @Override
    public @NotNull IDrawable getBackground() {
        return background;
    }
    @Override
    public @NotNull IDrawable getIcon() {
        return icon;
    }
}
