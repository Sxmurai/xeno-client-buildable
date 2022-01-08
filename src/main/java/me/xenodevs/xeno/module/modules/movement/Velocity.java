package me.xenodevs.xeno.module.modules.movement;

import me.xenodevs.xeno.event.impl.PacketEvent;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;

public class Velocity extends Module {

    public static Velocity INSTANCE;

    BooleanSetting velocityPacket = new BooleanSetting("Velocity PKT", true);
    NumberSetting horizontal = new NumberSetting("Horizontal", 0, 0, 1, 0.01);
    NumberSetting vertical = new NumberSetting("Vertical", 0, 0, 1, 0.01);
    BooleanSetting explosion = new BooleanSetting("Explosion", true);
    BooleanSetting fishhook = new BooleanSetting("Fishhook", true);

    public Velocity() {
        super("Velocity", "stops u being knocked back", 0, Category.MOVEMENT);
        this.addSettings(velocityPacket, horizontal, vertical, explosion, fishhook);
        INSTANCE = this;
    }

    @EventHandler
    public final Listener<PacketEvent.Receive> velocityListener = new Listener<>(event -> {
        if(event.getPacket() instanceof SPacketEntityVelocity) {
            if(((SPacketEntityVelocity) event.getPacket()).getEntityID() == mc.player.getEntityId() && velocityPacket.isEnabled()) {
                if(horizontal.getFloatValue() == 0 && vertical.getFloatValue() == 0) {
                    event.cancel();
                    return;
                }

                SPacketEntityVelocity pkt = (SPacketEntityVelocity) event.getPacket();
                pkt.motionX *= horizontal.getFloatValue();
                pkt.motionY *= vertical.getFloatValue();
                pkt.motionZ *= horizontal.getFloatValue();
            }
        }

        if(event.getPacket() instanceof SPacketExplosion && explosion.isEnabled()) {
            event.cancel();
        }
        SPacketEntityStatus packet;
        Entity entity;
        if(event.getPacket() instanceof SPacketEntityStatus && fishhook.isEnabled() && (packet = (SPacketEntityStatus) event.getPacket()).getOpCode() == 31 && (entity = packet.getEntity(mc.world)) instanceof EntityFishHook) {
            EntityFishHook fishHook = (EntityFishHook) entity;
            if (fishHook.caughtEntity == mc.player) {
                event.cancel();
            }
        }
    });
}