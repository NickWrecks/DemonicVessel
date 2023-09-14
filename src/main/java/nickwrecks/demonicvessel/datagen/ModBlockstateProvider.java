package nickwrecks.demonicvessel.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import nickwrecks.demonicvessel.DemonicVessel;
import nickwrecks.demonicvessel.block.ModBlocks;

import static nickwrecks.demonicvessel.client.BatteryModelLoader.BATTERY_LOADER;

public class ModBlockstateProvider extends BlockStateProvider {
    public ModBlockstateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, DemonicVessel.MODID, exFileHelper);
    }

    ResourceLocation creativeGeneratorSide = new ResourceLocation(DemonicVessel.MODID, "block/creative_generator_side");
    ResourceLocation creativeGeneratorFront = new ResourceLocation(DemonicVessel.MODID, "block/creative_generator_front");

    @Override
    protected void registerStatesAndModels() {
        BlockModelBuilder modelCreativeGenerator = models().cube("creative_generator_block", creativeGeneratorSide,creativeGeneratorFront,creativeGeneratorSide,creativeGeneratorSide,creativeGeneratorSide,creativeGeneratorSide);
        directionalBlock(ModBlocks.CREATIVE_GENERATOR_BLOCK.get(), modelCreativeGenerator);
        registerBattery();
        simpleBlock(ModBlocks.ABBADONIUM_BLOCK.get());
    }

    private void registerBattery(){
        BlockModelBuilder batteryModel = models().getBuilder(ModBlocks.BATTERY_BLOCK.getId().getPath())
                .parent(models().getExistingFile(mcLoc("cube")))
                .customLoader((blockModelBuilder, helper) -> new CustomLoaderBuilder<BlockModelBuilder>(BATTERY_LOADER,blockModelBuilder,helper) {})
                .end();
        simpleBlock(ModBlocks.BATTERY_BLOCK.get(),batteryModel);
    }
}
