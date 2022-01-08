package me.xenodevs.xeno.module.modules.movement;

import me.xenodevs.xeno.module.settings.ModeSetting;
import org.lwjgl.input.Keyboard;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;

public class Sprint extends Module {

    public static Sprint INSTANCE;
    ModeSetting mode = new ModeSetting("Mode", "Legit", "Omni");

    public Sprint() {
        super("Sprint", "run all the time lmao", Keyboard.KEY_NONE, Category.MOVEMENT);
        this.addSetting(mode);
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if(nullCheck())
            return;
        if(mode.is("Legit")) {
            if(mc.gameSettings.keyBindForward.isKeyDown())
                mc.player.setSprinting(true);
            else
                mc.player.setSprinting(false);
        } else if(mode.is("Omni"))
            mc.player.setSprinting(true);
    }
}