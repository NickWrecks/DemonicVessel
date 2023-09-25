package nickwrecks.demonicvessel.event;


import net.minecraft.client.gui.screens.social.PlayerEntry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;
import nickwrecks.demonicvessel.DemonicVessel;
import nickwrecks.demonicvessel.item.ModItems;

@Mod.EventBusSubscriber(modid = DemonicVessel.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEvents {

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        CompoundTag playerData = event.getEntity().getPersistentData();
        CompoundTag data = playerData.getCompound(Player.PERSISTED_NBT_TAG);
        if(data != null && !data.getBoolean("has_codex_gigas")) {
            ItemHandlerHelper.giveItemToPlayer(event.getEntity(), new ItemStack(ModItems.EXPERIENCE_GEM.get()));
            data.putBoolean("has_codex_gigas", true);
            playerData.put(Player.PERSISTED_NBT_TAG,data);
        }
    }

}
