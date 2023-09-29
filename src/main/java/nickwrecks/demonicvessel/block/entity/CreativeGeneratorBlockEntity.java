package nickwrecks.demonicvessel.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import nickwrecks.demonicvessel.energy.IRawDemonicEnergyStorage;
import nickwrecks.demonicvessel.energy.RawDemonicEnergyStorage;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;
import static nickwrecks.demonicvessel.energy.RawDemonicEnergyStorage.ENERGY_CAPABILITY;

public class CreativeGeneratorBlockEntity extends BlockEntity{
    protected final int BASE_PROCESS_TICK = 20;
    protected int processTick = BASE_PROCESS_TICK;

    protected Direction facing;

    private final Map<Direction, LazyOptional<IRawDemonicEnergyStorage>> energyCache = new EnumMap<>(Direction.class);
    private final RawDemonicEnergyStorage energyStorage = createEnergy();
    private final LazyOptional<IRawDemonicEnergyStorage> energy = LazyOptional.of(() -> energyStorage);

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

            @Override
            public boolean canReceive() {
                return false;
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
        distributeEnergy();
    }


    private void distributeEnergy() {
        Direction dir  = facing;
        LazyOptional<IRawDemonicEnergyStorage> targetCapability = energyCache.get(dir);
        if(getLevel().getBlockEntity(getBlockPos().relative(dir)) != null) {
            if (targetCapability == null) {
                ICapabilityProvider provider = getLevel().getBlockEntity(getBlockPos().relative(dir));
                targetCapability = provider.getCapability(ENERGY_CAPABILITY, dir.getOpposite());
                energyCache.put(dir, targetCapability);
                targetCapability.addListener(self -> energyCache.put(dir, null));
            }
            targetCapability.ifPresent(storage -> {
                if (energyStorage.getEnergyStored() <= 0) return;
                if (storage.canReceive()) {
                    int received = storage.receiveEnergy(Math.min(energyStorage.getEnergyStored(), 100), false);
                    energyStorage.extractEnergy(received, false);
                    setChanged();
                }
            });
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
