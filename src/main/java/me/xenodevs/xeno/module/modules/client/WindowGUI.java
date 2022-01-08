package me.xenodevs.xeno.module.modules.client;

import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import org.lwjgl.input.Keyboard;

public class WindowGUI extends Module {

    public WindowGUI() {
        super("WindowGUI", "Alternative to the ClickGUI", Keyboard.KEY_SEMICOLON, Category.CLIENT);
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(Xeno.windowGui);
        toggle();
    }
}
