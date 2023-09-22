package nickwrecks.demonicvessel.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import nickwrecks.demonicvessel.DemonicVessel;

public class Channel {
    private static SimpleChannel INSTANCE;
    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(DemonicVessel.MODID, "demonicvessel"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();
        INSTANCE = net;
        net.messageBuilder(BatteryConfigToServer.class,id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(BatteryConfigToServer::new)
                .encoder(BatteryConfigToServer::toBytes)
                .consumerMainThread(BatteryConfigToServer::handle)
                .add();
        net.messageBuilder(BatteryConfigToClient.class,id(),NetworkDirection.PLAY_TO_CLIENT)
                .decoder(BatteryConfigToClient::new)
                .encoder(BatteryConfigToClient::toBytes)
                .consumerMainThread(BatteryConfigToClient::handle)
                .add();
        net.messageBuilder(DistillationFeederToClient.class,id(),NetworkDirection.PLAY_TO_CLIENT)
                .decoder(DistillationFeederToClient::new)
                .encoder(DistillationFeederToClient::toBytes)
                .consumerMainThread(DistillationFeederToClient::handle)
                .add();
    }
    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }
    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}

