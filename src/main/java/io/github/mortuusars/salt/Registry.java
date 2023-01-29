package io.github.mortuusars.salt;

import io.github.mortuusars.salt.block.SaltBlock;
import io.github.mortuusars.salt.block.SaltCauldronBlock;
import io.github.mortuusars.salt.crafting.recipe.SaltingRecipe;
import io.github.mortuusars.salt.item.SaltItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Registry {
    public static class Blocks {
        private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Salt.ID);

        public static final RegistryObject<SaltBlock> ROCK_SALT = BLOCKS.register("rock_salt",
                () -> new SaltBlock());

        public static final RegistryObject<SaltCauldronBlock> SALT_CAULDRON = BLOCKS.register("salt_cauldron",
                () -> new SaltCauldronBlock(LayeredCauldronBlock.RAIN));
    }

    public static class Items {
        private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Salt.ID);

        public static final RegistryObject<Item> SALT = ITEMS.register("salt",
                () -> new SaltItem(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));

        public static final RegistryObject<Item> ROCK_SALT = ITEMS.register("rock_salt",
                () -> new BlockItem(Blocks.ROCK_SALT.get(), new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
    }

    public static class ItemTags {
        public static final TagKey<Item> SALT = net.minecraft.tags.ItemTags.create(new ResourceLocation("forge", "salts"));
        public static final TagKey<Item> CAN_BE_SALTED = net.minecraft.tags.ItemTags.create(Salt.resource("can_be_salted"));
    }

    public static class BlockTags {
        public static final TagKey<Block> HEATERS = net.minecraft.tags.BlockTags.create(Salt.resource("heaters"));
    }

    public static class RecipeSerializers {
        private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Salt.ID);

        public static final RegistryObject<RecipeSerializer<?>> SALTING = RECIPE_SERIALIZERS.register("salting", () -> new SimpleRecipeSerializer<>(SaltingRecipe::new));

    }

    public static void register(IEventBus modEventBus) {
        Blocks.BLOCKS.register(modEventBus);
        Items.ITEMS.register(modEventBus);
        RecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
    }
}
