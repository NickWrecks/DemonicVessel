package nickwrecks.demonicvessel.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
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
import nickwrecks.demonicvessel.block.entity.DistillationFeederBlockEntity;
import nickwrecks.demonicvessel.block.entity.FamishedGeneratorBlockEntity;
import nickwrecks.demonicvessel.client.screen.DistillationFeederMenu;
import nickwrecks.demonicvessel.network.Channel;
import nickwrecks.demonicvessel.network.DistillationFeederToClient;
import org.jetbrains.annotations.Nullable;

public class DistillationFeederBlock extends BaseEntityBlock {

    public DistillationFeederBlock(Properties pProperties) {
        super(pProperties.requiresCorrectToolForDrops());
    }

    public static final DirectionProperty HORIZONTAL_FACING =  BlockStateProperties.HORIZONTAL_FACING;
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
        return state.setValue(HORIZONTAL_FACING,  direction.rotate(state.getValue(HORIZONTAL_FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(HORIZONTAL_FACING)));
    }

    @Override
    public SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, @Nullable Entity entity) {
        return SoundType.GLASS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new DistillationFeederBlockEntity(pPos,pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide)
            return null;
        else return (lvl, pos, blockState, t) -> {
            if (t instanceof DistillationFeederBlockEntity tile) {
                tile.tickServer();
            }
        };
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if(!pLevel.isClientSide) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if(blockEntity instanceof DistillationFeederBlockEntity distillator) {
                MenuProvider menuProvider = new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return Component.translatable("demonicvessel.screen.distillationfeeder");
                    }

                    @Nullable
                    @Override
                    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
                        return new DistillationFeederMenu(pContainerId,pPlayer,pPos);
                    }
                };
                NetworkHooks.openScreen((ServerPlayer) pPlayer, menuProvider, blockEntity.getBlockPos());
                Channel.sendToPlayer(new DistillationFeederToClient(distillator.isOverFire,pPos),(ServerPlayer) pPlayer);
            }
        }
        return InteractionResult.SUCCESS;
    }

}
