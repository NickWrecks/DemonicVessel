package nickwrecks.demonicvessel.datagen;

import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.registries.RegistryObject;
import nickwrecks.demonicvessel.DemonicVessel;
import nickwrecks.demonicvessel.entity.ModEntityTypes;

import java.util.stream.Stream;

public class  ModEntityLootTableProvider extends EntityLootSubProvider {
    protected ModEntityLootTableProvider() {
        super(FeatureFlags.REGISTRY.allFlags());
    }


    @Override
    public void generate() {
            add(ModEntityTypes.LESSER_DEMON.get(), LootTable.lootTable());
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return DemonicVessel.ENTITY_TYPES.getEntries().stream().flatMap(RegistryObject::stream);
    }
}
