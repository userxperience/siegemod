package com.userxperience.siegemod.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class ChargedAmethystShardItem extends Item {
    public ChargedAmethystShardItem(Properties properties) {
        super(properties);
    }


    //adds tooltip
//    @Override
//    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
//        if(Screen.hasShiftDown()) {
//            tooltip.add(Component.literal("This is a charged amethyst.").withStyle(ChatFormatting.AQUA));
//        } else {
//            tooltip.add(Component.literal("Hold shift for more info").withStyle(ChatFormatting.GOLD));
//        }
//        super.appendHoverText(stack, level, tooltip, flag);
//    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(!level.isClientSide && hand == InteractionHand.MAIN_HAND) {

            //logs something
            outputRandomNumber(player);

            //adds cooldown
            player.getCooldowns().addCooldown(this, 20);
        }


        return super.use(level, player, hand);
    }

    private void outputRandomNumber(Player player) {
        player.sendSystemMessage(Component.literal("Random number: " + getRandomNumber()));
    }

    private int getRandomNumber() {
        return RandomSource.createNewThreadLocalInstance().nextInt(10);

    }
}
