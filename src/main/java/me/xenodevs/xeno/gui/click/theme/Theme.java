package me.xenodevs.xeno.gui.click.theme;

import java.util.ArrayList;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.click.component.Component;
import me.xenodevs.xeno.gui.click.component.Frame;
import me.xenodevs.xeno.gui.click.component.components.Button;
import me.xenodevs.xeno.gui.click.component.components.subcomponents.ColourComponent;
import me.xenodevs.xeno.gui.click.component.components.subcomponents.StringComponent;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.modules.client.ClickGuiModule;
import me.xenodevs.xeno.module.settings.*;
import me.xenodevs.xeno.utils.other.Timer;

public abstract class Theme {
	
	public String name;
	public boolean isScrollable;
	
	public Theme(String name, boolean isScrollable) {
		this.name = name;
		this.isScrollable = isScrollable;
	}
	
	public abstract void drawBoolean(BooleanSetting op, Button parent, double offset, int mouseX, int mouseY, boolean hovered);

	public abstract void drawMode(ModeSetting op, Button parent, double offset, int mouseX, int mouseY, boolean hovered);

	public abstract void drawSlider(NumberSetting val, Button parent, double offset, int renderWidth, boolean hovered, int mouseX, int mouseY);

	public abstract void drawColourPicker(ColourComponent cb, ColourPicker op, double offset, boolean open, Button parent, int mouseX, int mouseY);

	public abstract void drawStringComponent(StringSetting ss, StringComponent.CurrentString cs, Button parent, boolean isListening, boolean hovered, double offset, int x, double y, int mouseX, int mouseY);

	public abstract void drawKeybind(KeybindSetting op, double offset, Button parent, boolean binding, int mouseX, int mouseY);

	public abstract void drawFrame(ArrayList<Component> components, Frame frame, Category category, boolean open, int x, int y, int width, int mouseX, int mouseY);

	public abstract void drawButton(Frame parent, Button button, Module mod, ArrayList<Component> subcomponents, boolean open, double offset, int mouseX, int mouseY, boolean hovered);

	public abstract void drawButtonComponent(ButtonSetting op, Button parent, double offset, int mouseX, int mouseY, boolean hovered);

	public abstract void drawDescription(String text, Timer timer, int mouseX, int mouseY);

	public void drawScreen() {
		if(ClickGuiModule.blurBG.isEnabled())
			Xeno.blurManager.blur((int) ClickGuiModule.BGBlurIntensity.getDoubleValue());
	}

}
