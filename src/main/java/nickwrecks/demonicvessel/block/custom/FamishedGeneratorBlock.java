package nickwrecks.demonicvessel.block.custom;

import net.minecraft.core.BlockPos;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import nickwrecks.demonicvessel.block.entity.FamishedGeneratorBlockEntity;
import nickwrecks.demonicvessel.client.screen.FamishedGeneratorMenu;
import nickwrecks.demonicvessel.item.ModItems;
import org.jetbrains.annotations.Nullable;

public class FamishedGeneratorBlock extends BaseEntityBlock {
    public static final DirectionProperty HORIZONTAL_FACING =  BlockStateProperties.HORIZONTAL_FACING;
    public FamishedGeneratorBlock(Properties pProperties) {
        super(pProperties.requiresCorrectToolForDrops());
    }


    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(HORIZONTAL_FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(HORIZONTAL_FACING,pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
        return state.setValue(HORIZONTAL_FACING,direction.rotate(state.getValue(HORIZONTAL_FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(HORIZONTAL_FACING)));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new FamishedGeneratorBlockEntity(pPos,pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pLevel.isClientSide()) {
            return (lvl, pos, blockState, t) -> {
                if (t instanceof FamishedGeneratorBlockEntity tile) {
                    tile.tickClient();
                }
            };
        }
        return (lvl, pos, blockState, t) -> {
            if (t instanceof FamishedGeneratorBlockEntity tile) {
                tile.tickServer();
            }
        };
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(!pLevel.isClientSide) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof FamishedGeneratorBlockEntity famishedGen) {
                    ItemStack itemStack = pPlayer.getItemInHand(pHand);
                    if (!famishedGen.hasGem && itemStack.getItem() == ModItems.EXPERIENCE_GEM.get()) {
                        famishedGen.hasGem = true;
                        itemStack.shrink(1);
                        famishedGen.gemBreakerTimer = 200;
                        famishedGen.setChanged();
                        pLevel.sendBlockUpdated(pPos, pState, pState, Block.UPDATE_ALL);
                    } else if (famishedGen.hasGem && itemStack.isEmpty() && pPlayer.isCrouching()) {
                        famishedGen.collecting = !famishedGen.collecting;
                        famishedGen.setChanged();
                        pLevel.sendBlockUpdated(pPos, pState, pState, Block.UPDATE_ALL);
                    } else {
                        MenuProvider menuProvider = new MenuProvider() {
                        @Override
                        public Component getDisplayName() {
                            return Component.translatable("demonicvessel.screen.famishedgen");
                        }

                        @Nullable
                        @Override
                        public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
                            return new FamishedGeneratorMenu(pContainerId,pPlayer,pPos);
                        }
                    };
                        NetworkHooks.openScreen((ServerPlayer) pPlayer, menuProvider, blockEntity.getBlockPos());

                    }
                }
            }
        return InteractionResult.SUCCESS;
    }




    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        if (blockentity instanceof FamishedGeneratorBlockEntity && !pPlayer.isCreative()) {
            if(((FamishedGeneratorBlockEntity) blockentity).hasGem) {
                ItemEntity itemEntity = new ItemEntity(pLevel,pPos.getX()+0.5f,pPos.getY()+0.5f,pPos.getZ()+0.5f,Items.EMERALD.getDefaultInstance());
                itemEntity.setDefaultPickUpDelay();
                pLevel.addFreshEntity(itemEntity);
            }
        }
        super.playerWillDestroy(pLevel,pPos,pState,pPlayer);
    }
}
