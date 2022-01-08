package me.xenodevs.xeno.module.modules.client;

import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.clickguis.ClickGuiBase;
import me.xenodevs.xeno.gui.click.theme.Theme;
import me.xenodevs.xeno.gui.click.theme.themes.CleanTheme;
import me.xenodevs.xeno.gui.click.theme.themes.FutureTheme;
import me.xenodevs.xeno.gui.click.theme.themes.PlainTheme;
import me.xenodevs.xeno.gui.click.theme.themes.XenoTheme;
import me.xenodevs.xeno.managers.GuiManager;
import me.xenodevs.xeno.module.settings.ButtonSetting;
import org.lwjgl.input.Keyboard;
import me.xenodevs.xeno.gui.click.ClickGui;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;

public class ClickGuiModule extends Module {

	public static ClickGuiModule INSTANCE;

	public static ModeSetting textPos = new ModeSetting("TextPos", "Center", "Left", "Right");
	public static ModeSetting theme = new ModeSetting("Theme", "Xeno", "Plain", "Future");
	public static BooleanSetting underline = new BooleanSetting("Underline", false);
	public static BooleanSetting darken = new BooleanSetting("Darken", false);
	public static BooleanSetting blurFrame = new BooleanSetting("Blur Frame", false);
	public static NumberSetting frameBlurIntensity = new NumberSetting("FrBlur Intensity", 3, 1, 10, 1);
	public static BooleanSetting blurBG = new BooleanSetting("Blur BG", true);
	public static NumberSetting BGBlurIntensity = new NumberSetting("BG Intensity", 1, 1, 10, 1);

	public static BooleanSetting closedButtonOutline = new BooleanSetting("Button Outline", false);
	public static BooleanSetting clickSound = new BooleanSetting("Click Sound", true);
	public static ButtonSetting resetClickGUI = new ButtonSetting("Reset", () -> GuiManager.resetGui());
	public static BooleanSetting desc = new BooleanSetting("Tooltips", true);
	public static BooleanSetting limit = new BooleanSetting("LimitPositions", true);
	public static NumberSetting radius = new NumberSetting("Radius", 1, 1, 6, 1);
	public static NumberSetting length = new NumberSetting("Length", 300, 70, 495, 5);
	public static ModeSetting mode = new ModeSetting("Mode", "Frames", "Window");

	public ClickGuiModule() {
		super("ClickGUI", "idk clickgui open for settings etc", Keyboard.KEY_RSHIFT, Category.CLIENT);
		this.addSettings(resetClickGUI, desc, darken, closedButtonOutline, blurFrame, blurBG, BGBlurIntensity, clickSound, limit, radius, length, textPos, theme);
		INSTANCE = this;
	}

	@Override
	public void onEnable() {
		mc.displayGuiScreen(Xeno.guiManager.getCurrentGui());
		toggle();
	}
	
	@Override
	public void onNonToggledUpdate() {
		/* ClickGuiVariables.frameRoundedRadius = (int) radius.getDoubleValue();
		ClickGuiVariables.limitFrameXandYToDisplaySides = limit.getValue();
		ClickGuiVariables.frameTextUnderline = underline.getValue();
		ClickGui.maxLength = length.getIntValue(); */

		ClickGuiBase.frameRoundedRadius = (int) radius.getDoubleValue();
		ClickGuiBase.limitFrameXandYToDisplaySides = limit.getValue();
		ClickGuiBase.frameTextUnderline = underline.getValue();
		ClickGuiBase.maxLength = length.getIntValue();
	}

	public static Theme getTheme() {
		if(theme.is("Xeno"))
			return new XenoTheme();
		else if(theme.is("Clean"))
			return new CleanTheme();
		else if(theme.is("Plain"))
			return new PlainTheme();
		else if(theme.is("Future"))
			return new FutureTheme();

		return new XenoTheme();
	}
}
