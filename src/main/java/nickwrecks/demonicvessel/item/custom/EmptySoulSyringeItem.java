package nickwrecks.demonicvessel.item.custom;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import nickwrecks.demonicvessel.entity.custom.LesserDemonEntity;
import nickwrecks.demonicvessel.item.ModItems;

public class EmptySoulSyringeItem extends Item {
    public EmptySoulSyringeItem(Properties pProperties) {
        super(new Properties().stacksTo(1));
    }


    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        if(pInteractionTarget instanceof LesserDemonEntity lesserDemonEntity) {
            if (lesserDemonEntity.getHealth() <= 10) {
                pStack.shrink(1);
                pPlayer.getLevel().playLocalSound(pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.HOSTILE, 0.5f, 1.0f, false);
                pInteractionTarget.remove(Entity.RemovalReason.KILLED);
                pPlayer.getLevel().addFreshEntity(new ItemEntity(pPlayer.getLevel(), pInteractionTarget.getX(),pInteractionTarget.getY(),pInteractionTarget.getZ(), ModItems.LESSER_SOUL_SYRINGE.get().getDefaultInstance()));
            }
        }

        return super.interactLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand);
    }
}
