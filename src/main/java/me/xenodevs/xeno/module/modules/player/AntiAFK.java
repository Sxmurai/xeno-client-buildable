package me.xenodevs.xeno.module.modules.player;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.utils.other.Timer;

public class AntiAFK extends Module {

    int tickCount = 1, afkCount = 1;
    Timer timer = new Timer();

    public AntiAFK() {
        super("AntiAFK", "stops you from being kicked by the server's AFK timer", Category.PLAYER);
    }

    @Override
    public void onDisable() {
        mc.gameSettings.keyBindForward.pressed = false;
    }

    @Override
    public void onUpdate() {
        mc.gameSettings.keyBindForward.pressed = true;
        if(timer.hasTimeElapsed(1, true)) {
            // mc.player.rotationYaw = mc.player.prevRotationYaw -= 90;
            mc.player.rotationYaw -= 10;
        }
    }
}
