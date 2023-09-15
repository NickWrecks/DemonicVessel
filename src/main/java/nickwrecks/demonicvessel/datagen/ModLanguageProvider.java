package nickwrecks.demonicvessel.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;
import nickwrecks.demonicvessel.DemonicVessel;
import nickwrecks.demonicvessel.block.ModBlocks;
import nickwrecks.demonicvessel.entity.ModEntityTypes;
import nickwrecks.demonicvessel.item.ModItems;

public class ModLanguageProvider extends LanguageProvider{
    public ModLanguageProvider(PackOutput output, String locale) {
        super(output, DemonicVessel.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        add(ModBlocks.CREATIVE_GENERATOR_BLOCK.get(), "Creative Generator");
        add(ModBlocks.BATTERY_BLOCK.get(), "Unnatural Battery");
        add("demonicvessel.screen.battery", "Unnatural Battery");
        add("item_group.demonicvessel.everything", "Demonic Vessel");
        add(ModItems.ABBADONIUM_INGOT.get(), "Abbadonium Ingot");
        add(ModBlocks.ABBADONIUM_BLOCK.get(), "Block of Abbadonium");
        add(ModEntityTypes.LESSER_DEMON.get(), "Lesser Demon");
        add(ModItems.EMPTY_SOUL_SYRINGE.get(),"Empty Soul Syringe");
        add(ModItems.LESSER_SOUL_SYRINGE.get(), "Lesser Soul Syringe");
        add(ModItems.LESSER_DEMON_SPAWN_EGG.get(), "Lesser Demon Spawn Egg");
        add(ModItems.ABBADONIUM_GEAR.get(),"Abbadonium Gear");
        add(ModItems.ABBADONIUM_JACKET.get(), "Abbadonium Jacket");
        add(ModBlocks.FAMISHED_GENERATOR_BLOCK.get(),"Famished Generator");
    }
}
