package com.userxperience.siegemod.entity.client;

import com.userxperience.siegemod.SiegeMod;
import com.userxperience.siegemod.entity.custom.SiegeZombieEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import javax.annotation.Nullable;

public class SiegeZombieRenderer extends GeoEntityRenderer<SiegeZombieEntity> {

    public SiegeZombieRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SiegeZombieModel());
        this.shadowRadius = 0.7F;
    }

    @Override
    public ResourceLocation getTextureLocation(SiegeZombieEntity entity) {
        return new ResourceLocation(SiegeMod.MOD_ID, "textures/entity/siege_zombie_texture.png");
    }

    @Override
    public RenderType getRenderType(SiegeZombieEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}
