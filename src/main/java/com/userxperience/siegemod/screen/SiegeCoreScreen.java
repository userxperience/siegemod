package com.userxperience.siegemod.screen;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.userxperience.siegemod.SiegeMod;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SiegeCoreScreen extends AbstractContainerScreen<SiegeCoreMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(SiegeMod.MOD_ID,"textures/gui/siege_core_gui.png");

    public SiegeCoreScreen(SiegeCoreMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressArrow(pPoseStack, x, y);

        this.render(pPoseStack, x + 55, y + 15);
    }



    private void renderProgressArrow(PoseStack pPoseStack, int x, int y) {
        if(menu.isCrafting()) {
            blit(pPoseStack, x + 105, y + 33, 176, 0, 8, menu.getScaledProgress());
        }
    }

    public void render(PoseStack pPoseStack, int mouseX, int mouseY) {
        renderBackground(pPoseStack);
        RenderSystem.enableBlend();
        pPoseStack.pushPose();
        {
            pPoseStack.translate(mouseX, mouseY, 0);
        }
        pPoseStack.popPose();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
        renderTooltip(pPoseStack, mouseX, mouseY);
    }

}