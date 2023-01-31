package com.userxperience.siegemod.item;

import com.userxperience.siegemod.SiegeMod;
import com.userxperience.siegemod.item.custom.ChargedAmethystShardItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, SiegeMod.MODID);

    public static final RegistryObject<Item> CRYSTAL = ITEMS.register("crystal",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.SIEGE_TAB)));

    public static final RegistryObject<Item> RAW_CRYSTAL = ITEMS.register("raw_crystal",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.SIEGE_TAB)));

    public static final RegistryObject<Item> CHARGED_AMETHYST_SHARD = ITEMS.register("charged_amethyst_shard",
            () -> new ChargedAmethystShardItem(new Item.Properties().tab(ModCreativeModeTab.SIEGE_TAB)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }


}
