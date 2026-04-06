package com.alehacker.random_biomes.datagen;

import com.alehacker.random_biomes.RandomBiomesMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = RandomBiomesMod.MOD_ID)
public class DataGenerators {

    @SubscribeEvent
    public static void gaterData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new LootTableProvider(packOutput, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(ModBlockLootTableProvider::new,
                        LootContextParamSets.BLOCK)), lookupProvider)
        );

        generator.addProvider(event.includeServer(), new ModRecipeProvider(packOutput,lookupProvider));

        BlockTagsProvider blockTagsProvider = new ModBlockTagProvider(packOutput,lookupProvider,fileHelper);
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new ModItemTagProvider(packOutput,lookupProvider,blockTagsProvider.contentsGetter(), fileHelper));


        generator.addProvider(event.includeClient(), new ModItemModelProvider(packOutput, fileHelper));
        generator.addProvider(event.includeClient(), new ModBlcokStateProvider(packOutput, fileHelper));
    }


}
