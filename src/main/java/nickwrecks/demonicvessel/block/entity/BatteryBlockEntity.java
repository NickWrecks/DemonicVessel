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


import java.util.concurrent.atomic.AtomicInteger;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;
import static nickwrecks.demonicvessel.block.entity.ModBlockEntities.BATTERY_BLOCK_ENTITY;

public class    BatteryBlockEntity extends BlockEntity {


    public static final int BATTERY_CAPACITY = 10000;
    protected Direction facing;
    ///D-U-N-S-W-E
    public int[] inputStatusForItem = new int[6];
    public int[] inputStatus = new int[6];

    public BatteryBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BATTERY_BLOCK_ENTITY.get(), pPos, pBlockState);
        facing = pBlockState.getValue(HORIZONTAL_FACING);
    }

    public int getStoredEnergy() {
        return rawDemonicEnergyStorage.getEnergyStored();
    }
    public void addEnergy(int addedEnergy) {
        rawDemonicEnergyStorage.addEnergy(addedEnergy);
    }


    public static final ModelProperty<int[]> FACES_INPUT_STATUS = new ModelProperty<>();





    private int counter;
    private final RawDemonicEnergyStorage rawDemonicEnergyStorage = createEnergy();
    private final LazyOptional<IRawDemonicEnergyStorage> rawDemonicEnergy = LazyOptional.of(() -> rawDemonicEnergyStorage);

    public static Capability<IRawDemonicEnergyStorage> ENERGY_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });


    private RawDemonicEnergyStorage createEnergy() {
        return new RawDemonicEnergyStorage(BATTERY_CAPACITY, 100) {
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
            setChanged();
            sendOutPower();
    }



    private void sendOutPower() {
        AtomicInteger capacity = new AtomicInteger(rawDemonicEnergyStorage.getEnergyStored());
        if (capacity.get() > 0) {
       for(Direction dir : Direction.values()) {
            BlockEntity be = level.getBlockEntity(worldPosition.relative(dir));
            if (be != null && (inputStatus[dir.get3DDataValue()]==2 || inputStatus[dir.get3DDataValue()]==4)) {
                boolean doContinue = be.getCapability(ENERGY_CAPABILITY, facing.getOpposite()).map(handler -> {
                            if (handler.canReceive()) {
                                int received = handler.receiveEnergy(Math.min(capacity.get(), 100), false);
                                capacity.addAndGet(-received);
                                rawDemonicEnergyStorage.consumeEnergy(received);
                                setChanged();
                                return capacity.get() > 0;
                            } else {
                                return true;
                            }
                        }
                ).orElse(true);
                if (!doContinue) {
                    return;
                }
            }
            }
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
            if (inputStatus[side.get3DDataValue()] == 1 || inputStatus[side.get3DDataValue()]==4)
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
        getInputStatusForItem(facing, inputStatusForItem, inputStatus);
    }

    @Override
    public @NotNull ModelData getModelData() {
        requestModelDataUpdate();
        return ModelData.builder()
                .with(FACES_INPUT_STATUS, inputStatus)
                .build();

    }

    public void getInputStatusForItem(Direction facing, int[] processed, int[] unprocessed) {
        processed[Direction.UP.get3DDataValue()]= unprocessed[Direction.UP.get3DDataValue()];
        processed[Direction.DOWN.get3DDataValue()]= unprocessed[Direction.DOWN.get3DDataValue()];
        processed[Direction.NORTH.get3DDataValue()]= unprocessed[facing.get3DDataValue()];
        processed[Direction.SOUTH.get3DDataValue()] = unprocessed[facing.getOpposite().get3DDataValue()];
        processed[Direction.WEST.get3DDataValue()] =unprocessed[facing.getCounterClockWise().get3DDataValue()];
        processed[Direction.EAST.get3DDataValue()] =unprocessed[facing.getClockWise().get3DDataValue()];

    }
}
