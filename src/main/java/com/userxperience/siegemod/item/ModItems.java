package com.userxperience.siegemod.item;

import com.userxperience.siegemod.SiegeMod;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, SiegeMod.MODID);

    public static final RegistryObject<Item> CRYSTAL = ITEMS.register("crystal",
            () -> new Item(new Item.Properties().tab(ModItemGroup.TAB_SIEGEMOD)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }


}
