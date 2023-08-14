package nickwrecks.demonicvessel.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import nickwrecks.demonicvessel.block.BlockTools;
import nickwrecks.demonicvessel.block.entity.BatteryBlockEntity;
import nickwrecks.demonicvessel.energy.IRawDemonicEnergyStorage;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static nickwrecks.demonicvessel.block.custom.BatteryBlock.FACING;

public class BatteryBlockItem extends BlockItem {
    public BatteryBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }
    public static Capability<IRawDemonicEnergyStorage> ENERGY_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });


    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        int itemEnergy;
        if(pStack.getTag()==null || !pStack.getTag().contains("Energy")) itemEnergy = 0;
        else itemEnergy = pStack.getTag().getInt("Energy");
        pTooltip.add(Component.literal("RDE: " + itemEnergy + "/" + BatteryBlockEntity.BATTERY_CAPACITY));
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext pContext, BlockState pState) {
         int[] inputStatusUnprocessed = new int[6];
        boolean success = pContext.getLevel().setBlock(pContext.getClickedPos(), pState, 11);
        BatteryBlockEntity blockEntity = (BatteryBlockEntity) pContext.getLevel().getBlockEntity(pContext.getClickedPos());
        ItemStack itemStack = pContext.getItemInHand();
        if(itemStack.getTag()==null || itemStack.getTag().getIntArray("InputStatus").length==0) {
            for(int i = 0;i<6;i++) inputStatusUnprocessed[i] = 0;
        }
        else inputStatusUnprocessed = itemStack.getTag().getIntArray("InputStatus");
        if(itemStack.getTag() != null) {
            if(itemStack.getTag().contains("Energy"))
            blockEntity.addEnergy(itemStack.getTag().getInt("Energy"));
        }
        int[] inputStatus = new int[6];
        BlockTools.makeInputStatusRelative(pState.getValue(FACING), inputStatus,inputStatusUnprocessed);
        for(int i = 0;i<=5; i++) blockEntity.inputStatus[i] = inputStatus[i];
        BlockTools.makeInputStatusAbsolute(pState.getValue(FACING), blockEntity.inputStatusForItem, inputStatus);
        return success;
    }
}
