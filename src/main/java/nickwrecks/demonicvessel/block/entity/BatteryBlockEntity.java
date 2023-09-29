package nickwrecks.demonicvessel.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import nickwrecks.demonicvessel.block.BlockTools;
import nickwrecks.demonicvessel.energy.IRawDemonicEnergyStorage;
import nickwrecks.demonicvessel.energy.RawDemonicEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;
import static nickwrecks.demonicvessel.block.entity.ModBlockEntities.BATTERY_BLOCK_ENTITY;
import static nickwrecks.demonicvessel.energy.RawDemonicEnergyStorage.ENERGY_CAPABILITY;

public class BatteryBlockEntity extends BlockEntity {

    private final Map<Direction, LazyOptional<IRawDemonicEnergyStorage>> energyCache = new EnumMap<>(Direction.class);

    public static final int BATTERY_CAPACITY = 100000;
    public Direction facing;
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

    private final RawDemonicEnergyStorage rawDemonicEnergyStorage = createEnergy();
    private final RawDemonicEnergyStorage outputEnergyStorage = new RawDemonicEnergyStorage(BATTERY_CAPACITY,100) {
        @Override
        public boolean canReceive() {
            return false;
        }

    };
    private final RawDemonicEnergyStorage viewOnlyEnergyStorage = new RawDemonicEnergyStorage(BATTERY_CAPACITY,100) {
        @Override
        public boolean canReceive() {
            return false;
        }

        @Override
        public boolean canExtract() {
            return false;
        }
    };
    private final LazyOptional<IRawDemonicEnergyStorage> rawDemonicEnergy = LazyOptional.of(() -> rawDemonicEnergyStorage);

    private final LazyOptional<IRawDemonicEnergyStorage> outputDemonicEnergy = LazyOptional.of(() -> outputEnergyStorage);

    private final LazyOptional<IRawDemonicEnergyStorage> viewOnlyDemonicEnergy = LazyOptional.of(() -> viewOnlyEnergyStorage);




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
        pTag.put("OutputEnergy", outputEnergyStorage.serializeNBT());
        pTag.put("ViewEnergy",viewOnlyEnergyStorage.serializeNBT());
        saveClientData(pTag);

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("Energy"))
            rawDemonicEnergyStorage.deserializeNBT(pTag.get("Energy"));
        if(pTag.contains("OutputEnergy"))
            outputEnergyStorage.deserializeNBT(pTag.get("OutputEnergy"));
        if(pTag.contains("ViewEnergy"))
            viewOnlyEnergyStorage.deserializeNBT(pTag.get("ViewEnergy"));
        loadClientData(pTag);
    }

    public void tick() {
        outputEnergyStorage.setEnergy(rawDemonicEnergyStorage.getEnergyStored());
        viewOnlyEnergyStorage.setEnergy(rawDemonicEnergyStorage.getEnergyStored());
        distributeEnergy();
        rawDemonicEnergyStorage.setEnergy(outputEnergyStorage.getEnergyStored());
        viewOnlyEnergyStorage.setEnergy(rawDemonicEnergyStorage.getEnergyStored());
    }




    private void distributeEnergy() {
        for (Direction dir : Direction.values()) {
            if (getLevel().getBlockEntity(getBlockPos().relative(dir)) != null && (inputStatus[dir.get3DDataValue()] == 2 || inputStatus[dir.get3DDataValue()] == 3)) {
                LazyOptional<IRawDemonicEnergyStorage> targetCapability = energyCache.get(dir);
                if (targetCapability == null) {
                    ICapabilityProvider provider = getLevel().getBlockEntity(getBlockPos().relative(dir));
                    targetCapability = provider.getCapability(ENERGY_CAPABILITY, dir.getOpposite());
                    energyCache.put(dir, targetCapability);
                    targetCapability.addListener(self -> energyCache.put(dir, null));
                }
                targetCapability.ifPresent(storage -> {
                    if (outputEnergyStorage.getEnergyStored() <= 0) return;
                    if (storage.canReceive()) {
                        int received = storage.receiveEnergy(Math.min(outputEnergyStorage.getEnergyStored(), 100), false);
                        outputEnergyStorage.extractEnergy(received, false);
                        setChanged();
                    }
                });
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
            if(side == null)  return viewOnlyDemonicEnergy.cast();
            else if (inputStatus[side.get3DDataValue()] == 1 || inputStatus[side.get3DDataValue()]==3)
                return rawDemonicEnergy.cast();
            else if(inputStatus[side.get3DDataValue()] == 2)
                return outputDemonicEnergy.cast();
        }

        return super.getCapability(cap, side);
    }


    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveClientData(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        if(tag!=null)
            loadClientData(tag);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        int[] oldInputStatus = new int[6];
        for(int i=0;i<=5;i++) oldInputStatus[i] = inputStatus[i];

        CompoundTag tag = pkt.getTag();
        if(tag!=null)
        handleUpdateTag(tag);

        if (oldInputStatus != inputStatus) {
            requestModelDataUpdate();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
        BlockTools.makeInputStatusAbsolute(facing, inputStatusForItem, inputStatus);

    }

    @Override
    public @NotNull ModelData getModelData() {
        requestModelDataUpdate();
        return ModelData.builder()
                .with(FACES_INPUT_STATUS, inputStatus)
                .build();
    }
    private void saveClientData(CompoundTag tag) {
        tag.putIntArray("InputStatus", inputStatus);
    }
    private void loadClientData(CompoundTag pTag) {
        if(pTag.contains("InputStatus")) {
            if(!((pTag.get("InputStatus")) instanceof IntArrayTag))
                throw new IllegalArgumentException("Cannot deserialize to a value that isnt the default implementation");
            else this.inputStatus = pTag.getIntArray("InputStatus");
        }
    }
}
