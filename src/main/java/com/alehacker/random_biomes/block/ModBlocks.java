package com.alehacker.random_biomes.block;

import com.alehacker.random_biomes.RandomBiomesMod;
import com.alehacker.random_biomes.block.custom.CrimsonBushBlock;
import com.alehacker.random_biomes.block.custom.CrimsonGrassBlock;
import com.alehacker.random_biomes.items.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(RandomBiomesMod.MOD_ID);

    //Crimson Block
    public static final DeferredBlock<Block> CRIMSON = registerBlock("crimson",
        () ->  new Block(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_RED)
            .strength(2f,6f)
            .sound(SoundType.STONE)
            .requiresCorrectToolForDrops()
        ));

    //Crimson Grass
    public static final DeferredBlock<Block> CRIMSON_GRASS = registerBlock("crimson_grass",
        () -> new CrimsonGrassBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRASS_BLOCK)
            .mapColor(MapColor.COLOR_RED)
            .strength(1f)
            .sound(SoundType.GRAVEL)
            .requiresCorrectToolForDrops()
        ));

    //Crimson Stone
    public static final DeferredBlock<Block> CRIMSON_STONE = registerBlock("crimson_stone",
        () -> new Block(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_RED)
            .strength(1.5f,6f)
            .sound(SoundType.STONE)
            .requiresCorrectToolForDrops()
        ));

    //Crimson Bush
    public static final DeferredBlock<Block> CRIMSON_BUSH = registerBlock("crimson_bush",
        () -> new CrimsonBushBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_RED)
            .noCollission()
            .noOcclusion()
            .sound(SoundType.GRASS)
            .instabreak()
        ));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

}