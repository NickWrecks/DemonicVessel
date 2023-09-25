package nickwrecks.demonicvessel.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import nickwrecks.demonicvessel.DemonicVessel;
import nickwrecks.demonicvessel.item.ModItems;

public class ModItemModelProvider extends ItemModelProvider {
        public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
                super(output, DemonicVessel.MODID, existingFileHelper);
        }

        @Override
        protected void registerModels() {
                withExistingParent(ModItems.GENERATOR_BLOCK_ITEM.getId().getPath(), modLoc("block/creative_generator_block"));
                withExistingParent(ModItems.BATTERY_BLOCK_ITEM.getId().getPath(), modLoc("block/battery_block"));
                withExistingParent(ModItems.CABLE_ITEM.getId().getPath(), modLoc("block/cable"));
                cubeAll(ModItems.ABBADONIUM_JACKET.getId().getPath(), modLoc("item/abbadonium_jacket"));
                simpleItem(ModItems.ABBADONIUM_INGOT);
                withExistingParent(ModItems.ABBADONIUM_BLOCK.getId().getPath(),modLoc("block/abbadonium_block"));
                simpleItem(ModItems.EMPTY_SOUL_SYRINGE);
                simpleItem(ModItems.LESSER_SOUL_SYRINGE);
                simpleItem(ModItems.ABBADONIUM_GEAR);
                withExistingParent(ModItems.LESSER_DEMON_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
                withExistingParent(ModItems.DISTILLATION_FEEDER.getId().getPath(),modLoc("block/distillation_feeder"));
                simpleItem(ModItems.EXPERIENCE_GEM);
        }

        private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
                return withExistingParent(item.getId().getPath(),
                        new ResourceLocation("item/generated")).texture("layer0",
                        new ResourceLocation(DemonicVessel.MODID, "item/" + item.getId().getPath()));
        }



        private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
                return withExistingParent(item.getId().getPath(),
                        new ResourceLocation("item/handheld")).texture("layer0",
                        new ResourceLocation(DemonicVessel.MODID, "item/" + item.getId().getPath()));
        }
}
