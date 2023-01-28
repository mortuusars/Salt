package io.github.mortuusars.salt;

import io.github.mortuusars.salt.item.SaltItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Registry {
    public static class Items {
        private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Salt.ID);

        public static final RegistryObject<Item> SALT = ITEMS.register("salt",
                () -> new SaltItem(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
    }

    public static void register(IEventBus modEventBus) {
        Items.ITEMS.register(modEventBus);
    }
}
