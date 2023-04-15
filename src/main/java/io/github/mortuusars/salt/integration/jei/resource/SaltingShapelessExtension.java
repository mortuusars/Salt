package io.github.mortuusars.salt.integration.jei.resource;

import io.github.mortuusars.salt.Salting;
import io.github.mortuusars.salt.crafting.recipe.SaltingRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public record SaltingShapelessExtension(SaltingRecipe saltingRecipe) implements ICraftingCategoryExtension {
    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull ICraftingGridHelper craftingGridHelper, @NotNull IFocusGroup focuses) {
        List<ItemStack> canBeSaltedItems = Arrays.stream(saltingRecipe.getFoodIngredient().getItems()).toList();

        if (canBeSaltedItems.size() == 1 && canBeSaltedItems.get(0).is(Items.BARRIER))
            return;

        List<List<ItemStack>> ingredientItems = saltingRecipe.getIngredients().stream()
                .map(i -> List.of(i.getItems()))
                .toList();

        List<List<ItemStack>> inputs = new ArrayList<>();
        inputs.add(canBeSaltedItems);
        inputs.addAll(ingredientItems);

        List<IRecipeSlotBuilder> inputSlots = craftingGridHelper.createAndSetInputs(builder, VanillaTypes.ITEM_STACK, inputs, 0, 0);

        IRecipeSlotBuilder outputSlots = craftingGridHelper.createAndSetOutputs(builder, VanillaTypes.ITEM_STACK, canBeSaltedItems.stream()
                .map(i -> Salting.setSalted(i.copy()))
                .collect(Collectors.toList()));

        builder.createFocusLink(inputSlots.get(0), outputSlots);
    }
}
