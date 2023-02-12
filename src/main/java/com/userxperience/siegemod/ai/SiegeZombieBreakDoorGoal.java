package com.userxperience.siegemod.ai;

import java.util.function.Predicate;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.DoorInteractGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Predicate;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;

public class SiegeZombieBreakDoorGoal extends Goal {
    private static final int DEFAULT_DOOR_BREAK_TIME = 240;
    private final Predicate<Difficulty> validDifficulties;
    private final Mob mob;
    protected BlockPos doorPos = BlockPos.ZERO;
    protected int breakTime;
    protected int lastBreakProgress = -1;
    protected int doorBreakTime = -1;
    protected boolean hasDoor;
    private boolean passed;
    private float doorOpenDirX;
    private float doorOpenDirZ;

    public SiegeZombieBreakDoorGoal(Mob pMob, Predicate<Difficulty> pValidDifficulties) {
        if (!GoalUtils.hasGroundPathNavigation(pMob)) {
            throw new IllegalArgumentException("Unsupported mob type for DoorInteractGoal");
        }
        this.mob = pMob;
        this.validDifficulties = pValidDifficulties;
    }

    public SiegeZombieBreakDoorGoal(Mob pMob, int pDoorBreakTime, Predicate<Difficulty> pValidDifficulties) {
        this(pMob, pValidDifficulties);
        this.doorBreakTime = pDoorBreakTime;
    }

    protected int getDoorBreakTime() {
        return Math.max(240, this.doorBreakTime);
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean canUse() {
        if (!GoalUtils.hasGroundPathNavigation(this.mob)) {
            return false;
        } else if (!this.mob.horizontalCollision) {
            return false;
        } else {
            GroundPathNavigation groundpathnavigation = (GroundPathNavigation)this.mob.getNavigation();
            Path path = groundpathnavigation.getPath();
            if (path != null && !path.isDone() && groundpathnavigation.canOpenDoors()) {
                for(int i = 0; i < Math.min(path.getNextNodeIndex() + 2, path.getNodeCount()); ++i) {
                    Node node = path.getNode(i);
                    this.doorPos = new BlockPos(node.x, node.y + 1, node.z);
                    if (!(this.mob.distanceToSqr((double)this.doorPos.getX(), this.mob.getY(), (double)this.doorPos.getZ()) > 2.25D)) {
                        this.hasDoor = DoorBlock.isWoodenDoor(this.mob.level, this.doorPos);
                        if (this.hasDoor) {
                            return true;
                        }
                    }
                }

                this.doorPos = this.mob.blockPosition().above();
                this.hasDoor = DoorBlock.isWoodenDoor(this.mob.level, this.doorPos);
                return this.hasDoor;
            } else {
                return false;
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void start() {
        this.passed = false;
        this.doorOpenDirX = (float)((double)this.doorPos.getX() + 0.5D - this.mob.getX());
        this.doorOpenDirZ = (float)((double)this.doorPos.getZ() + 0.5D - this.mob.getZ());
        this.breakTime = 0;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean canContinueToUse() {
        return this.breakTime <= this.getDoorBreakTime() && !this.isOpen() && this.doorPos.closerToCenterThan(this.mob.position(), 2.0D) && this.isValidDifficulty(this.mob.level.getDifficulty());
    }

    protected boolean isOpen() {
        if (!this.hasDoor) {
            return false;
        } else {
            BlockState blockstate = this.mob.level.getBlockState(this.doorPos);
            if (!(blockstate.getBlock() instanceof DoorBlock)) {
                this.hasDoor = false;
                return false;
            } else {
                return blockstate.getValue(DoorBlock.OPEN);
            }
        }
    }

    protected void setOpen(boolean pOpen) {
        if (this.hasDoor) {
            BlockState blockstate = this.mob.level.getBlockState(this.doorPos);
            if (blockstate.getBlock() instanceof DoorBlock) {
                ((DoorBlock)blockstate.getBlock()).setOpen(this.mob, this.mob.level, blockstate, this.doorPos, pOpen);
            }
        }

    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void stop() {
        super.stop(); //from Goal
        this.mob.level.destroyBlockProgress(this.mob.getId(), this.doorPos, -1);
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        float f = (float)((double)this.doorPos.getX() + 0.5D - this.mob.getX());
        float f1 = (float)((double)this.doorPos.getZ() + 0.5D - this.mob.getZ());
        float f2 = this.doorOpenDirX * f + this.doorOpenDirZ * f1;
        if (f2 < 0.0F) {
            this.passed = true;
        }

        if (this.mob.getRandom().nextInt(20) == 0) {
            this.mob.level.levelEvent(1019, this.doorPos, 0);
            if (!this.mob.swinging) {
                this.mob.swing(this.mob.getUsedItemHand());
            }
        }

        ++this.breakTime;
        int i = (int)((float)this.breakTime / (float)this.getDoorBreakTime() * 10.0F);
        if (i != this.lastBreakProgress) {
            this.mob.level.destroyBlockProgress(this.mob.getId(), this.doorPos, i);
            this.lastBreakProgress = i;
        }

        if (this.breakTime == this.getDoorBreakTime() && this.isValidDifficulty(this.mob.level.getDifficulty())) {
            this.mob.level.removeBlock(this.doorPos, false);
            this.mob.level.levelEvent(1021, this.doorPos, 0);
            this.mob.level.levelEvent(2001, this.doorPos, Block.getId(this.mob.level.getBlockState(this.doorPos)));
        }

    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    private boolean isValidDifficulty(Difficulty pDifficulty) {
        return this.validDifficulties.test(pDifficulty);
    }
}
