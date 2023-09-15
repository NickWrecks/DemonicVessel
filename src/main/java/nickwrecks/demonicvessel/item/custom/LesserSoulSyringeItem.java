package nickwrecks.demonicvessel.item.custom;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import nickwrecks.demonicvessel.block.ModBlocks;

public class LesserSoulSyringeItem extends Item {

    public LesserSoulSyringeItem(Properties pProperties) {
        super(new Item.Properties().stacksTo(1));
    }


    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 1000;
    }
    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        if(pLivingEntity instanceof Player player) {
            BlockHitResult block = (BlockHitResult) player.pick(20.0d,0.0f,false);
            if(pLevel.getBlockState(block.getBlockPos()).getBlock() == Blocks.COPPER_BLOCK
            && this.getUseDuration(pStack) - pTimeCharged >= 32) {
                pLevel.setBlockAndUpdate(block.getBlockPos(), ModBlocks.ABBADONIUM_BLOCK.get().defaultBlockState());
                pStack.shrink(1);
                pLevel.playLocalSound(block.getBlockPos(), SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS,0.5f,1.0f,false);
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        pPlayer.startUsingItem(pUsedHand);
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
