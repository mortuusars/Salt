package io.github.mortuusars.salt;

import com.mojang.logging.LogUtils;
import io.github.mortuusars.salt.configuration.Configuration;
import io.github.mortuusars.salt.event.ClientEvents;
import io.github.mortuusars.salt.event.CommonEvents;
import net.minecraft.SharedConstants;
import net.minecraft.core.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Mod(Salt.ID)
public class Salt
{
    public static final String ID = "salt";

    public Salt() {
        Configuration.init();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Registry.register(modEventBus);

        modEventBus.addListener(CommonEvents::onCommonSetup);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            modEventBus.addListener(ClientEvents::onClientSetup);
            modEventBus.addListener(ClientEvents::onRegisterModels);
            MinecraftForge.EVENT_BUS.addListener(ClientEvents::onItemTooltipEvent);
        });
        MinecraftForge.EVENT_BUS.addListener(CommonEvents::onItemUseFinish);
        MinecraftForge.EVENT_BUS.addListener(CommonEvents::onBiomeLoadingEvent);

        MinecraftForge.EVENT_BUS.register(this);

        MinecraftForge.EVENT_BUS.addListener(this::onRightClick);
    }

    public static ResourceLocation resource(String path) {
        return new ResourceLocation(ID, path);
    }

    // Testing
    private void onRightClick(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntity().getLevel().isClientSide || !(event.getEntity() instanceof Player player))
            return;
    }
}
