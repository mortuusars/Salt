package io.github.mortuusars.salt.crafting.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import io.github.mortuusars.salt.Salt;
import io.github.mortuusars.salt.Salting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class SaltingRecipe extends CustomRecipe {
    private static final Ingredient CAN_BE_SALTED = Ingredient.of(Salt.ItemTags.CAN_BE_SALTED);

    private final String group;
    private NonNullList<Ingredient> ingredients;

    public SaltingRecipe(ResourceLocation id, String group, NonNullList<Ingredient> ingredients) {
        super(id);
        this.group = group;
        this.ingredients = ingredients;
    }

    @Override
    public boolean matches(CraftingContainer craftingContainer, Level level) {
        boolean hasFoodInput = false;
        NonNullList<Boolean> matches = NonNullList.withSize(this.ingredients.size(), false);
        int itemsCount = 0;

        for (int slotIndex = 0; slotIndex < craftingContainer.getContainerSize(); slotIndex++) {
            ItemStack stackInSlot = craftingContainer.getItem(slotIndex);
            if (stackInSlot.isEmpty())
                continue;

            itemsCount++;

            if (CAN_BE_SALTED.test(stackInSlot) && !hasFoodInput && !Salting.isSalted(stackInSlot))
                hasFoodInput = true;

            for (int ingredientIndex = 0; ingredientIndex < this.ingredients.size(); ingredientIndex++) {
                if (this.ingredients.get(ingredientIndex).test(stackInSlot) && !matches.get(ingredientIndex))
                    matches.set(ingredientIndex, true);
            }
        }

        return hasFoodInput && matches.stream().allMatch(v -> v == true) && itemsCount == this.ingredients.size() + 1;

//        boolean hasSalt = false;
//        boolean hasSaltedIngredient = false;
//
//        for (int index = 0; index < craftingContainer.getContainerSize(); index++) {
//            ItemStack itemStack = craftingContainer.getItem(index);
//
//            if (itemStack.isEmpty())
//                continue;
//
//            if (SALT.test(itemStack)) {
//                if (hasSalt)
//                    return false;
//
//                hasSalt = true;
//            }
//            else if (CAN_BE_SALTED.test(itemStack)) {
//                if (hasSaltedIngredient || Salting.isSalted(itemStack))
//                    return false;
//
//                hasSaltedIngredient = true;
//            }
//        }
//
//        return hasSalt && hasSaltedIngredient;
    }

    @Override
    public ItemStack assemble(CraftingContainer craftingContainer) {
        for (int index = 0; index < craftingContainer.getContainerSize(); index++) {
            ItemStack itemStack = craftingContainer.getItem(index);

            if (CAN_BE_SALTED.test(itemStack)) {
                ItemStack resultStack = itemStack.copy();
                resultStack.setCount(1);

                return Salting.setSalted(resultStack);
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
        return Salt.RecipeSerializers.SALTING.get();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    public Ingredient getFoodIngredient() {
        return CAN_BE_SALTED;
    }

    public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<SaltingRecipe> {
        private static final ResourceLocation NAME = Salt.resource("salting");
        public SaltingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            String group = GsonHelper.getAsString(json, "group", "");
            return new SaltingRecipe(recipeId, group, getIngredients(json));
        }

        private NonNullList<Ingredient> getIngredients(JsonObject json) {
            JsonArray jsonArray = GsonHelper.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> ingredients = NonNullList.create();

            for(int i = 0; i < jsonArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(jsonArray.get(i));
                if (net.minecraftforge.common.ForgeConfig.SERVER.skipEmptyShapelessCheck.get() || !ingredient.isEmpty())
                    ingredients.add(ingredient);
            }

            if (ingredients.isEmpty())
                throw new JsonParseException("No ingredients for salting recipe");
            else if (ingredients.size() > 3 * 3)
                throw new JsonParseException("Too many ingredients for salting recipe. The maximum is 9");
            return ingredients;
        }

        public SaltingRecipe fromNetwork(ResourceLocation recipeID, FriendlyByteBuf buffer) {
            String group = buffer.readUtf();
            int ingredientsCount = buffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(ingredientsCount, Ingredient.EMPTY);

            for(int i = 0; i < ingredients.size(); ++i) {
                ingredients.set(i, Ingredient.fromNetwork(buffer));
            }

            return new SaltingRecipe(recipeID, group, ingredients);
        }

        public void toNetwork(FriendlyByteBuf buffer, SaltingRecipe recipe) {
            buffer.writeUtf(recipe.group);
            buffer.writeVarInt(recipe.ingredients.size());

            for(Ingredient ingredient : recipe.ingredients) {
                ingredient.toNetwork(buffer);
            }
        }
    }
}
