package nickwrecks.demonicvessel.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import nickwrecks.demonicvessel.block.entity.CableBlockEntity;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

import static nickwrecks.demonicvessel.energy.RawDemonicEnergyStorage.ENERGY_CAPABILITY;

public class CableBlock extends BaseEntityBlock {
    public CableBlock(Properties pProperties) {
        super(pProperties.requiresCorrectToolForDrops());
        makeShapes();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(DOWN,UP,NORTH,SOUTH,WEST,EAST);
    }

    private static VoxelShape[] shapeCache = null;
    public static final EnumProperty<ConnectorType> DOWN = EnumProperty.<ConnectorType>create("down", ConnectorType.class);
    public static final EnumProperty<ConnectorType> UP = EnumProperty.<ConnectorType>create("up", ConnectorType.class);
    public static final EnumProperty<ConnectorType> NORTH = EnumProperty.<ConnectorType>create("north", ConnectorType.class);
    public static final EnumProperty<ConnectorType> SOUTH = EnumProperty.<ConnectorType>create("south", ConnectorType.class);
    public static final EnumProperty<ConnectorType> EAST = EnumProperty.<ConnectorType>create("east", ConnectorType.class);
    public static final EnumProperty<ConnectorType> WEST = EnumProperty.<ConnectorType>create("west", ConnectorType.class);
    private static final VoxelShape SHAPE_DOWN_CABLED = Shapes.box(0.375, 0, 0.375, 0.625, 0.375, 0.625);
    private static VoxelShape shapeDownConnected() {
            VoxelShape shape = Shapes.empty();
    shape = Shapes.join(shape, Shapes.box(0.375, 0.125, 0.375, 0.625, 0.375, 0.625), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(0.3125, 0.0625, 0.3125, 0.6875, 0.125, 0.6875), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(0.25, 0, 0.25, 0.75, 0.0625, 0.75), BooleanOp.OR);

	return shape;
    }

