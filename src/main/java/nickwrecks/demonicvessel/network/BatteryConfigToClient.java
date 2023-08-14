package nickwrecks.demonicvessel.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import nickwrecks.demonicvessel.client.screen.BatteryScreen;

import java.util.function.Supplier;

public class BatteryConfigToClient {
    private final BlockPos pos;
    private final int[] config;

    public BatteryConfigToClient(int[] config, BlockPos pos) {
        this.config = new int[6];
        for(int i =0; i<=5; i++) {
            this.config[i] = config[i];
        }
        this.pos = pos;
    }
    public BatteryConfigToClient(FriendlyByteBuf buf) {
        config =buf.readVarIntArray();
        pos = buf.readBlockPos();
    }
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeVarIntArray(config);
        buf.writeBlockPos(pos);
    }
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().screen instanceof BatteryScreen batteryScreen) {
                for (int i = 0; i <= 5; i++)
                    batteryScreen.config[i] = this.config[i];
            }
        });

        return true;
    }

}
