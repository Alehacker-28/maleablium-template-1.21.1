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
import net.minecraft.world.level.levelgen.SurfaceRules;

public class CrimsonLandsSurface {
    public static final ResourceKey<Biome> CRIMSON_LANDS =
            ResourceKey.create(Registries.BIOME,
                    ResourceLocation.fromNamespaceAndPath(RandomBiomesMod.MOD_ID, "crimson_lands"));

    public static void register() {

        BiomePlacement.addOverworld(CRIMSON_LANDS, Climate.parameters(
                Climate.Parameter.span(0.0F, 0.5F),
                Climate.Parameter.span(-0.5F, 0.0F),
                Climate.Parameter.span(0.3F, 1.0F),
                Climate.Parameter.span(-0.2F, 0.4F),
                Climate.Parameter.point(0.0F),
                Climate.Parameter.span(-0.56F, 0.56F),
                0.0F
        ));

        SurfaceRules.RuleSource rules = SurfaceRules.ifTrue(
                SurfaceRules.isBiome(CRIMSON_LANDS),
                SurfaceRules.sequence(
                        SurfaceRules.ifTrue(
                                SurfaceRules.ON_FLOOR,
                                SurfaceRules.sequence(
                                        SurfaceRules.ifTrue(
                                                SurfaceRules.waterBlockCheck(-1, 0),
                                                SurfaceRules.state(ModBlocks.CRIMSON_GRASS.get().defaultBlockState())
                                        ),
                                        // Dry land → crimson grass
                                        SurfaceRules.state(ModBlocks.CRIMSON.get().defaultBlockState())
                                )

                        ), SurfaceRules.ifTrue(
                                SurfaceRules.UNDER_FLOOR,
                                SurfaceRules.state(ModBlocks.CRIMSON_STONE.get().defaultBlockState())
                        ),
                        SurfaceRules.state(ModBlocks.CRIMSON_STONE.get().defaultBlockState())
                )
        );

        SurfaceGeneration.addOverworldSurfaceRules(
                ResourceLocation.fromNamespaceAndPath(RandomBiomesMod.MOD_ID, "crimson_lands"),
                rules
        );

    }
}
