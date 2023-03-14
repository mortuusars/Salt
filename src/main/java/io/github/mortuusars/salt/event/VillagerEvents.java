package io.github.mortuusars.salt.event;

import io.github.mortuusars.salt.Salt;
import io.github.mortuusars.salt.configuration.Configuration;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Salt.ID)
public class VillagerEvents {
    @SubscribeEvent
    public static void onVillagerTrades(VillagerTradesEvent event) {
        if (!Configuration.BUTCHER_SALT_TRADES_ENABLED.get())
            return;

        Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
        VillagerProfession profession = event.getType();
        if (profession.name().equals("butcher")) {
            trades.get(1).add(emeraldForItemsTrade(Salt.Items.SALT.get(), 18, 12, 2));
            trades.get(1).add(emeraldForItemsTrade(Salt.Items.SALT.get(), 8, 16, 2));
            trades.get(3).add(itemForEmeraldTrade(Salt.Items.SALT.get(), 14, 5, 10, 6));
            trades.get(3).add(itemForEmeraldTrade(Salt.Items.SALT.get(), 8, 3, 12, 6));
        }
    }

    private static BasicItemListing emeraldForItemsTrade(ItemLike item, int count, int maxTrades, int xp) {
        return new BasicItemListing(new ItemStack(item, count), new ItemStack(Items.EMERALD), maxTrades, xp, 0.05F);
    }

    private static BasicItemListing itemForEmeraldTrade(ItemLike item, int itemsCount, int emeralds, int maxTrades, int xp) {
        return new BasicItemListing(emeralds, new ItemStack(item, itemsCount), maxTrades, xp, 0.05F);
    }
}
