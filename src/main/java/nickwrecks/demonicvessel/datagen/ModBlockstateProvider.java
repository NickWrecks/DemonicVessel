package nickwrecks.demonicvessel.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.util.TransformationHelper;
import nickwrecks.demonicvessel.DemonicVessel;
import nickwrecks.demonicvessel.block.ModBlocks;

import static nickwrecks.demonicvessel.client.BatteryModelLoader.BATTERY_LOADER;
import static nickwrecks.demonicvessel.client.CableModelLoader.CABLE_LOADER;

public class ModBlockstateProvider extends BlockStateProvider {
    private ExistingFileHelper existingFileHelper;
    public ModBlockstateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, DemonicVessel.MODID, exFileHelper);
        existingFileHelper = exFileHelper;
    }

    ResourceLocation creativeGeneratorSide = new ResourceLocation(DemonicVessel.MODID, "block/creative_generator_side");
    ResourceLocation creativeGeneratorFront = new ResourceLocation(DemonicVessel.MODID, "block/creative_generator_front");

    @Override
    protected void registerStatesAndModels() {
        BlockModelBuilder modelCreativeGenerator = models().cube("creative_generator_block", creativeGeneratorSide,creativeGeneratorFront,creativeGeneratorSide,creativeGeneratorSide,creativeGeneratorSide,creativeGeneratorSide);
        directionalBlock(ModBlocks.CREATIVE_GENERATOR_BLOCK.get(), modelCreativeGenerator);
        horizontalBlock(ModBlocks.FAMISHED_GENERATOR_BLOCK.get(), new ModelFile.ExistingModelFile(new ResourceLocation(DemonicVessel.MODID,"block/famished_generator"), existingFileHelper));
        registerBattery();
        simpleBlock(ModBlocks.ABBADONIUM_BLOCK.get());
        horizontalBlock(ModBlocks.DISTILLATION_FEEDER_BLOCK.get(), new ModelFile.ExistingModelFile(new ResourceLocation(DemonicVessel.MODID,"block/distillation_feeder"),existingFileHelper));
        registerCable();
    }

    private void registerBattery(){
        BlockModelBuilder batteryModel = models().getBuilder(ModBlocks.BATTERY_BLOCK.getId().getPath())
                .parent(models().getExistingFile(mcLoc("cube")))
                .customLoader((blockModelBuilder, helper) -> new CustomLoaderBuilder<BlockModelBuilder>(BATTERY_LOADER,blockModelBuilder,helper) {})
                .end();
        simpleBlock(ModBlocks.BATTERY_BLOCK.get(),batteryModel);
    }

    private void registerCable() {
        BlockModelBuilder cableModel = models().getBuilder(ModBlocks.CABLE.getId().getPath())
                .parent(models().getExistingFile(mcLoc("cube")))
                .customLoader((blockModelBuilder, helper) -> new CustomLoaderBuilder<BlockModelBuilder>(CABLE_LOADER,blockModelBuilder,helper) {})
                .end();
        simpleBlock(ModBlocks.CABLE.get(),cableModel);
    }
}
