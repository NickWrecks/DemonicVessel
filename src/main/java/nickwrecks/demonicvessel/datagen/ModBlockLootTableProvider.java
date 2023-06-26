package nickwrecks.demonicvessel.datagen;


import net.minecraft.data.loot.packs.VanillaBlockLoot;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import nickwrecks.demonicvessel.DemonicVessel;

import java.util.Set;

public class ModBlockLootTableProvider extends VanillaBlockLoot {


    @Override
    protected void generate() {

    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return DemonicVessel.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }

}
