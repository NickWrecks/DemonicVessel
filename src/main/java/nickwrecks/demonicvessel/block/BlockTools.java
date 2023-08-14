package nickwrecks.demonicvessel.block;

import net.minecraft.core.Direction;

public class BlockTools {

    public static void makeInputStatusRelative(Direction facing, int[] processed, int[] unprocessed) {
        processed[Direction.UP.get3DDataValue()] = unprocessed[Direction.UP.get3DDataValue()];
        processed[Direction.DOWN.get3DDataValue()] = unprocessed[Direction.DOWN.get3DDataValue()];
        processed[facing.get3DDataValue()] = unprocessed[Direction.NORTH.get3DDataValue()];
        processed[facing.getOpposite().get3DDataValue()] = unprocessed[Direction.SOUTH.get3DDataValue()];
        processed[facing.getCounterClockWise().get3DDataValue()] = unprocessed[Direction.WEST.get3DDataValue()];
        processed[facing.getClockWise().get3DDataValue()] = unprocessed[Direction.EAST.get3DDataValue()];
    }
    public static void makeInputStatusAbsolute(Direction facing, int[] processed, int[] unprocessed) {
        processed[Direction.UP.get3DDataValue()]= unprocessed[Direction.UP.get3DDataValue()];
        processed[Direction.DOWN.get3DDataValue()]= unprocessed[Direction.DOWN.get3DDataValue()];
        processed[Direction.NORTH.get3DDataValue()]= unprocessed[facing.get3DDataValue()];
        processed[Direction.SOUTH.get3DDataValue()] = unprocessed[facing.getOpposite().get3DDataValue()];
        processed[Direction.WEST.get3DDataValue()] =unprocessed[facing.getCounterClockWise().get3DDataValue()];
        processed[Direction.EAST.get3DDataValue()] =unprocessed[facing.getClockWise().get3DDataValue()];

    }


}
