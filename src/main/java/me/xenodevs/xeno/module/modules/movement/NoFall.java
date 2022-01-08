package me.xenodevs.xeno.module.modules.movement;

import org.lwjgl.input.Keyboard;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.ModeSetting;
import net.minecraft.network.play.client.CPacketPlayer;

public class NoFall extends Module {

    public static NoFall INSTANCE;
    public static ModeSetting mode = new ModeSetting("Mode", "NCP", "AAC", "Vanilla");

    public NoFall() {
        super("NoFall", "dont break ur legs when you fall", Keyboard.KEY_NONE, Category.MOVEMENT);
        this.addSetting(mode);
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (mc.player.fallDistance > 4.0f) {
            switch (mode.getValue()) {
                case 0:
                    mc.player.fallDistance = 0;
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX +420420, mc.player.posY, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1, mc.player.posZ, true));
                    break;
                case 1:
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1, mc.player.posZ, true));
                    break;
                case 2:
                    mc.getConnection().sendPacket(new CPacketPlayer(true));
            }
        }
    }

    @Override
    public String getHUDData() {
        return " " + mode.getMode();
    }
}