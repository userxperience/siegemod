package com.userxperience.siegemod.entity.custom;

import com.userxperience.siegemod.ai.SiegeZombieBreakDoorGoal;
import com.userxperience.siegemod.block.ModBlocks;
import com.userxperience.siegemod.block.entity.ModBlockEntities;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeMod;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.Nullable;

public class SiegeZombieEntity extends Zombie implements GeoEntity {
    private AnimatableInstanceCache factory = new SingletonAnimatableInstanceCache(this);

    private static final Predicate<Difficulty> DOOR_BREAKING_PREDICATE = (difficulty) -> {
        return difficulty == Difficulty.HARD;
    };
    private final SiegeZombieBreakDoorGoal breakDoorGoal = new SiegeZombieBreakDoorGoal(this, DOOR_BREAKING_PREDICATE);

    private boolean canBreakDoors;

    public SiegeZombieEntity(EntityType<? extends Zombie> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier setAttributes() {
        return Zombie.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE)
                .add(Attributes.ARMOR, 2.0D)
                .build();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new com.userxperience.siegemod.ai.SiegeZombieBreakDoorGoal(this, DOOR_BREAKING_PREDICATE));
        this.goalSelector.addGoal(4, new SiegeZombieAttackSiegeCoreGoal(this, 1.0D, 3));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    private PlayState predicate(AnimationState animationState) {
        if(animationState.isMoving()) {
            animationState.getController().setAnimation(RawAnimation.begin().then("animation.siege_zombie.walk", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }

        animationState.getController().setAnimation(RawAnimation.begin().then("animation.siege_zombie.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

        private PlayState attackPredicate(AnimationState state) {
        if(this.swinging && state.getController().getAnimationState().equals(AnimationController.State.STOPPED)) {
            state.getController().forceAnimationReset();
            state.getController().setAnimation(RawAnimation.begin().then("animation.siege_zombie.attack", Animation.LoopType.PLAY_ONCE));
            this.swinging = false;
        }

        return PlayState.CONTINUE;
    }

    public boolean canBreakDoors() {
        return true;
    }

    protected boolean supportsBreakDoorGoal() {
        return true;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController(this, "controller",
                0, this::predicate));
        controllers.add(new AnimationController(this, "attackController",
                0, this::attackPredicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, 0.15F, 1.0F);
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.CAT_STRAY_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.DOLPHIN_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.DOLPHIN_DEATH;
    }

    protected float getSoundVolume() {
        return 0.2F;
    }

    class SiegeZombieAttackSiegeCoreGoal extends RemoveBlockGoal {
        SiegeZombieAttackSiegeCoreGoal(PathfinderMob pMob, double pSpeedModifier, int pVerticalSearchRange) {
            super(ModBlocks.SIEGE_CORE.get(), pMob, pSpeedModifier, pVerticalSearchRange);
        }

        public void playDestroyProgressSound(LevelAccessor pLevel, BlockPos pPos) {
//            pLevel.playSound((Player)null, pPos, SoundEvents.ZOMBIE_DESTROY_EGG, SoundSource.HOSTILE, 0.5F, 0.9F + Zombie.this.random.nextFloat() * 0.2F);
        }

        public void playBreakSound(Level pLevel, BlockPos pPos) {
            pLevel.playSound((Player)null, pPos, SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + pLevel.random.nextFloat() * 0.2F);
        }

        public double acceptedDistance() {
            return 1.14D;
        }
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        RandomSource randomsource = pLevel.getRandom();
        pSpawnData = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        float f = pDifficulty.getSpecialMultiplier();
        this.setCanPickUpLoot(randomsource.nextFloat() < 0.55F * f);
        if (pSpawnData == null) {
            pSpawnData = new Zombie.ZombieGroupData(getSpawnAsBabyOdds(randomsource), true);
        }

        if (pSpawnData instanceof Zombie.ZombieGroupData zombie$zombiegroupdata) {
            if (zombie$zombiegroupdata.isBaby) {
                this.setBaby(true);
                if (zombie$zombiegroupdata.canSpawnJockey) {
                    if ((double)randomsource.nextFloat() < 0.05D) {
                        List<Chicken> list = pLevel.getEntitiesOfClass(Chicken.class, this.getBoundingBox().inflate(5.0D, 3.0D, 5.0D), EntitySelector.ENTITY_NOT_BEING_RIDDEN);
                        if (!list.isEmpty()) {
                            Chicken chicken = list.get(0);
                            chicken.setChickenJockey(true);
                            this.startRiding(chicken);
                        }
                    } else if ((double)randomsource.nextFloat() < 0.05D) {
                        Chicken chicken1 = EntityType.CHICKEN.create(this.level);
                        if (chicken1 != null) {
                            chicken1.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                            chicken1.finalizeSpawn(pLevel, pDifficulty, MobSpawnType.JOCKEY, (SpawnGroupData)null, (CompoundTag)null);
                            chicken1.setChickenJockey(true);
                            this.startRiding(chicken1);
                            pLevel.addFreshEntity(chicken1);
                        }
                    }
                }
            }

            this.setCanBreakDoors(true);
            this.populateDefaultEquipmentSlots(randomsource, pDifficulty);
            this.populateDefaultEquipmentEnchantments(randomsource, pDifficulty);
        }

        if (this.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
            LocalDate localdate = LocalDate.now();
            int i = localdate.get(ChronoField.DAY_OF_MONTH);
            int j = localdate.get(ChronoField.MONTH_OF_YEAR);
            if (j == 10 && i == 31 && randomsource.nextFloat() < 0.25F) {
                this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(randomsource.nextFloat() < 0.1F ? Blocks.JACK_O_LANTERN : Blocks.CARVED_PUMPKIN));
                this.armorDropChances[EquipmentSlot.HEAD.getIndex()] = 0.0F;
            }
        }

        this.handleAttributes(f);
        return pSpawnData;
    }
    public void setCanBreakDoors(boolean pCanBreakDoors) {
        if (this.supportsBreakDoorGoal() && GoalUtils.hasGroundPathNavigation(this)) {
            if (this.canBreakDoors != pCanBreakDoors) {
                this.canBreakDoors = pCanBreakDoors;
                ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(pCanBreakDoors);
                if (pCanBreakDoors) {
                    this.goalSelector.addGoal(1, this.breakDoorGoal);
                } else {
                    this.goalSelector.removeGoal(this.breakDoorGoal);
                }
            }
        } else if (this.canBreakDoors) {
            this.goalSelector.removeGoal(this.breakDoorGoal);
            this.canBreakDoors = false;
        }

    }
}


