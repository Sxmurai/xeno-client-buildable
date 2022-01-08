package me.xenodevs.xeno.module.modules.movement;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;

public class Jetpack extends Module {

    public Jetpack() {
        super("Jetpack", "Makes you go up when you jump.", Category.MOVEMENT);
    }

    @Override
    public void onUpdate() {
        if(mc.gameSettings.keyBindJump.pressed) {
            mc.player.jump();
        }
    }
}