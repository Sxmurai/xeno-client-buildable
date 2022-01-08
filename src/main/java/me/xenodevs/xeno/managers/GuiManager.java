package me.xenodevs.xeno.managers;

import me.xenodevs.xeno.clickguis.ClickGuiBase;
import me.xenodevs.xeno.clickguis.xeno.XenoGui;

public class GuiManager {
    // Themes
    public static XenoGui xenoTheme;

    public GuiManager() {
        xenoTheme = new XenoGui();
    }

    public static ClickGuiBase getCurrentGui() { // get current based on the mode in ClickGuiModule
        return xenoTheme;
    }

    public static void resetGui() {
        if(getCurrentGui() != null) // otherwise, it crashes when loading up
            getCurrentGui().reset();
    }
}
