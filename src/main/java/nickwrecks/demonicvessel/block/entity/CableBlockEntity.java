package nickwrecks.demonicvessel.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;

public class CableBlockEntity extends BlockEntity {
    public CableBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.CABLE_BLOCK_ENTITY.get(), pPos, pBlockState);
    }
    public int[] connectionStatus = new int[6];
    public static final ModelProperty<int[]> FACES_CONNECTION_STATUS = new ModelProperty<>();

    @Override
    public @NotNull ModelData getModelData() {
        requestModelDataUpdate();
        return ModelData.builder()
                .with(FACES_CONNECTION_STATUS,connectionStatus)
                .build();
    }
}
