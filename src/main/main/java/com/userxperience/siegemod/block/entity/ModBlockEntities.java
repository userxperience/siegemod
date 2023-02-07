package com.userxperience.siegemod.block.entity;



import com.userxperience.siegemod.SiegeMod;
import com.userxperience.siegemod.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;



public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SiegeMod.MOD_ID);



    public static final RegistryObject<BlockEntityType<SiegeCoreBlockEntity>> SIEGE_CORE =
            BLOCK_ENTITIES.register("siege_core", () ->
                    BlockEntityType.Builder.of(SiegeCoreBlockEntity::new,
                        ModBlocks.SIEGE_CORE.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}