
package me.wolfsurge.api;

import me.wolfsurge.api.gui.font.FontUtil;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.managers.FontManager;
import me.xenodevs.xeno.module.modules.client.ClickGuiModule;
import me.xenodevs.xeno.utils.render.ColorUtil;
import net.minecraft.client.Minecraft;

public class TextUtil {

	public static Minecraft mc = Minecraft.getMinecraft();
	static boolean cFont = false;

	public static void drawStringWithShadow(String text, float x, float y, int color) {
		if(Xeno.moduleManager.getModule("CustomFont").isEnabled()) {
			if(cFont) {
				FontManager.comfortaa.drawStringWithShadow(text, (x), y + 4, color);
				return;
			}

			FontUtil.comfortaa.drawStringWithShadow(text, (x), y + 4, color);
		} else {
			mc.fontRenderer.drawStringWithShadow(text, x, y + 2, color);
		}
	}
	
	public static int getStringWidth(String text) {
		try {
			if(Xeno.moduleManager.getModule("CustomFont").isEnabled()) {
				if(cFont) {
					return FontManager.comfortaa.getStringWidth(text);
				}

				return FontUtil.comfortaa.getStringWidth(text);
			} else {
				return mc.fontRenderer.getStringWidth(text);
			}
		} catch (NullPointerException e) {}
		
		return mc.fontRenderer.getStringWidth(text);
	}
	
	public static void drawCenteredString(String text, float x, float y, int color) {
		if(Xeno.moduleManager.getModule("CustomFont").isEnabled()) {
			if(cFont) {
				FontManager.comfortaa.drawStringWithShadow(text, (x - FontManager.comfortaa.getStringWidth(text) / 2), y + 4, color);
				return;
			}

			FontUtil.comfortaa.drawStringWithShadow(text, (x - FontUtil.comfortaa.getStringWidth(text) / 2), y + 4, color);
		} else {
			mc.fontRenderer.drawStringWithShadow(text, (x - mc.fontRenderer.getStringWidth(text) / 2), y + 2, color);
		}
    }
	
	public static void drawClickGuiString(String text, float x, float y, float centeredX, float rightX, int color) {
		if(Xeno.moduleManager.getModule("CustomFont").isEnabled()) {
			if(ClickGuiModule.textPos.is("Center")) {
				if(cFont) {
					FontManager.comfortaa.drawStringWithShadow(text, (centeredX - FontManager.comfortaa.getStringWidth(text) / 2), y + 4, color);
					return;
				}

				FontUtil.comfortaa.drawStringWithShadow(text, (centeredX - FontUtil.comfortaa.getStringWidth(text) / 2), y + 4, color);
			}
			if(ClickGuiModule.textPos.is("Left")) {
				if(cFont) {
					FontManager.comfortaa.drawStringWithShadow(text, x, y + 4, color);
					return;
				}

				FontUtil.comfortaa.drawStringWithShadow(text, x, y + 4, color);
			}
			
			if(ClickGuiModule.textPos.is("Right")) {
				if(cFont) {
					FontManager.comfortaa.drawStringWithShadow(text, (rightX - FontManager.comfortaa.getStringWidth(text) - 2), y + 4, color);
					return;
				}

				FontUtil.comfortaa.drawStringWithShadow(text, (rightX - FontUtil.comfortaa.getStringWidth(text) - 2), y + 4, color);
			}
		} else {
			if(ClickGuiModule.textPos.is("Center"))
				mc.fontRenderer.drawStringWithShadow(text, (centeredX - mc.fontRenderer.getStringWidth(text) / 2), y + 1, color);
			if(ClickGuiModule.textPos.is("Left"))
				mc.fontRenderer.drawStringWithShadow(text, (x), y + 1, color);
			if(ClickGuiModule.textPos.is("Right"))
				mc.fontRenderer.drawStringWithShadow(text, (rightX - mc.fontRenderer.getStringWidth(text) - 2), y + 1, color);
		}
	}
	
	public static void drawRainbowString(String text, float x, float y, float speed, float saturation, int seperation) {
		if(Xeno.moduleManager.getModule("CustomFont").isEnabled()) {
			if(cFont) {
				FontManager.comfortaa.drawRainbowStringWithShadow(text, x, y, 0, speed, saturation, seperation);
				return;
			}

			FontUtil.comfortaa.drawRainbowStringWithShadow(text, x, y, 0, speed, saturation, seperation);
		} else {
			int count = text.length() * seperation;
	    	int off = 0;
	    	String chars = "";
	    	for(char c : text.toCharArray()) {
	    		mc.fontRenderer.drawStringWithShadow(String.valueOf(c), x + off, y, ColorUtil.rainbowWave(speed, saturation, 1, count));
	    		off = off + mc.fontRenderer.getStringWidth(String.valueOf(c));
	    		count -= seperation;
	    	}
		}
	}
	
	public static void drawVanillaCenteredString(String text, float x, float y, int color) {
		mc.fontRenderer.drawStringWithShadow(text, (x - mc.fontRenderer.getStringWidth(text) / 2), y, color);
	}
	
}
