package me.xenodevs.xeno.module.modules.hud;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.ModeSetting;

public class Armour extends Module {

    public static ModeSetting orientation = new ModeSetting("Orient", "Across", "Down");

    public Armour() {
        super("Armour", "Displays your armour on screen.", Category.HUD, true);
        this.addSettings(orientation);
    }
}
