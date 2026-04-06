package com.alehacker.random_biomes.items;

import com.alehacker.random_biomes.RandomBiomesMod;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(RandomBiomesMod.MOD_ID);

    public static final DeferredItem<Item> CRIMSON_SEEDS = ITEMS.register("crimson_seeds",
            () -> new Item(new Item.Properties()));

}
