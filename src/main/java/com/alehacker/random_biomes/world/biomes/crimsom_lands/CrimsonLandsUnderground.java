package com.alehacker.random_biomes.world.biomes.crimsom_lands;

import com.alehacker.random_biomes.RandomBiomesMod;
import com.alehacker.random_biomes.block.ModBlocks;
import com.terraformersmc.biolith.api.biome.BiomePlacement;
import com.terraformersmc.biolith.api.surface.SurfaceGeneration;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.placement.CaveSurface;

public class CrimsonLandsUnderground {
    public static final ResourceKey<Biome> CRIMSON_CAVES =
            ResourceKey.create(Registries.BIOME,
                    ResourceLocation.fromNamespaceAndPath(RandomBiomesMod.MOD_ID, "crimson_caves"));

    public static void register() {

        BiomePlacement.addOverworld(CRIMSON_CAVES, Climate.parameters(
                Climate.Parameter.span(0.0F, 0.5F),
                Climate.Parameter.span(-0.5F, 0.0F),
                Climate.Parameter.span(0.3F, 1.0F),
                Climate.Parameter.span(-0.2F, 0.4F),
                Climate.Parameter.point(1.0F),
                Climate.Parameter.span(-0.56F, 0.56F),
                0.0F
        ));

        SurfaceRules.RuleSource rules = SurfaceRules.ifTrue(
                SurfaceRules.isBiome(CRIMSON_CAVES),
                SurfaceRules.sequence(
                        SurfaceRules.ifTrue(
                                SurfaceRules.stoneDepthCheck(0, false, CaveSurface.FLOOR),
                                SurfaceRules.sequence(
                                        SurfaceRules.ifTrue(
                                                SurfaceRules.noiseCondition(Noises.SURFACE, 0.0, 1.0),
                                                SurfaceRules.state(ModBlocks.CRIMSON.get().defaultBlockState())
                                        ),
                                        SurfaceRules.state(ModBlocks.CRIMSON_STONE.get().defaultBlockState())
                                )
                        ),
                        SurfaceRules.ifTrue(
                                SurfaceRules.stoneDepthCheck(0, false, CaveSurface.CEILING),
                                SurfaceRules.state(ModBlocks.CRIMSON_STONE.get().defaultBlockState())
                        ),
                        SurfaceRules.state(ModBlocks.CRIMSON_STONE.get().defaultBlockState())
                )
        );
        SurfaceGeneration.addOverworldSurfaceRules(
                ResourceLocation.fromNamespaceAndPath(RandomBiomesMod.MOD_ID, "crimson_caves"),
                rules
        );

    }
}
