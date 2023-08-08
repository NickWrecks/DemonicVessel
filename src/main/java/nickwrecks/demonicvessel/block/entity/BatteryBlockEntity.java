package nickwrecks.demonicvessel.block.entity;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState     ;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import nickwrecks.demonicvessel.energy.IRawDemonicEnergyStorage;
import nickwrecks.demonicvessel.energy.RawDemonicEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;


import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;
import static nickwrecks.demonicvessel.block.entity.ModBlockEntities.BATTERY_BLOCK_ENTITY;

public class    BatteryBlockEntity extends BlockEntity {


    protected Direction facing;
    ///D-U-N-S-W-E
    int[] inputStatusUnprocessed = {0,0,1,0,0,0};
    int[] inputStatus = new int[6];

    public BatteryBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BATTERY_BLOCK_ENTITY.get(), pPos, pBlockState);
        facing = pBlockState.getValue(HORIZONTAL_FACING);
        processInputStatus(facing);
    }

    private void processInputStatus(Direction facing) {

        inputStatus[facing.get3DDataValue()] = inputStatusUnprocessed[2];
        inputStatus[Direction.UP.get3DDataValue()] = inputStatusUnprocessed[Direction.UP.get3DDataValue()];
        inputStatus[Direction.DOWN.get3DDataValue()] = inputStatusUnprocessed[Direction.DOWN.get3DDataValue()];
        inputStatus[facing.getOpposite().get3DDataValue()] = inputStatusUnprocessed[3];
        switch (facing) {
            case NORTH:
                inputStatus[facing.getClockWise().get3DDataValue()] = inputStatusUnprocessed[4];
                inputStatus[facing.getCounterClockWise().get3DDataValue()] = inputStatusUnprocessed[5];
            case SOUTH:
                inputStatus[facing.getClockWise().get3DDataValue()] = inputStatusUnprocessed[4];
                inputStatus[facing.getCounterClockWise().get3DDataValue()] = inputStatusUnprocessed[5];
            case WEST:
                inputStatus[facing.getClockWise().get3DDataValue()] = inputStatusUnprocessed[4];
                inputStatus[facing.getCounterClockWise().get3DDataValue()] = inputStatusUnprocessed[5];
            case EAST:
                inputStatus[facing.getClockWise().get3DDataValue()] = inputStatusUnprocessed[4];
                inputStatus[facing.getCounterClockWise().get3DDataValue()] = inputStatusUnprocessed[5];

        }

    }


    public static final ModelProperty<int[]> FACES_INPUT_STATUS = new ModelProperty<>();





    private int counter;
    private static final Logger LOGGER = LogUtils.getLogger();
    private final RawDemonicEnergyStorage rawDemonicEnergyStorage = createEnergy();
    private final LazyOptional<IRawDemonicEnergyStorage> rawDemonicEnergy = LazyOptional.of(() -> rawDemonicEnergyStorage);

    public static Capability<IRawDemonicEnergyStorage> ENERGY_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });


    private RawDemonicEnergyStorage createEnergy() {
        return new RawDemonicEnergyStorage(10000, 100) {
            @Override
            protected void onEnergyChanged() {
                setChanged();
            }
        };
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("Energy", rawDemonicEnergyStorage.serializeNBT());
        pTag.put("InputStatus", new IntArrayTag(inputStatus));
        CompoundTag infoTag = new CompoundTag();
        infoTag.putInt("Counter", counter);

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("Energy"))
            rawDemonicEnergyStorage.deserializeNBT(pTag.get("Energy"));
        if(pTag.contains("InputStatus")) {
            if(!((pTag.get("InputStatus")) instanceof IntArrayTag))
                throw new IllegalArgumentException("Cannot deserialize to a value that isnt the default implementation");
            else this.inputStatus = pTag.getIntArray("InputStatus");
        }
    }

    public void tick() {
        if (counter > 0) {
            counter--;
            setChanged();
        }
        if (counter <= 0) {
            System.out.println(rawDemonicEnergyStorage.getEnergyStored());
            counter = 100;
            for(int i=0;i<6;i++)
                System.out.print(this.inputStatus[i]);
            setChanged();
        }

    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        rawDemonicEnergy.invalidate();
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ENERGY_CAPABILITY) {
            if (inputStatus[side.get3DDataValue()] == 1)
                return rawDemonicEnergy.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        return tag;
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        int[] oldInputStatus = inputStatus;

        CompoundTag tag = pkt.getTag();
        handleUpdateTag(tag);

        if (oldInputStatus != inputStatus) {
            requestModelDataUpdate();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    @Override
    public @NotNull ModelData getModelData() {
        return ModelData.builder()
                .with(FACES_INPUT_STATUS, inputStatus)
                .build();
    }
}
