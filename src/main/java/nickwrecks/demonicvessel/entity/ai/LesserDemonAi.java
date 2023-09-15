package nickwrecks.demonicvessel.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.warden.SonicBoom;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.FrogAi;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.monster.warden.WardenAi;
import net.minecraft.world.entity.schedule.Activity;
import nickwrecks.demonicvessel.entity.ai.behaviour.LesserLaser;
import nickwrecks.demonicvessel.entity.ai.behaviour.LesserSlow;
import nickwrecks.demonicvessel.entity.custom.LesserDemonEntity;

public class LesserDemonAi {

    public static Brain<?> makeBrain(LesserDemonEntity lesserDemonEntity, Brain<LesserDemonEntity> pBrain) {
        initCoreActivity(pBrain);
        initIdleActivity(pBrain);
        initFightActivity(lesserDemonEntity,pBrain);
        pBrain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        pBrain.setDefaultActivity(Activity.IDLE);
        pBrain.useDefaultActivity();
        return pBrain;
    }
    private static void initCoreActivity(Brain<LesserDemonEntity> pBrain) {
        pBrain.addActivity(Activity.CORE, 0,
                ImmutableList.of(new Swim(0.8F),
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink()));

    }
    private static void initIdleActivity(Brain<LesserDemonEntity> pBrain) {
        pBrain.addActivity(Activity.IDLE,ImmutableList.of(
                Pair.of(1,StartAttacking.create(LesserDemonEntity::findNearestValidAttackTarget)),
                Pair.of(2, new RunOne<>(ImmutableList.of(
                        Pair.of(RandomStroll.fly(1.0f),2),
                        Pair.of(SetWalkTargetFromLookTarget.create(1.0f,3),1),
                        Pair.of(new DoNothing(30,60),4))))
                ));
    }
    private static void initFightActivity(LesserDemonEntity lesserDemonEntity, Brain<LesserDemonEntity> pBrain) {
        pBrain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 5, ImmutableList.of(SetEntityLookTarget.create((pEntity) -> {
            return isTarget(lesserDemonEntity, pEntity);
        },(float) lesserDemonEntity.getAttributeValue(Attributes.FOLLOW_RANGE)),
                        BackUpIfTooClose.create(2,1.0f), new LesserLaser(), new LesserSlow(),
                StopAttackingIfTargetInvalid.create((entity) -> !lesserDemonEntity.canTargetEntity(entity)),
                new RunOne<>(ImmutableList.of(
                Pair.of(RandomStroll.fly(1.0f),2),
                Pair.of(SetWalkTargetFromLookTarget.create(1.0f,10),1)))),
                MemoryModuleType.ATTACK_TARGET);
    }


    public static void updateActivity(LesserDemonEntity lesserDemonEntity) {
        lesserDemonEntity.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT,Activity.IDLE));
    }
    private static boolean isTarget(LesserDemonEntity lesserDemonEntity, LivingEntity pEntity) {
        return lesserDemonEntity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).filter((pComparingEntity) -> {
            return pComparingEntity == pEntity;
        }).isPresent();
    }

}
