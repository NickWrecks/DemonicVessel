package nickwrecks.demonicvessel.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import nickwrecks.demonicvessel.client.screen.DistillationFeederScreen;

import java.util.function.Supplier;

public class DistillationFeederToClient {
    private final boolean isOverFire;
    private final BlockPos pos;
    public DistillationFeederToClient(boolean isOverFire,BlockPos pos) {
        this.isOverFire = isOverFire;
        this.pos = pos;
    }
    public DistillationFeederToClient(FriendlyByteBuf buf) {
        isOverFire = buf.readBoolean();
        pos = buf.readBlockPos();
    }
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(isOverFire);
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().screen instanceof DistillationFeederScreen feederScreen) {
                feederScreen.isOverFire = this.isOverFire;
            }
        });

        return true;
    }
}
