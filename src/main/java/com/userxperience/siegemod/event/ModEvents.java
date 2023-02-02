package com.userxperience.siegemod.event;

import com.userxperience.siegemod.SiegeMod;
import com.userxperience.siegemod.entity.ModEntityTypes;
import com.userxperience.siegemod.entity.custom.SiegeZombieEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ModEvents {
    @Mod.EventBusSubscriber(modid = SiegeMod.MOD_ID)
    public static class ForgeEvents {


                @Mod.EventBusSubscriber(modid = SiegeMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
                public static class ModEventBusEvents {
                    @SubscribeEvent
                    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
                        event.put(ModEntityTypes.SIEGE_ZOMBIE.get(), SiegeZombieEntity.setAttributes());
                    }
                }
            }

        }