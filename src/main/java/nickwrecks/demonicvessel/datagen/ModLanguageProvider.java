package nickwrecks.demonicvessel.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;
import nickwrecks.demonicvessel.DemonicVessel;

public class ModLanguageProvider extends LanguageProvider{
    public ModLanguageProvider(PackOutput output, String locale) {
        super(output, DemonicVessel.MODID, locale);
    }

    @Override
    protected void addTranslations() {

    }
}
