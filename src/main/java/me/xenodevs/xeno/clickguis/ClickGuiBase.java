package me.xenodevs.xeno.clickguis;

import me.xenodevs.xeno.gui.click.ClickGuiVariables;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;

public class ClickGuiBase extends GuiScreen {
    public static int frameWidth = 90;
    public static int frameBarHeight = 13;
    public static int buttonBarHeight = 13;
    public static int clickGuiColor = new Color(0, 128, 255).getRGB();
    public static int frameRoundedRadius = 12; // Set between 1 and 10
    public static int colourMultiplier = 6;

    public static boolean frameTextUnderline = false;
    public static boolean limitFrameXandYToDisplaySides = true; // Stop the frames from going beyond the screen dimensions
    public static int maxLength = buttonBarHeight * 15;

    public void reset() {}
}
