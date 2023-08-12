package nickwrecks.demonicvessel.item.custom;

import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import nickwrecks.demonicvessel.block.entity.BatteryBlockEntity;
import nickwrecks.demonicvessel.energy.IRawDemonicEnergyStorage;
import nickwrecks.demonicvessel.energy.RawDemonicEnergyStorage;

import static nickwrecks.demonicvessel.block.custom.BatteryBlock.FACING;

public class BatteryBlockItem extends BlockItem {
    public BatteryBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }
    public static Capability<IRawDemonicEnergyStorage> ENERGY_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    private void processInputStatus(Direction facing, int[] processed, int[] unprocessed) {
        processed[Direction.UP.get3DDataValue()] = unprocessed[Direction.UP.get3DDataValue()];
        processed[Direction.DOWN.get3DDataValue()] = unprocessed[Direction.DOWN.get3DDataValue()];
        processed[facing.get3DDataValue()] = unprocessed[Direction.NORTH.get3DDataValue()];
        processed[facing.getOpposite().get3DDataValue()] = unprocessed[Direction.SOUTH.get3DDataValue()];
        processed[facing.getCounterClockWise().get3DDataValue()] = unprocessed[Direction.WEST.get3DDataValue()];
        processed[facing.getClockWise().get3DDataValue()] = unprocessed[Direction.EAST.get3DDataValue()];
    }
    public void getInputStatusForItem(Direction facing, int[] processed, int[] unprocessed) {
        processed[Direction.UP.get3DDataValue()]= unprocessed[Direction.UP.get3DDataValue()];
        processed[Direction.DOWN.get3DDataValue()]= unprocessed[Direction.DOWN.get3DDataValue()];
        processed[Direction.NORTH.get3DDataValue()]= unprocessed[facing.get3DDataValue()];
        processed[Direction.SOUTH.get3DDataValue()] = unprocessed[facing.getOpposite().get3DDataValue()];
        processed[Direction.WEST.get3DDataValue()] =unprocessed[facing.getCounterClockWise().get3DDataValue()];
        processed[Direction.EAST.get3DDataValue()] =unprocessed[facing.getClockWise().get3DDataValue()];

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
        processInputStatus(pState.getValue(FACING), inputStatus,inputStatusUnprocessed);
        blockEntity.inputStatus = inputStatus;
        getInputStatusForItem(pState.getValue(FACING), blockEntity.inputStatusForItem, inputStatus);
        return success;
    }
}
