package com.alehacker.random_biomes.client.biome_colors;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockBiomeColor implements BlockColor {

    @Override
    public int getColor(@NotNull BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int tintIndex) {
        return level != null && pos != null
                ? BiomeColors.getAverageGrassColor(level, pos)
                : GrassColor.getDefaultColor();
    }
}
