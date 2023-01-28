package io.github.mortuusars.salt.crafting.recipe;

import io.github.mortuusars.salt.Registry;
import io.github.mortuusars.salt.Salt;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.stream.Collectors;

public class SaltingRecipe extends CustomRecipe {

    private static final Ingredient SALT = Ingredient.of(Registry.ItemTags.SALT);
    private static final Ingredient CAN_BE_SALTED = Ingredient.of(Registry.ItemTags.CAN_BE_SALTED);

    public SaltingRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingContainer craftingContainer, Level level) {
        boolean hasSalt = false;
        boolean hasSaltedIngredient = false;

        for (int index = 0; index < craftingContainer.getContainerSize(); index++) {
            ItemStack itemStack = craftingContainer.getItem(index);

            if (itemStack.isEmpty())
                continue;

            if (SALT.test(itemStack)) {
                if (hasSalt)
                    return false;

                hasSalt = true;
            }
            else if (CAN_BE_SALTED.test(itemStack)) {
                if (itemStack.hasTag() && itemStack.getTag().contains(Salt.SALTED_KEY))
                    return false; // Already salted

                if (hasSaltedIngredient)
                    return false;

                hasSaltedIngredient = true;
            }
        }

        return hasSalt && hasSaltedIngredient;
    }

    @Override
    public ItemStack assemble(CraftingContainer craftingContainer) {
        for (int index = 0; index < craftingContainer.getContainerSize(); index++) {
            ItemStack itemStack = craftingContainer.getItem(index);

            if (CAN_BE_SALTED.test(itemStack)) {
                ItemStack resultStack = itemStack.copy();
                resultStack.setCount(1);

                CompoundTag tag = resultStack.getOrCreateTag();
                tag.putBoolean(Salt.SALTED_KEY, true);

                return resultStack;
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 2 || height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Registry.RecipeSerializers.SALTING.get();
    }
}
