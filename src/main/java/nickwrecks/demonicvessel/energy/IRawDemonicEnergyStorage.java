package nickwrecks.demonicvessel.energy;


import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface IRawDemonicEnergyStorage {
    /*This is instead of IEnergyStorage, the default forge capability.
    I do this for the sake of avoiding compatibility, which would break
    the balance of this and other mods.
     */

    int receiveEnergy(int maxReceive, boolean simulate);
    int extractEnergy(int maxExtract, boolean simulate);


    int getEnergyStored();

    int getMaxEnergyStored();
    boolean canExtract();

    boolean canReceive();

}
