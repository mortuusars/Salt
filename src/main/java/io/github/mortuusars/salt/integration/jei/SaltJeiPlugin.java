package io.github.mortuusars.salt.integration.jei;

import com.google.common.collect.ImmutableList;
import io.github.mortuusars.salt.Salt;
import io.github.mortuusars.salt.configuration.Configuration;
import io.github.mortuusars.salt.crafting.recipe.SaltingRecipe;
import io.github.mortuusars.salt.integration.jei.category.SaltCrystalGrowingCategory;
import io.github.mortuusars.salt.integration.jei.category.SaltEvaporationCategory;
import io.github.mortuusars.salt.integration.jei.resource.SaltCrystalGrowingDummy;
import io.github.mortuusars.salt.integration.jei.resource.SaltEvaporationDummy;
import io.github.mortuusars.salt.integration.jei.resource.SaltingShapelessExtension;
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
    public static final RecipeType<SaltCrystalGrowingDummy> SALT_CRYSTAL_GROWING_RECIPE_TYPE =
            RecipeType.create(Salt.ID, "salt_crystal_growing", SaltCrystalGrowingDummy.class);

    private static final ResourceLocation ID = Salt.resource("jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        if (isSaltEvaporationEnabled())
            registration.addRecipeCategories(new SaltEvaporationCategory(registration.getJeiHelpers().getGuiHelper()));

        if (isSaltCrystalGrowingEnabled())
            registration.addRecipeCategories(new SaltCrystalGrowingCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        if (isSaltEvaporationEnabled())
            registration.addRecipeCatalyst(new ItemStack(Items.CAULDRON), SALT_EVAPORATION_RECIPE_TYPE);

        if (isSaltCrystalGrowingEnabled())
            registration.addRecipeCatalyst(new ItemStack(Salt.Items.RAW_ROCK_SALT_BLOCK.get()), SALT_CRYSTAL_GROWING_RECIPE_TYPE);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (isSaltEvaporationEnabled())
            registration.addRecipes(SALT_EVAPORATION_RECIPE_TYPE, ImmutableList.of(new SaltEvaporationDummy()));

        if (isSaltCrystalGrowingEnabled())
            registration.addRecipes(SALT_CRYSTAL_GROWING_RECIPE_TYPE, ImmutableList.of(new SaltCrystalGrowingDummy()));
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        registration.getCraftingCategory()
                .addCategoryExtension(SaltingRecipe.class, SaltingShapelessExtension::new);
    }

    private boolean isSaltEvaporationEnabled() {
        return Configuration.JEI_SALT_EVAPORATION_ENABLED.get()
                && Configuration.EVAPORATION_ENABLED.get()
                && Configuration.EVAPORATION_CHANCE.get() > 0.0d
                && !ForgeRegistries.BLOCKS.tags().getTag(Salt.BlockTags.HEATERS).isEmpty();
    }

    private boolean isSaltCrystalGrowingEnabled() {
        return Configuration.JEI_SALT_CRYSTAL_GROWING_ENABLED.get()
                && Configuration.SALT_CLUSTER_GROWING_ENABLED.get()
                && Configuration.SALT_CLUSTER_GROWING_CHANCE.get() > 0.0d;
    }
}
