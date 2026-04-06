package com.alehacker.random_biomes.datagen;

import com.alehacker.random_biomes.block.ModBlocks;
import com.alehacker.random_biomes.items.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    protected ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {

        dropSelf(ModBlocks.CRIMSON_COBBLESTONE.get());

        add(ModBlocks.CRIMSON_STONE.get(),
            block -> createSingleItemTableWithSilkTouch(
                ModBlocks.CRIMSON_STONE.get(),
                ModBlocks.CRIMSON_COBBLESTONE.get())
        );

        add(ModBlocks.CRIMSON_GRASS.get(),
            block -> createSingleItemTableWithSilkTouch(
                ModBlocks.CRIMSON_GRASS.get(),
                Blocks.DIRT)
        );

        add(ModBlocks.CRIMSON_BUSH.get(),
            block -> createModGrassDrops(
                ModBlocks.CRIMSON_BUSH.get(),
                ModItems.CRIMSON_SEEDS.get())
        );
    }

    protected LootTable.Builder createModGrassDrops(Block block, Item item) {
        HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createShearsDispatchTable(
                block,
                this.applyExplosionDecay(
                        block,
                        LootItem.lootTableItem(item)
                                .when(LootItemRandomChanceCondition.randomChance(0.125F))
                                .apply(ApplyBonusCount.addUniformBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE), 2))
                )
        );
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
