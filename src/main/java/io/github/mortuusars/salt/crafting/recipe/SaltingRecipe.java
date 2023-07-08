package io.github.mortuusars.salt.crafting.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import io.github.mortuusars.salt.Salt;
import io.github.mortuusars.salt.Salting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class SaltingRecipe extends CustomRecipe {
    private static final Ingredient CAN_BE_SALTED = Ingredient.of(Salt.ItemTags.CAN_BE_SALTED);

    private final String group;
    private final NonNullList<Ingredient> ingredients;

    public SaltingRecipe(ResourceLocation id, String group, NonNullList<Ingredient> ingredients) {
        super(id, CraftingBookCategory.MISC);
        this.group = group;
        this.ingredients = ingredients;
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingContainer pContainer) {
        return NonNullList.withSize(pContainer.getContainerSize(), ItemStack.EMPTY);
    }

    @Override
    public boolean matches(CraftingContainer craftingContainer, @NotNull Level level) {
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

        return hasFoodInput && matches.stream().allMatch(match -> match) && itemsCount == this.ingredients.size() + 1;
    }

    @Override
    public @NotNull ItemStack assemble(CraftingContainer craftingContainer, @NotNull RegistryAccess registryAccess) {
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
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Salt.RecipeSerializers.SALTING.get();
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    public Ingredient getFoodIngredient() {
        return CAN_BE_SALTED;
    }

    public static class Serializer implements RecipeSerializer<SaltingRecipe> {
        public @NotNull SaltingRecipe fromJson(@NotNull ResourceLocation recipeId, @NotNull JsonObject json) {
            String group = GsonHelper.getAsString(json, "group", "");
            return new SaltingRecipe(recipeId, group, getIngredients(json));
        }

        private NonNullList<Ingredient> getIngredients(JsonObject json) {
            JsonArray jsonArray = GsonHelper.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> ingredients = NonNullList.create();

            for(int i = 0; i < jsonArray.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(jsonArray.get(i));
                if (!ingredient.isEmpty())
                    ingredients.add(ingredient);
            }

            if (ingredients.isEmpty())
                throw new JsonParseException("No ingredients for salting recipe");
            else if (ingredients.size() > 3 * 3)
                throw new JsonParseException("Too many ingredients for salting recipe. The maximum is 9");
            return ingredients;
        }

        public SaltingRecipe fromNetwork(@NotNull ResourceLocation recipeID, FriendlyByteBuf buffer) {
            String group = buffer.readUtf();
            int ingredientsCount = buffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(ingredientsCount, Ingredient.EMPTY);

            //noinspection Java8ListReplaceAll
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
