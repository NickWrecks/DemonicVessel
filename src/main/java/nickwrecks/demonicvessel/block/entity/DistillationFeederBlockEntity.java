package nickwrecks.demonicvessel.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class DistillationFeederBlockEntity extends BlockEntity {
    protected Direction facing;
    public DistillationFeederBlockEntity( BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.DISTILLATION_FEEDER_BLOCK_ENTITY.get(), pPos, pBlockState);
        facing = pBlockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
    }
    public boolean isOverFire = false;
    private static final int SLOT_COUNT = 1;
    private final ItemStackHandler items = createItemHandler();

    private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> items);

    @Nonnull
    private ItemStackHandler createItemHandler() {
        return new ItemStackHandler(SLOT_COUNT) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
    }


    public void tickServer() {
        isOverFire = getLevel().getBlockState(getBlockPos().below()).getBlock() == Blocks.CAMPFIRE ||
                getLevel().getBlockState(getBlockPos().below()).getBlock() == Blocks.SOUL_CAMPFIRE;
        if(isOverFire && getLevel().getBlockEntity(getBlockPos().relative(facing)) !=null &&
                        !items.getStackInSlot(0).isEmpty()) {
            ItemStack food = items.getStackInSlot(0);
            if (getLevel().getBlockEntity(getBlockPos().relative(facing)) instanceof FamishedGeneratorBlockEntity blockEntity) {
                if (food.isEdible() && blockEntity.hunger + food.getFoodProperties(null).getNutrition() <= FamishedGeneratorBlockEntity.MAXIMUM_HUNGER) {
                    blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent((beItemHandler) -> {

                            beItemHandler.insertItem(0,items.getStackInSlot(0).copyWithCount(1),false);
                            items.extractItem(0,1,false);
                    });
                }

            }
        }
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap== ForgeCapabilities.ITEM_HANDLER)
            return itemHandler.cast();
        else return super.getCapability(cap,side);
    }

    @Override
    public void invalidateCaps() {
        itemHandler.invalidate();
        super.invalidateCaps();
    }
    public ItemStackHandler getItems() {
        return items;
    }

}
