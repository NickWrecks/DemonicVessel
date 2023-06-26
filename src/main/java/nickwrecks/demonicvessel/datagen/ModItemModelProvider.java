package nickwrecks.demonicvessel.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import nickwrecks.demonicvessel.DemonicVessel;

public class ModItemModelProvider extends ItemModelProvider {
        public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
                super(output, DemonicVessel.MODID, existingFileHelper);
        }

        @Override
        protected void registerModels() {

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
