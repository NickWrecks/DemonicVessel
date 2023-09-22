package nickwrecks.demonicvessel.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import nickwrecks.demonicvessel.energy.IRawDemonicEnergyStorage;
import nickwrecks.demonicvessel.energy.RawDemonicEnergyStorage;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;

public class CreativeGeneratorBlockEntity extends BlockEntity{
    protected final int BASE_PROCESS_TICK = 20;
    protected int processTick = BASE_PROCESS_TICK;

    protected Direction facing;

    private final RawDemonicEnergyStorage energyStorage = createEnergy();
    private final LazyOptional<IRawDemonicEnergyStorage> energy = LazyOptional.of(() -> energyStorage);
    public static Capability<IRawDemonicEnergyStorage> ENERGY_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});

    public CreativeGeneratorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.CREATIVE_GENERATOR_BLOCK_ENTITY.get(), pPos, pBlockState);
        facing = pBlockState.getValue(FACING);
    }
    private RawDemonicEnergyStorage createEnergy() {
        return new RawDemonicEnergyStorage(1000, 0) {
            @Override
            protected void onEnergyChanged() {
                setChanged();
            }
        };
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        energy.invalidate();
    }
    public void tickServer() {
        energyStorage.addEnergy(100);
        sendOutPower();
    }

    private void sendOutPower() {
        AtomicInteger capacity = new AtomicInteger(energyStorage.getEnergyStored());
        if (capacity.get() > 0) {

                BlockEntity be = level.getBlockEntity(worldPosition.relative(facing));
                if (be != null) {
                    boolean doContinue = be.getCapability(ENERGY_CAPABILITY, facing.getOpposite()).map(handler -> {
                                if (handler.canReceive()) {
                                    int received = handler.receiveEnergy(Math.min(capacity.get(), 100), false);
                                    capacity.addAndGet(-received);
                                    energyStorage.consumeEnergy(received);
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

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {

        if (cap == ENERGY_CAPABILITY) {
            return energy.cast();
        }
        return super.getCapability(cap);
    }
}
