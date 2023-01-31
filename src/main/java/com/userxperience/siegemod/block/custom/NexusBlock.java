package com.userxperience.siegemod.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.state.properties.BooleanProperty;


public class NexusBlock extends Block {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public NexusBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ACTIVE);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
            if (!level.isClientSide && hand == InteractionHand.MAIN_HAND) {
                player.sendSystemMessage(Component.literal("clicked nexus"));
                level.setBlock(pos, state.cycle(ACTIVE), 3);
            }
        return super.use(state, level, pos, player, hand, hit);
    }

}
