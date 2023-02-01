package io.github.mortuusars.salt.integration.jei;

import io.github.mortuusars.salt.Salt;
import io.github.mortuusars.salt.crafting.recipe.SaltingRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class SaltJeiPlugin implements IModPlugin {
    private static final ResourceLocation ID = Salt.resource("jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        registration.getCraftingCategory()
                .addCategoryExtension(SaltingRecipe.class, saltingRecipe -> new SaltingShapelessExtension(saltingRecipe));
    }
}
