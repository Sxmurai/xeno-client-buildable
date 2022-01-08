package me.xenodevs.xeno.module.modules.misc;

import me.xenodevs.xeno.event.impl.PacketEvent;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.StringSetting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.network.play.server.SPacketChat;

public class FriendlyChat extends Module {

    public FriendlyChat() {
        super("FriendlyChat", "makes it so you cannot see NSFW words in chat!", Category.MISC);
    }

    @Override
    public void onUpdate() {
        if(nullCheck())
            return;


    }

    @EventHandler
    public final Listener<PacketEvent.Receive> receiveListener = new Listener<>(event -> {
       if(event.getPacket() instanceof SPacketChat) {

       }
    });

}
