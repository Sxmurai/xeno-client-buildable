package me.xenodevs.xeno.module.modules.movement;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.NumberSetting;

public class Step extends Module {

    public static Step INSTANCE;
    NumberSetting up = new NumberSetting("Up Height", 2.5, 1, 3, 0.5);

    public Step() {
        super("Step", "auto jump but better", 0, Category.MOVEMENT);
        this.addSettings(up);
        INSTANCE = this;
    }

    @Override
    public void onDisable() {
        mc.player.stepHeight = 0.5f;
    }

    @Override
    public void onUpdate() {
        if(nullCheck() || !isEnabled())
            return;

        mc.player.stepHeight = (float) up.getVal();
    }
}