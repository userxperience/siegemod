package com.userxperience.siegemod.ai;

import java.util.function.Predicate;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RemoveBlockGoal;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;

public class BreakDoorGoal extends Goal{
    private final Mob mob;
    protected BlockPos doorPos = BlockPos.ZERO;
    private static final int DEFAULT_DOOR_BREAK_TIME = 240;
    private final Predicate<Difficulty> validDifficulties;
    protected int breakTime;
    protected int lastBreakProgress = -1;
    protected int doorBreakTime = -1;
    protected boolean hasDoor;

    public BreakDoorGoal(Mob pMob, Predicate<Difficulty> pValidDifficulties) {
        this.mob = pMob;
        this.validDifficulties = pValidDifficulties;
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

    public BreakDoorGoal(Mob pMob, int pDoorBreakTime, Predicate<Difficulty> pValidDifficulties) {
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
         if (!net.minecraftforge.common.ForgeHooks.canEntityDestroy(this.mob.level, this.doorPos, this.mob)) {
            return false;
        } else {
            return this.isValidDifficulty(this.mob.level.getDifficulty()) && !this.isOpen();
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void start() {
        super.start();
        this.breakTime = 0;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean canContinueToUse() {
        return this.breakTime <= this.getDoorBreakTime() && !this.isOpen() && this.doorPos.closerToCenterThan(this.mob.position(), 2.0D) && this.isValidDifficulty(this.mob.level.getDifficulty());
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void stop() {
        super.stop();
        this.mob.level.destroyBlockProgress(this.mob.getId(), this.doorPos, -1);
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        super.tick();
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

    private boolean isValidDifficulty(Difficulty pDifficulty) {
        return this.validDifficulties.test(pDifficulty);
    }
}
