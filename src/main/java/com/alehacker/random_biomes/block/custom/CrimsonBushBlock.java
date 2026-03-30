package com.alehacker.random_biomes.block.custom;

import com.alehacker.random_biomes.block.ModBlocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class CrimsonBushBlock extends BushBlock{


    public CrimsonBushBlock(Properties properties) {
        super(properties);
    }

    private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 13, 14);

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return state.is(ModBlocks.CRIMSON_GRASS) || super.mayPlaceOn(state, level, pos);
    }

    @Override
    public @NotNull MapCodec<CrimsonBushBlock> codec() {
        return simpleCodec(CrimsonBushBlock::new);
    }
}
