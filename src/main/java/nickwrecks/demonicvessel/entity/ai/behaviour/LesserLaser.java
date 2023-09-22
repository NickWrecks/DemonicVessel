package nickwrecks.demonicvessel.entity.ai.behaviour;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import nickwrecks.demonicvessel.client.ModSounds;
import nickwrecks.demonicvessel.entity.custom.LesserDemonEntity;

public class LesserLaser extends Behavior<LesserDemonEntity> {

    private static final int DURATION = Mth.ceil(80.0F);

    private static final int TICKS_BEFORE_PLAYING_SOUND = Mth.ceil(60.0D);

    public LesserLaser() {
        super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT,
                ModMemoryModuleTypes.LESSER_LASER_COOLDOWN.get(), MemoryStatus.VALUE_ABSENT,
                ModMemoryModuleTypes.LESSER_LASER_SOUND_COOLDOWN.get(),MemoryStatus.REGISTERED,
                ModMemoryModuleTypes.LESSER_LASER_SOUND_DELAY.get(),MemoryStatus.REGISTERED),DURATION);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel pLevel, LesserDemonEntity pOwner) {
        return pOwner.closerThan(pOwner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get(), 15.0D, 20.0D);
    }

    @Override
    protected boolean canStillUse(ServerLevel pLevel, LesserDemonEntity pEntity, long pGameTime) {
        return true;
    }

    @Override
    protected void start(ServerLevel pLevel, LesserDemonEntity pEntity, long pGameTime) {
        pEntity.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_COOLING_DOWN, true, (long) DURATION);
        pEntity.getBrain().setMemoryWithExpiry(ModMemoryModuleTypes.LESSER_LASER_SOUND_DELAY.get(), Unit.INSTANCE, (long) TICKS_BEFORE_PLAYING_SOUND);
        pEntity.playSound(ModSounds.LESSER_WHISPER.get(), 1.0F, 1.0f);
    }

    @Override
    protected void tick(ServerLevel pLevel, LesserDemonEntity pOwner, long pGameTime) {
        pOwner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).ifPresent((pEntity) -> {
            pOwner.getLookControl().setLookAt(pEntity.position());
        });
        if (!pOwner.getBrain().hasMemoryValue(ModMemoryModuleTypes.LESSER_LASER_SOUND_DELAY.get()) && !pOwner.getBrain().hasMemoryValue(ModMemoryModuleTypes.LESSER_LASER_SOUND_COOLDOWN.get())) {
            pOwner.getBrain().setMemoryWithExpiry(ModMemoryModuleTypes.LESSER_LASER_SOUND_COOLDOWN.get(), Unit.INSTANCE, (long)(DURATION - TICKS_BEFORE_PLAYING_SOUND));
            pOwner.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).filter(pOwner::canTargetEntity).filter((livingEntity) -> {
                return pOwner.closerThan(livingEntity, 15.0D, 20.0D);
            }).ifPresentOrElse((livingEntity) -> {

                pOwner.setActiveAttackTarget(livingEntity.getId());

                pOwner.playSound(ModSounds.LESSER_IMPACT.get(), 0.5F, 0.8F);
                livingEntity.hurt(pLevel.m_269111_().m_269548_(), 8.0F);
            }, () -> pOwner.setActiveAttackTarget(0));
        }
        else pOwner.setActiveAttackTarget(0);
    }


    @Override
    protected void stop(ServerLevel pLevel, LesserDemonEntity pEntity, long pGameTime) {
        setCooldown(pEntity,120);
    }

    public static void setCooldown(LivingEntity pEntity, int pCooldown) {
        pEntity.getBrain().setMemoryWithExpiry(ModMemoryModuleTypes.LESSER_LASER_COOLDOWN.get(), Unit.INSTANCE, (long)pCooldown);
    }
}
