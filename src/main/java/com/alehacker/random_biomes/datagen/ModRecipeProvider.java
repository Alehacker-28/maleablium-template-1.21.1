package com.alehacker.random_biomes.datagen;

import com.alehacker.random_biomes.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    List<ItemLike> CRIMSON_STONE_SMELTABLE = List.of(ModBlocks.CRIMSON_COBBLESTONE.get());

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        oreSmelting(recipeOutput,CRIMSON_STONE_SMELTABLE,RecipeCategory.MISC,
                ModBlocks.CRIMSON_STONE.get(),0.2f,200,"crimson_stone");
    }
}
