package me.xenodevs.xeno.managers;

import java.io.InputStream;

import me.wolfsurge.api.gui.font.FontRender;
import me.wolfsurge.api.util.Globals;
import me.xenodevs.xeno.Xeno;

public class FontManager implements Globals {

	public static FontRender comfortaa;
	public static FontRender comfortaaBig;

	public static FontRender aquireBig;
	public static FontRender aquire;

	public static void load() {
		comfortaa = new FontRender(getFont("comfortaa", 50));
		comfortaaBig = new FontRender(getFont("comfortaa", 80));

		aquire = new FontRender(getFont("aquire", 50));
		aquireBig = new FontRender(getFont("aquire", 120));
	}

	public static void drawStringWithShadow(String text, float x, float y, int color) {
		if(Xeno.moduleManager.isModuleEnabled("CustomFont"))
			comfortaa.drawStringWithShadow(text, x, y, color);
		else
			mc.fontRenderer.drawStringWithShadow(text, x, y, color);
	}

	public static int getStringWidth(String text) {
		return comfortaa.getStringWidth(text);
	}

	public static int getFontHeight() {
		return comfortaa.FONT_HEIGHT;
	}

	public static int getFontString(String text, float x, float y, int color) {
		return comfortaa.drawStringWithShadow(text, x, y, color);
	}

	private static java.awt.Font getFont(String fontName, float size) {
		try {
			InputStream inputStream = FontManager.class.getResourceAsStream("/assets/xeno/font/" + fontName + ".ttf");
			java.awt.Font awtClientFont = java.awt.Font.createFont(0, inputStream);
			awtClientFont = awtClientFont.deriveFont(java.awt.Font.PLAIN, size);
			inputStream.close();

			return awtClientFont;
		} catch (Exception exception) {
			exception.printStackTrace();
			return new java.awt.Font("default", java.awt.Font.PLAIN, (int) size);
		}
	}
}
