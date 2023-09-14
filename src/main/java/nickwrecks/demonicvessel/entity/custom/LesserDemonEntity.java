package nickwrecks.demonicvessel.entity.custom;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;
import nickwrecks.demonicvessel.entity.ai.LesserDemonAi;
import nickwrecks.demonicvessel.entity.ai.behaviour.ModMemoryModuleTypes;

import javax.annotation.Nullable;
import java.util.Optional;

public class LesserDemonEntity extends PathfinderMob implements Enemy {

    private final static EntityDataAccessor<Integer> DATA_ID_ATTACK_TARGET = SynchedEntityData.defineId(LesserDemonEntity.class, EntityDataSerializers.INT);
    private final static EntityDataAccessor<Boolean> DATA_IS_CHANNELING_SLOW = SynchedEntityData.defineId(LesserDemonEntity.class, EntityDataSerializers.BOOLEAN);
    private LivingEntity clientSideCachedAttackTarget;
    private Boolean isChannelingSlow;
    private int clientSideSlowTime;

    public LesserDemonEntity(EntityType<? extends LesserDemonEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.xpReward = 5;
        this.setPathfindingMalus(BlockPathTypes.WALKABLE, 1.0f);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 1.0f);
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_ATTACK_TARGET,0);
        this.entityData.define(DATA_IS_CHANNELING_SLOW, false);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        super.onSyncedDataUpdated(pKey);
        if (DATA_ID_ATTACK_TARGET.equals(pKey)) {
            this.clientSideCachedAttackTarget = null;
        }
        if(DATA_IS_CHANNELING_SLOW.equals(pKey)) {
            this.clientSideSlowTime = 0;
            this.isChannelingSlow = false;
        }
    }

    public void setActiveAttackTarget(int pActiveAttackTargetId) {
        this.entityData.set(DATA_ID_ATTACK_TARGET, pActiveAttackTargetId);
    }
    public void setIsChannelingSlow(boolean isChannelingSlow) {
        this.entityData.set(DATA_IS_CHANNELING_SLOW, isChannelingSlow);
    }
    @Nullable
    public LivingEntity getActiveAttackTarget() {
        if (this.entityData.get(DATA_ID_ATTACK_TARGET) == 0) {
            return null;
        } else if (this.level.isClientSide) {
            if (this.clientSideCachedAttackTarget != null) {
                return this.clientSideCachedAttackTarget;
            } else {
                Entity entity = this.level.getEntity(this.entityData.get(DATA_ID_ATTACK_TARGET));
                if (entity instanceof LivingEntity) {
                    this.clientSideCachedAttackTarget = (LivingEntity)entity;
                    return this.clientSideCachedAttackTarget;
                } else {
                    return null;
                }
            }
        } else {
            return this.getTarget();
        }
    }

    public boolean getIsChannelingSlow() {
        this.isChannelingSlow = this.entityData.get(DATA_IS_CHANNELING_SLOW);
        return isChannelingSlow;
    }
    public int getSlowTime() {
        return this.clientSideSlowTime;
    }

    public static AttributeSupplier setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 25)
                .add(Attributes.MOVEMENT_SPEED, 0.65f)
                .add(Attributes.ATTACK_SPEED, 0.5f)
                .add(Attributes.ATTACK_DAMAGE, 10.0f)
                .add(Attributes.FLYING_SPEED, (double) 0.1F)
                .add(Attributes.MOVEMENT_SPEED, (double) 0.1F).build();

    }

    /*  private static final List<MemoryModuleType<?>> MEMORY_TYPES =
              List.of(MemoryModuleType.RECENT_PROJECTILE,
                MemoryModuleType.DIG_COOLDOWN, MemoryModuleType.SONIC_BOOM_COOLDOWN, MemoryModuleType.SONIC_BOOM_SOUND_COOLDOWN, MemoryModuleType.SONIC_BOOM_SOUND_DELAY);
       (MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER,
        MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET,
         MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.NEAREST_ATTACKABLE,
         MemoryModuleType.ROAR_TARGET, MemoryModuleType.DISTURBANCE_LOCATION, MemoryModuleType.RECENT_PROJECTILE, MemoryModuleType.IS_SNIFFING, MemoryModuleType.IS_EMERGING,
          MemoryModuleType.ROAR_SOUND_DELAY, MemoryModuleType.DIG_COOLDOWN, MemoryModuleType.ROAR_SOUND_COOLDOWN, MemoryModuleType.SNIFF_COOLDOWN, MemoryModuleType.TOUCH_COOLDOWN,
          MemoryModuleType.VIBRATION_COOLDOWN, MemoryModuleType.SONIC_BOOM_COOLDOWN, MemoryModuleType.SONIC_BOOM_SOUND_COOLDOWN, MemoryModuleType.SONIC_BOOM_SOUND_DELAY);

  */
    protected static final ImmutableList<SensorType<? extends Sensor<? super LesserDemonEntity>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.HURT_BY);
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
            MemoryModuleType.PATH, MemoryModuleType.LOOK_TARGET, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
            MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.HURT_BY,
            MemoryModuleType.NEAREST_ATTACKABLE,
            MemoryModuleType.ATTACK_TARGET, ModMemoryModuleTypes.LESSER_LASER_COOLDOWN.get(), ModMemoryModuleTypes.LESSER_LASER_SOUND_COOLDOWN.get(),
            ModMemoryModuleTypes.LESSER_LASER_SOUND_DELAY.get(), ModMemoryModuleTypes.LESSER_SLOW_COOLDOWN.get(), ModMemoryModuleTypes.LESSER_SLOW_SOUND_COOLDOWN.get(),
            ModMemoryModuleTypes.LESSER_SLOW_SOUND_DELAY.get());

    @Override
    protected Brain<?> makeBrain(Dynamic<?> pDynamic) {
        return LesserDemonAi.makeBrain(this, this.brainProvider().makeBrain(pDynamic));
    }

    @Override
    public Brain<LesserDemonEntity> getBrain() {
        return (Brain<LesserDemonEntity>) super.getBrain();
    }

    @Override
    protected Brain.Provider<LesserDemonEntity> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }


    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }


    @Override
    public void aiStep() {
        super.aiStep();
        if(this.entityData.get(DATA_IS_CHANNELING_SLOW)) {
            this.clientSideSlowTime++;
        }
        this.level.addParticle(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 0.5f, this.getZ(), 0.0f, 0.2f, 0.0f);

    }

    @Override
    protected void customServerAiStep() {
        this.getBrain().tick((ServerLevel) this.level, this);
        LesserDemonAi.updateActivity(this);
        super.customServerAiStep();
    }


    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, pLevel);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanFloat(true);
        flyingpathnavigation.setCanPassDoors(true);
        return flyingpathnavigation;
    }

    @Override
    public void travel(Vec3 pTravelVector) {
        if (this.isControlledByLocalInstance()) {
            if (this.isInWater()) {
                this.moveRelative(0.02F, pTravelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale((double) 0.8F));
            } else if (this.isInLava()) {
                this.moveRelative(0.02F, pTravelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
            } else {
                this.moveRelative(this.getSpeed(), pTravelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale((double) 0.91F));
            }
        }
    }

    @Override
    protected int calculateFallDamage(float pFallDistance, float pDamageMultiplier) {
        return 0;
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor pLevel, MobSpawnType pReason) {
        if (pLevel.getDifficulty() == Difficulty.PEACEFUL) return false;
        return super.checkSpawnRules(pLevel, pReason);
    }

    @Override
    protected void playStepSound(BlockPos pPos, BlockState pState) {
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        return super.doHurtTarget(pEntity);
    }

    public boolean canTargetEntity(@Nullable Entity entity) {
        if (entity instanceof LivingEntity livingentity) {
            if (this.level == entity.level  &&
                    !this.isAlliedTo(entity) && livingentity.getType() != EntityType.ARMOR_STAND &&
                    !livingentity.isInvulnerable() && !livingentity.isDeadOrDying() &&
                    EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity) &&
                    this.level.getWorldBorder().isWithinBounds(livingentity.getBoundingBox())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public LivingEntity getTarget() {
        return this.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse((LivingEntity)null);
    }


    public Optional<? extends LivingEntity> findNearestValidAttackTarget() {
        return this.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER);
    }

}
