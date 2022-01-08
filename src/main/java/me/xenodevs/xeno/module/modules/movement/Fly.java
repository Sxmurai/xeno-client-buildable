package me.xenodevs.xeno.module.modules.movement;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.ButtonSetting;

public class Fly extends Module {

    public static Fly INSTANCE;

    public Fly() {
        super("Fly", "lets u fly lol", 0, Category.MOVEMENT);
        INSTANCE = this;
    }

    @Override
    public void onDisable() {
        if(mc.player != null)
            mc.player.capabilities.isFlying = false;
    }

    @Override
    public void onUpdate() {
        if(mc.player != null)
            mc.player.capabilities.isFlying = true;
    }
}