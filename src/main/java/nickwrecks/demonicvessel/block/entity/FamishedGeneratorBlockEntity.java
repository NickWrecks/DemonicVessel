package nickwrecks.demonicvessel.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FamishedGeneratorBlockEntity extends BlockEntity {

    public int time;
    public FamishedGeneratorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.FAMISHED_GENERATOR_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    public void tickClient() {
        time++;
    }
}
