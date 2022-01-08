package me.xenodevs.xeno.module.modules.combat;

import me.xenodevs.xeno.event.impl.PacketEvent;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.CPacketPlayer;

import java.util.LinkedList;
import java.util.Queue;

public class Blink extends Module {

    private final Queue<CPacketPlayer> packetQueue = new LinkedList<>();
    private EntityOtherPlayerMP player;

    public Blink() {
        super("Blink", "fake lag", Category.COMBAT);
    }

    @Override
    public void onEnable() {
        player = new EntityOtherPlayerMP(mc.world, mc.getSession().getProfile());
        player.copyLocationAndAnglesFrom(mc.player);
        player.rotationYawHead = mc.player.rotationYawHead;
        mc.world.addEntityToWorld(-100, player);
    }

    @Override
    public void onDisable() {
        while (!packetQueue.isEmpty()) mc.player.connection.sendPacket(packetQueue.poll());

        if (mc.player != null) {
            mc.world.removeEntityFromWorld(-100);
            player = null;
        }
    }

    @EventHandler
    private final Listener<PacketEvent.Send> pkt = new Listener<>(event -> {
        if(mc.player == null || mc.world == null) return;

        if (event.getPacket() instanceof CPacketPlayer) {
            event.cancel();
            packetQueue.add((CPacketPlayer) event.getPacket());
        }
    });

}
