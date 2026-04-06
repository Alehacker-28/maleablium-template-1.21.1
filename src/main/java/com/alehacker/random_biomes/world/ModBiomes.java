package com.alehacker.random_biomes.world;

import com.alehacker.random_biomes.RandomBiomesMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public class ModBiomes {

    public static final ResourceKey<Biome> CRIMSON_LANDS =
            ResourceKey.create(Registries.BIOME,
                    ResourceLocation.fromNamespaceAndPath(RandomBiomesMod.MOD_ID, "crimson_lands"));

    public static final ResourceKey<Biome> CRIMSON_CAVES =
            ResourceKey.create(Registries.BIOME,
                    ResourceLocation.fromNamespaceAndPath(RandomBiomesMod.MOD_ID, "crimson_caves"));

    public static void register() {

    }
}
