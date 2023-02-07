package com.userxperience.siegemod.entity;

import com.userxperience.siegemod.SiegeMod;
import com.userxperience.siegemod.entity.custom.SiegeZombieEntity;

import net.minecraft.resources.ResourceLocation;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, SiegeMod.MOD_ID);

    public static final RegistryObject<EntityType<SiegeZombieEntity>> SIEGE_ZOMBIE =
            ENTITY_TYPES.register("siege_zombie",
                    () -> EntityType.Builder.of(SiegeZombieEntity::new, MobCategory.MONSTER)
                            .sized(0.4f, 1.5f)
                            .build(new ResourceLocation(SiegeMod.MOD_ID, "siege_zombie").toString()));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}