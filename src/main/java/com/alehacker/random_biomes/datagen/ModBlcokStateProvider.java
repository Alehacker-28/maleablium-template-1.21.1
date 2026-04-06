package com.alehacker.random_biomes.datagen;

import com.alehacker.random_biomes.RandomBiomesMod;
import com.alehacker.random_biomes.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class ModBlcokStateProvider extends BlockStateProvider {

    public ModBlcokStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, RandomBiomesMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.CRIMSON_STONE);
        blockWithItem(ModBlocks.CRIMSON_COBBLESTONE);
    }

    private void blockWithItem (DeferredBlock<?> deferredBlock){
        simpleBlockItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }
}
