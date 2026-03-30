package com.alehacker.random_biomes;

import com.alehacker.random_biomes.block.ModBlocks;
import com.alehacker.random_biomes.items.ModCreativeModeTabs;
import com.alehacker.random_biomes.items.ModItems;
import com.alehacker.random_biomes.world.ModBiomes;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(RandomBiomesMod.MOD_ID)
public class RandomBiomesMod {
    public static final String MOD_ID = "random_biomes";
    public static final Logger LOGGER = LogUtils.getLogger();
    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public RandomBiomesMod(IEventBus modEventBus, ModContainer modContainer) {
        ModCreativeModeTabs.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        modEventBus.addListener(this::commonSetup);

    }


    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(ModBiomes::register);
    }

    // Add the example block item to the building blocks tab
    private void buildContents(BuildCreativeModeTabContentsEvent event) {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }


}
