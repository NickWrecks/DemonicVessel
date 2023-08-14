package nickwrecks.demonicvessel.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;
import nickwrecks.demonicvessel.DemonicVessel;
import nickwrecks.demonicvessel.block.ModBlocks;

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
    }
}
