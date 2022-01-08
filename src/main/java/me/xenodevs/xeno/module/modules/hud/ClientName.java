package me.xenodevs.xeno.module.modules.hud;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.ModeSetting;

public class ClientName extends Module {

    public static ModeSetting mode = new ModeSetting("Mode", "Text", "Image");

    public ClientName() {
        super("ClientName", "Displays the client's name on screen.", Category.HUD, true);
        this.addSettings(mode);
    }

}
