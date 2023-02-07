package com.userxperience.siegemod.recipe;

import com.userxperience.siegemod.SiegeMod;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SiegeMod.MOD_ID);

    public static final RegistryObject<RecipeSerializer<SiegeCoreRecipe>> GEM_CHARGING_SERIALIZER =
            SERIALIZERS.register("gem_charging", () -> SiegeCoreRecipe.Serializer.INSTANCE);

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}
