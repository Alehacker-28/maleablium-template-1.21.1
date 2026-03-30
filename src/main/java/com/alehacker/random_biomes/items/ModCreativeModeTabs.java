package com.alehacker.random_biomes.items;

import com.alehacker.random_biomes.RandomBiomesMod;
import com.alehacker.random_biomes.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RandomBiomesMod.MOD_ID);

    public static final Supplier<CreativeModeTab> CRIMSON_BLOCKS_TAB = CREATIVE_MODE_TAB.register("crimson_blocks_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModBlocks.CRIMSON.get()))
                    .title(Component.translatable("creativetab.random_biomes.crimson_blocks"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModBlocks.CRIMSON);
                        output.accept(ModBlocks.CRIMSON_GRASS);
                        output.accept(ModBlocks.CRIMSON_STONE);
                        output.accept(ModBlocks.CRIMSON_BUSH);
                    })

                    .build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
