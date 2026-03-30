package com.alehacker.random_biomes.world;

import com.alehacker.random_biomes.world.biomes.crimsom_lands.CrimsonLandsSurface;
import com.alehacker.random_biomes.world.biomes.crimsom_lands.CrimsonLandsUnderground;


public class ModBiomes {

    public static void register() {
        CrimsonLandsSurface.register();
        CrimsonLandsUnderground.register();
    }

}
