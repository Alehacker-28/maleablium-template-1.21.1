package com.alehacker.random_biomes.client;

import com.alehacker.random_biomes.RandomBiomesMod;
import com.alehacker.random_biomes.block.ModBlocks;
import com.alehacker.random_biomes.client.biome_colors.BlockBiomeColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@EventBusSubscriber(modid = RandomBiomesMod.MOD_ID, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register(
                new BlockBiomeColor(),
                ModBlocks.CRIMSON_GRASS.get(),
                ModBlocks.CRIMSON_BUSH.get()
        );
    }

    @SubscribeEvent
    public static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
        event.register(
                (stack, tintIndex) -> 0xC6BC55,
                ModBlocks.CRIMSON_GRASS.get(),
                ModBlocks.CRIMSON_BUSH.get()
        );
    }
}