    private static final VoxelShape SHAPE_UP_CABLED = Shapes.box(0.375, 0.625, 0.375, 0.625, 1, 0.625);
    private static VoxelShape shapeUpConnected() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.375, 0.625, 0.375, 0.625, 0.875, 0.625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.3125, 0.875, 0.3125, 0.6875, 0.9375, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.25, 0.9375, 0.25, 0.75, 1, 0.75), BooleanOp.OR);
        return shape;
    };
    private static final VoxelShape SHAPE_NORTH_CABLED = Shapes.box(0.375, 0.375, 0, 0.625, 0.625, 0.375);
    private static final VoxelShape shapeNorthConnected() {
        VoxelShape shape = Shapes.empty();
    shape = Shapes.join(shape, Shapes.box(0.375, 0.375, 0.125, 0.625, 0.625, 0.375), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(0.3125, 0.3125, 0.0625, 0.6875, 0.6875, 0.125), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(0.25, 0.25, 0, 0.75, 0.75, 0.0625), BooleanOp.OR);

	return shape;}
    private static final VoxelShape SHAPE_SOUTH_CABLED = Shapes.box(0.375, 0.375, 0.625, 0.625, 0.625, 1);
    private static VoxelShape shapeSouthConnected() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.375, 0.375, 0.625, 0.625, 0.625, 0.875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.3125, 0.3125, 0.875, 0.6875, 0.6875, 0.9375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.25, 0.25, 0.9375, 0.75, 0.75, 1), BooleanOp.OR);

        return shape;
    }
    private static final VoxelShape SHAPE_WEST_CABLED = Shapes.box(0, 0.375, 0.375, 0.375, 0.625, 0.625);
    private static final VoxelShape shapeWestConnected() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.125, 0.375, 0.375, 0.375, 0.625, 0.625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.3125, 0.3125, 0.125, 0.6875, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0, 0.25, 0.25, 0.0625, 0.75, 0.75), BooleanOp.OR);

        return shape;
    }
    private static final VoxelShape SHAPE_EAST_CABLED =  Shapes.box(0.625, 0.375, 0.375, 1, 0.625, 0.625);
    private static VoxelShape shapeEastConnected() {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.625, 0.375, 0.375, 0.875, 0.625, 0.625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.875, 0.3125, 0.3125, 0.9375, 0.6875, 0.6875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.9375, 0.25, 0.25, 1, 0.75, 0.75), BooleanOp.OR);

        return shape;

    }


    @Nonnull
    private BlockState calculateState(LevelAccessor world, BlockPos pos, BlockState state) {
        ConnectorType north = getConnectorType(world, pos, Direction.NORTH);
        ConnectorType south = getConnectorType(world, pos, Direction.SOUTH);
        ConnectorType west = getConnectorType(world, pos, Direction.WEST);
        ConnectorType east = getConnectorType(world, pos, Direction.EAST);
        ConnectorType up = getConnectorType(world, pos, Direction.UP);
        ConnectorType down = getConnectorType(world, pos, Direction.DOWN);

        return state
                .setValue(NORTH, north)
                .setValue(SOUTH, south)
                .setValue(WEST, west)
                .setValue(EAST, east)
                .setValue(UP, up)
                .setValue(DOWN, down);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        return calculateState(pLevel,pCurrentPos,pState);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CableBlockEntity(pPos,pState);
    }
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        BlockState blockState = calculateState(pLevel,pPos,pState);
        if(blockState != pState)
            pLevel.setBlockAndUpdate(pPos,blockState);
    }

    private void makeShapes() {
        if(shapeCache == null) {
            int length = ConnectorType.values().length;
            shapeCache = new VoxelShape[length * length * length * length * length * length];
            for (ConnectorType up : ConnectorType.VALUES) {
                for (ConnectorType down : ConnectorType.VALUES) {
                    for (ConnectorType north : ConnectorType.VALUES) {
                        for (ConnectorType south : ConnectorType.VALUES) {
                            for (ConnectorType east : ConnectorType.VALUES) {
                                for (ConnectorType west : ConnectorType.VALUES) {
                                    int idx = calculateShapeIndex(north, south, west, east, up, down);
                                    shapeCache[idx] = makeShape(north, south, west, east, up, down);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    private int calculateShapeIndex(ConnectorType north, ConnectorType south, ConnectorType west, ConnectorType east, ConnectorType up, ConnectorType down) {
        int l = ConnectorType.values().length;
        return ((((south.ordinal() * l + north.ordinal()) * l + west.ordinal()) * l + east.ordinal()) * l + up.ordinal()) * l + down.ordinal();
    }

    private VoxelShape makeShape(ConnectorType north, ConnectorType south, ConnectorType west, ConnectorType east, ConnectorType up, ConnectorType down) {
        VoxelShape shape = Shapes.box(.4, .4, .4, .6, .6, .6);
        shape = combineShape(shape, down,  SHAPE_DOWN_CABLED, shapeDownConnected());
        shape = combineShape(shape, up,  SHAPE_UP_CABLED, shapeUpConnected());
        shape = combineShape(shape, north, SHAPE_NORTH_CABLED, shapeNorthConnected());
        shape = combineShape(shape, south,  SHAPE_SOUTH_CABLED, shapeSouthConnected());
        shape = combineShape(shape, west,  SHAPE_WEST_CABLED, shapeWestConnected());
        shape = combineShape(shape, east,  SHAPE_EAST_CABLED, shapeEastConnected());

        return shape;
    }
    private VoxelShape combineShape(VoxelShape shape, ConnectorType connectorType, VoxelShape cableShape, VoxelShape blockShape) {
        if (connectorType == ConnectorType.CABLED) {
            return Shapes.join(shape, cableShape, BooleanOp.OR);
        } else if (connectorType == ConnectorType.CONNECTED) {
            return Shapes.join(shape, blockShape, BooleanOp.OR);
        } else {
            return shape;
        }
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        ConnectorType north = getConnectorType(pLevel, pPos, Direction.NORTH);
        ConnectorType south = getConnectorType(pLevel, pPos, Direction.SOUTH);
        ConnectorType west = getConnectorType(pLevel, pPos, Direction.WEST);
        ConnectorType east = getConnectorType(pLevel, pPos, Direction.EAST);
        ConnectorType up = getConnectorType(pLevel, pPos, Direction.UP);
        ConnectorType down = getConnectorType(pLevel, pPos, Direction.DOWN);
        int index = calculateShapeIndex(north, south, west, east, up, down);
        return shapeCache[index];
    }

    private ConnectorType getConnectorType(BlockGetter world, BlockPos connectorPos, Direction facing) {
        BlockPos pos = connectorPos.relative(facing);
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block instanceof CableBlock) {
            return ConnectorType.CABLED;
        } else if (isConnectable(world, connectorPos, facing)) {
            return ConnectorType.CONNECTED;
        } else {
            return ConnectorType.NONE;
        }
    }

        public static boolean isConnectable(BlockGetter world, BlockPos connectorPos, Direction facing) {
            BlockPos pos = connectorPos.relative(facing);
            BlockState state = world.getBlockState(pos);
            if (state.isAir()) {
                return false;
            }
            BlockEntity te = world.getBlockEntity(pos);
            if (te == null) {
                return false;
            }
            return te.getCapability(ENERGY_CAPABILITY,facing.getOpposite()).isPresent();
        }


}

