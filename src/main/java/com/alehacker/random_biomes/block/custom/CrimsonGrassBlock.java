package com.alehacker.random_biomes.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.GrassBlock;
import org.jetbrains.annotations.NotNull;

public class CrimsonGrassBlock extends GrassBlock {

    public CrimsonGrassBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull MapCodec<GrassBlock> codec() {
        return simpleCodec(CrimsonGrassBlock::new);
    }
}
