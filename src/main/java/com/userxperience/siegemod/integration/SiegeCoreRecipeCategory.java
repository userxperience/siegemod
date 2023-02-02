package com.userxperience.siegemod.integration;

import com.userxperience.siegemod.SiegeMod;
import com.userxperience.siegemod.block.ModBlocks;
import com.userxperience.siegemod.recipe.SiegeCoreRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class SiegeCoreRecipeCategory implements IRecipeCategory<SiegeCoreRecipe> {
    public final static ResourceLocation UID = new ResourceLocation(SiegeMod.MOD_ID, "gem_charging");
    public final static ResourceLocation TEXTURE =
            new ResourceLocation(SiegeMod.MOD_ID, "textures/gui/siege_core_gui.png");

    private final IDrawable background;
    private final IDrawable icon;

    public SiegeCoreRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 85);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.SIEGE_CORE.get()));
    }

    @Override
    public RecipeType<SiegeCoreRecipe> getRecipeType() {
        return JEISiegeModPlugin.CHARGING_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Siege Core");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SiegeCoreRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 86, 15).addIngredients(recipe.getIngredients().get(0));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 86, 60).addItemStack(recipe.getResultItem());
    }
}