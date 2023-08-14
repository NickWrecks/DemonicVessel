package nickwrecks.demonicvessel.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.network.NetworkEvent;
import nickwrecks.demonicvessel.block.BlockTools;
import nickwrecks.demonicvessel.block.entity.BatteryBlockEntity;

import java.util.function.Supplier;

public class BatteryConfigToServer {
    private final BlockPos pos;
    private final int[] config;

    public BatteryConfigToServer(int[] config, BlockPos pos) {
        this.config = new int[6];
        for(int i =0; i<=5; i++)
        {
          this.config[i] = config[i];
        }
        this.pos = pos;
    }
    public BatteryConfigToServer(FriendlyByteBuf buf) {
        config = buf.readVarIntArray();
        pos = buf.readBlockPos();
    }
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeVarIntArray(config);
        buf.writeBlockPos(pos);
    }
    private void processInputStatus(Direction facing, int[] processed, int[] unprocessed) {
        processed[Direction.UP.get3DDataValue()] = unprocessed[Direction.UP.get3DDataValue()];
        processed[Direction.DOWN.get3DDataValue()] = unprocessed[Direction.DOWN.get3DDataValue()];
        processed[facing.get3DDataValue()] = unprocessed[Direction.NORTH.get3DDataValue()];
        processed[facing.getOpposite().get3DDataValue()] = unprocessed[Direction.SOUTH.get3DDataValue()];
        processed[facing.getCounterClockWise().get3DDataValue()] = unprocessed[Direction.WEST.get3DDataValue()];
        processed[facing.getClockWise().get3DDataValue()] = unprocessed[Direction.EAST.get3DDataValue()];
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(()-> {
           if(context.getSender().getLevel().getBlockEntity(pos) instanceof  BatteryBlockEntity blockEntity) {
                processInputStatus(blockEntity.facing,blockEntity.inputStatus,this.config);
                BlockTools.makeInputStatusAbsolute(blockEntity.facing,blockEntity.inputStatusForItem, blockEntity.inputStatus);
                blockEntity.requestModelDataUpdate();
                context.getSender().getLevel().sendBlockUpdated(pos, blockEntity.getBlockState(),blockEntity.getBlockState(), Block.UPDATE_ALL);
            }
        });
        return true;
    }
}
