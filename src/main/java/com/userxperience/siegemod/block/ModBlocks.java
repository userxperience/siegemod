package com.userxperience.siegemod.block;

import com.userxperience.siegemod.SiegeMod;
import com.userxperience.siegemod.block.custom.NexusBlock;
import com.userxperience.siegemod.block.custom.SiegeCoreBlock;
import com.userxperience.siegemod.item.ModItems;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, SiegeMod.MODID);


    public static final RegistryObject<Block> CRYSTAL_BLOCK = registerBlock("crystal_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(6f).requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> CRYSTAL_ORE = registerBlock("crystal_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(6f).requiresCorrectToolForDrops(),
                    UniformInt.of(3, 7)));

    public static final RegistryObject<Block> DEEPSLATE_CRYSTAL_ORE = registerBlock("deepslate_crystal_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(6f).requiresCorrectToolForDrops(),
                    UniformInt.of(3, 7)));

    public static final RegistryObject<Block> NEXUS_BLOCK = registerBlock("nexus",
            () -> new NexusBlock(BlockBehaviour.Properties.of(Material.STONE)
                .strength(6f).requiresCorrectToolForDrops().lightLevel(state -> state.getValue(NexusBlock.ACTIVE) ? 15 : 0)));

    public static final RegistryObject<Block> SIEGE_CORE = registerBlock("siege_core",
            () -> new SiegeCoreBlock(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(6f).requiresCorrectToolForDrops()));


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    public static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }


        public static void register(IEventBus eventBus) {
            BLOCKS.register(eventBus);
        }
}
