package com.userxperience.siegemod.entity.client;

import com.userxperience.siegemod.SiegeMod;
import com.userxperience.siegemod.entity.custom.SiegeZombieEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SiegeZombieModel extends GeoModel<SiegeZombieEntity> {


    @Override
    public ResourceLocation getModelResource(SiegeZombieEntity object) {
        return new ResourceLocation(SiegeMod.MOD_ID, "geo/siege_zombie.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SiegeZombieEntity object) {
        return new ResourceLocation(SiegeMod.MOD_ID, "textures/entity/siege_zombie_texture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SiegeZombieEntity animatable) {
        return new ResourceLocation(SiegeMod.MOD_ID, "animations/siege_zombie.animation.json");
    }
}
