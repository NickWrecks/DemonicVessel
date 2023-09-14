package nickwrecks.demonicvessel.datagen;


import net.minecraft.data.loot.packs.VanillaBlockLoot;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import nickwrecks.demonicvessel.DemonicVessel;
import nickwrecks.demonicvessel.block.ModBlocks;


public class ModBlockLootTableProvider extends VanillaBlockLoot {


    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.CREATIVE_GENERATOR_BLOCK.get());
        this.add(ModBlocks.BATTERY_BLOCK.get(),noDrop());
        this.dropSelf(ModBlocks.ABBADONIUM_BLOCK.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return DemonicVessel.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }

}
