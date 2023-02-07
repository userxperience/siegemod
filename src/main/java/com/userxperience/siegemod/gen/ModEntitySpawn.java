package com.userxperience.siegemod.gen;


import net.minecraft.client.Minecraft;


    public class ModEntitySpawn {
        public static void addEntitySpawn() {
            BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.DESERT), SpawnGroup.MONSTER,
                    ModEntities.CHOMPER, 50, 1, 3);

            SpawnRestriction.register(ModEntities.CHOMPER, SpawnRestriction.Location.ON_GROUND,
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnInDark);
        }
    }