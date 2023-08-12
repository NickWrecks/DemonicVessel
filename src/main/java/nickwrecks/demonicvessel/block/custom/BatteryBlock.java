package nickwrecks.demonicvessel.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import nickwrecks.demonicvessel.block.entity.BatteryBlockEntity;
import nickwrecks.demonicvessel.client.screen.BatteryMenu;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BatteryBlock extends BaseEntityBlock {



    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BatteryBlock(Properties pProperties) {
        super(pProperties);
    }


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }


    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        if(blockentity instanceof BatteryBlockEntity) {
            ItemStack itemStack = new ItemStack(this);
            itemStack.getOrCreateTag().putIntArray("InputStatus", ((BatteryBlockEntity) blockentity).inputStatusForItem);
            itemStack.getOrCreateTag().putInt("Energy", ((BatteryBlockEntity) blockentity).getStoredEnergy());

            ItemEntity itementity = new ItemEntity(pLevel, (double)pPos.getX() + 0.5D, (double)pPos.getY() + 0.5D, (double)pPos.getZ() + 0.5D, itemStack);
            itementity.setDefaultPickUpDelay();
            pLevel.addFreshEntity(itementity);
        }

        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

       if(!pLevel.isClientSide()) {
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if(blockEntity instanceof BatteryBlockEntity) {
            MenuProvider containerProvider = new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return Component.translatable("demonicvessel.screen.battery");
                }

                @Nullable
                @Override
                public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
                    return new BatteryMenu(pContainerId, pPlayer, pPos);
                }
            };
            NetworkHooks.openScreen((ServerPlayer) pPlayer, containerProvider, blockEntity.getBlockPos());
        }
        else {
            throw new IllegalArgumentException("Our named container provider is missing!");
        }
       }
        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
        return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);

    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BatteryBlockEntity(pPos, pState);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return (lvl, pos, blockState, t) -> {
            if (t instanceof BatteryBlockEntity tile) {
                tile.tick();
            }
        };
    }
}
