package me.xenodevs.xeno.module.modules.client;

import java.awt.Color;

import me.xenodevs.xeno.gui.click.ClickGui;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ColourPicker;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.utils.render.ColorUtil;
import me.xenodevs.xeno.utils.render.Colour;

public class Colors extends Module {
	
	public ColourPicker colour = new ColourPicker("Colour", new Colour(0, 128, 255, 255));
	
	public static int colourInt = -1;
	public static Color col = Color.WHITE;
	
	public Colors() {
		super("Colors", "change the clients colour", 0, Category.CLIENT);
		this.addSettings(colour);
	}
	
	@Override
	public void onNonToggledUpdate() {
		this.enabled = false;
		
		colourInt = colour.getColor().getRGB();
		col = colour.getColor();
	}
}
