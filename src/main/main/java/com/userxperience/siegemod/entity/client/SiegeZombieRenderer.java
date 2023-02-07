package com.userxperience.siegemod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.userxperience.siegemod.SiegeMod;
import com.userxperience.siegemod.entity.custom.SiegeZombieEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

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
    public RenderType getRenderType(SiegeZombieEntity animatable, float partialTicks, PoseStack stack,
                                    @Nullable MultiBufferSource renderTypeBuffer,
                                    @Nullable VertexConsumer vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        stack.scale(0.8f, 0.8f, 0.8f);
        return super.getRenderType(animatable, partialTicks, stack, renderTypeBuffer, vertexBuilder, packedLightIn, textureLocation);
    }
}
