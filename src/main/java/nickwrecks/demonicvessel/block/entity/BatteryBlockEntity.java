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
import net.minecraft.world.level.block.state.BlockState     ;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import nickwrecks.demonicvessel.block.BlockTools;
import nickwrecks.demonicvessel.energy.IRawDemonicEnergyStorage;
import nickwrecks.demonicvessel.energy.RawDemonicEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;



import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;
import static nickwrecks.demonicvessel.block.entity.ModBlockEntities.BATTERY_BLOCK_ENTITY;

public class BatteryBlockEntity extends BlockEntity {


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
            distributeEnergy();
    }




    private void distributeEnergy() {
        for (Direction direction : Direction.values()) {
            if (rawDemonicEnergyStorage.getEnergyStored() <= 0) {
                return;
            }
            BlockEntity be = level.getBlockEntity(getBlockPos().relative(direction));
            if (be != null  && (inputStatus[direction.get3DDataValue()]==2 || inputStatus[direction.get3DDataValue()]==3)) {
                be.getCapability(ENERGY_CAPABILITY,direction.getOpposite()).map(e -> {
                    if (e.canReceive()) {
                        int received = e.receiveEnergy(Math.min(rawDemonicEnergyStorage.getEnergyStored(), 100), false);
                        rawDemonicEnergyStorage.extractEnergy(received, false);
                        setChanged();
                        return received;
                    }
                    return 0;
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
            if (inputStatus[side.get3DDataValue()] == 1 || inputStatus[side.get3DDataValue()]==3)
                return rawDemonicEnergy.cast();
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
    private void loadClientData(CompoundTag tag) {
        inputStatus = tag.getIntArray("InputStatus");
    }
}
