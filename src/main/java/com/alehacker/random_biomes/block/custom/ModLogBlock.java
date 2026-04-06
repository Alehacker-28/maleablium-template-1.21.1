package com.alehacker.random_biomes.block.custom;

import com.alehacker.random_biomes.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ModLogBlock extends RotatedPillarBlock {
    public ModLogBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return true;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 5;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 5;
    }

//    private static final Map<Block, Block> STRIPPING_MAP = new HashMap<>(Map.ofEntries(
//        Map.entry(ModBlocks.CRIMSON_LOG.get(), ModBlocks.STRIPPED_CRIMSON_LOG.get()),
//        Map.entry(ModBlocks.CRIMSON_WOOD.get(), ModBlocks.STRIPPED_CRIMSON_WOOD.get())
//    ));

//    @Override
//    public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context,
//                                                     ItemAbility itemAbility, boolean simulate) {
//        if(context.getItemInHand().getItem() instanceof AxeItem) {
//            Block stripped = STRIPPING_MAP.get(state.getBlock());
//            if(stripped != null) {
//                return stripped.defaultBlockState().setValue(AXIS, state.getValue(AXIS));
//            }
//
//        }
//        return super.getToolModifiedState(state, context, itemAbility, simulate);
//    }
}
