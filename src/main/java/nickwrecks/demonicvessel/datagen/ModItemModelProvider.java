package nickwrecks.demonicvessel.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import nickwrecks.demonicvessel.DemonicVessel;
import nickwrecks.demonicvessel.block.ModBlocks;
import nickwrecks.demonicvessel.item.ModItems;

public class ModItemModelProvider extends ItemModelProvider {
        public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
                super(output, DemonicVessel.MODID, existingFileHelper);
        }

        @Override
        protected void registerModels() {
                withExistingParent(ModBlocks.CREATIVE_GENERATOR_BLOCK.getId().getPath(), modLoc("block/creative_generator_block"));
                withExistingParent(ModItems.BATTERY_BLOCK_ITEM.getId().getPath(), modLoc("block/battery_block"));
                simpleItem(ModItems.ABBADONIUM_INGOT);
                withExistingParent(ModItems.ABBADONIUM_BLOCK.getId().getPath(),modLoc("block/abbadonium_block"));
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
