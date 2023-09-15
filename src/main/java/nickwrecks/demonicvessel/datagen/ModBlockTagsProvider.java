package nickwrecks.demonicvessel.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import nickwrecks.demonicvessel.DemonicVessel;
import nickwrecks.demonicvessel.block.ModBlocks;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {

    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, DemonicVessel.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.BATTERY_BLOCK.get())
                .add(ModBlocks.ABBADONIUM_BLOCK.get())
                .add(ModBlocks.FAMISHED_GENERATOR_BLOCK.get());
        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.ABBADONIUM_BLOCK.get())
                .add(ModBlocks.BATTERY_BLOCK.get())
                .add(ModBlocks.FAMISHED_GENERATOR_BLOCK.get());

    }
}
