package io.github.mortuusars.salt.integration.jei;

import com.google.common.collect.ImmutableList;
import io.github.mortuusars.salt.Salt;
import io.github.mortuusars.salt.configuration.Configuration;
import io.github.mortuusars.salt.crafting.recipe.SaltingRecipe;
import io.github.mortuusars.salt.integration.jei.category.SaltEvaporationCategory;
import io.github.mortuusars.salt.integration.jei.resource.SaltEvaporationDummy;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

@JeiPlugin
public class SaltJeiPlugin implements IModPlugin {
    public static final RecipeType<SaltEvaporationDummy> SALT_EVAPORATION_RECIPE_TYPE =
            RecipeType.create(Salt.ID, "salt_evaporation", SaltEvaporationDummy.class);

    private static final ResourceLocation ID = Salt.resource("jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        if (isSaltEvaporationEnabled())
            registration.addRecipeCategories(new SaltEvaporationCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        if (isSaltEvaporationEnabled())
            registration.addRecipeCatalyst(new ItemStack(Items.CAULDRON), SALT_EVAPORATION_RECIPE_TYPE);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (isSaltEvaporationEnabled())
            registration.addRecipes(SALT_EVAPORATION_RECIPE_TYPE, ImmutableList.of(new SaltEvaporationDummy()));
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        registration.getCraftingCategory()
                .addCategoryExtension(SaltingRecipe.class, saltingRecipe -> new SaltingShapelessExtension(saltingRecipe));
    }

    private boolean isSaltEvaporationEnabled() {
        return Configuration.EVAPORATION_ENABLED.get()
                && Configuration.EVAPORATION_CHANCE.get() > 0.0d
                && !ForgeRegistries.BLOCKS.tags().getTag(Salt.BlockTags.HEATERS).isEmpty();
    }
}
