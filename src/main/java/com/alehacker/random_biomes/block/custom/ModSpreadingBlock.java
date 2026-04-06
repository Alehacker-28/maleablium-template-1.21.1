package com.alehacker.random_biomes.block.custom;

import com.alehacker.random_biomes.block.ModBlocks;
import com.alehacker.random_biomes.world.ModBiomes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.commands.FillBiomeCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class ModSpreadingBlock extends Block {
    public ModSpreadingBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (world.isClientSide) return;
        spread(state, world, pos, random);

        if (random.nextFloat() < 0.002f) {
            tryTransformBiome(world, pos);
        }
    }

    private void spread(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (random.nextFloat() > 0.25f) return;
        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 2; y++) {
                for (int z = -2; z <= 2; z++) {
                    if (x == 0 && y == 0 && z == 0) continue;

                    BlockPos targetPos = pos.offset(x, y, z);
                    BlockState targetState = world.getBlockState(targetPos);

                    if (targetState.is(Blocks.GRASS_BLOCK)) {
                        BlockPos aboveTargetPos = targetPos.above();
                        BlockState aboveTargetState = world.getBlockState(aboveTargetPos);

                        if (aboveTargetState.is(Blocks.SHORT_GRASS)) {
                            world.setBlockAndUpdate(aboveTargetPos,
                                    ModBlocks.CRIMSON_BUSH.get().defaultBlockState());
                        }

                        world.setBlockAndUpdate(targetPos, state);
                        return;
                    }
                }
            }
        }
    }

    private void tryTransformBiome(ServerLevel world, BlockPos pos) {
        if (world.getGameTime() % 20 != 0) return;
        if (world.getBiome(pos).is(ModBiomes.CRIMSON_LANDS)) return;

        int radius = 16;
        float fillRatio = 0.50f;
        int diameter = radius * 2;
        int totalColumns = diameter * diameter;
        int threshold = (int)(totalColumns * fillRatio);
        int crimsonCount = 0;

        for (int xOffset = -radius; xOffset < radius; xOffset++) {
            for (int zOffset = -radius; zOffset < radius; zOffset++) {
                BlockPos columnPos = pos.offset(xOffset, 0, zOffset);
                for (int y = -9; y <= 9; y++) {
                    if (world.getBlockState(columnPos.above(y)).is(this)) {
                        crimsonCount++;
                        break;
                    }
                }
            }
        }

        if (crimsonCount >= threshold) {
            Holder<Biome> newBiome = world.registryAccess()
                    .lookupOrThrow(Registries.BIOME)
                    .getOrThrow(ModBiomes.CRIMSON_LANDS);

            BlockPos from = pos.offset(-radius, -8, -radius);
            BlockPos to   = pos.offset(radius,   8,  radius);
            FillBiomeCommand.fill(world, from, to, newBiome);
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }
}
