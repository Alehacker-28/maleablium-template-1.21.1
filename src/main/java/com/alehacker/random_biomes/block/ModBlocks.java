package com.alehacker.random_biomes.block;

import com.alehacker.random_biomes.RandomBiomesMod;
import com.alehacker.random_biomes.block.custom.ModBushBlock;
import com.alehacker.random_biomes.block.custom.ModVineGrowBlock;
import com.alehacker.random_biomes.block.custom.ModSpreadingBlock;
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

    public static final DeferredBlock<Block> CRIMSON_COBBLESTONE = registerBlock("crimson_cobblestone",
        () ->  new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE)));

    public static final DeferredBlock<Block> CRIMSON_GRASS = registerBlock("crimson_grass",
        () -> new ModSpreadingBlock(BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_RED)
                .strength(0.6f)
                .sound(SoundType.GRASS)
                .randomTicks()));

    public static final DeferredBlock<Block> CRIMSON_STONE = registerBlock("crimson_stone",
        () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));

    public static final DeferredBlock<Block> CRIMSON_BUSH = registerBlock("crimson_bush",
        () -> new ModBushBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS)));

    public static final DeferredBlock<Block> CRIMSON_VINES = registerBlock("crimson_vines",
            () -> new ModVineGrowBlock(BlockBehaviour.Properties.of()
                    .randomTicks()
                    .sound(SoundType.WEEPING_VINES)
                    .instabreak()
            ));


//    public static final DeferredBlock<Block> CRIMSON_LOG = registerBlock("crimson_log",
//        () -> new ModLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)));
//
//    public static final DeferredBlock<Block> CRIMSON_WOOD = registerBlock("crimson_wood",
//        () -> new ModLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)));
//
//    public static final DeferredBlock<Block> STRIPPED_CRIMSON_LOG = registerBlock("stripped_crimson_log",
//        () -> new ModLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)));
//
//    public static final DeferredBlock<Block> STRIPPED_CRIMSON_WOOD = registerBlock("stripped_crimson_wood",
//        () -> new ModLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)));
//
//    public static final DeferredBlock<Block> CRIMSON_PLANKS = registerBlock("crimson_planks",
//        () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));
//
//    public static final DeferredBlock<Block> CRIMSON_LEAVES = registerBlock("crimson_leaves",
//        () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)));





    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

}
