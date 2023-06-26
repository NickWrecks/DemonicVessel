package nickwrecks.demonicvessel.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import nickwrecks.demonicvessel.DemonicVessel;

public class ModBlockstateProvider extends BlockStateProvider {
    public ModBlockstateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, DemonicVessel.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

    }
}
