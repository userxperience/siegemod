//package com.userxperience.siegemod.integration;
//
//import com.userxperience.siegemod.SiegeMod;
//import com.userxperience.siegemod.recipe.SiegeCoreRecipe;
//import net.minecraft.client.Minecraft;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.item.crafting.RecipeManager;
//
//import java.util.List;
//import java.util.Objects;
//
//@JeiPlugin
//public class JEISiegeModPlugin implements IModPlugin {
//    public static RecipeType<SiegeCoreRecipe> CHARGING_TYPE =
//            new RecipeType<>(SiegeCoreRecipeCategory.UID, SiegeCoreRecipe.class);
//
//    @Override
//    public ResourceLocation getPluginUid() {
//        return new ResourceLocation(SiegeMod.MOD_ID, "jei_plugin");
//    }
//
//    @Override
//    public void registerCategories(IRecipeCategoryRegistration registration) {
//        registration.addRecipeCategories(new
//                SiegeCoreRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
//    }
//
//    @Override
//    public void registerRecipes(IRecipeRegistration registration) {
//        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
//
//        List<SiegeCoreRecipe> recipesCharging = rm.getAllRecipesFor(SiegeCoreRecipe.Type.INSTANCE);
//        registration.addRecipes(CHARGING_TYPE, recipesCharging);
//    }
//}