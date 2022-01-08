package me.xenodevs.xeno.gui.mainmenu;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.GuiUtil;
import me.xenodevs.xeno.gui.mainmenu.custom.XenoMainMenu;
import me.xenodevs.xeno.managers.FontManager;
import me.xenodevs.xeno.utils.render.ColorUtil;
import me.xenodevs.xeno.utils.render.RenderUtils2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class MainMenuHook {
	
	public static void render(int mouseX, int mouseY) {
		Xeno.discordManager.update("In Main Menu");
		ArrayList<String> strings = new ArrayList<>();
		
		strings.add("Logged in as " + Minecraft.getMinecraft().getSession().getUsername());
		strings.sort(Comparator.comparingInt(s -> FontManager.comfortaa.getStringWidth((String) s)).reversed());
		
		int len = FontManager.comfortaaBig.getStringWidth(Xeno.NAME) + 10;
		
		if(FontManager.comfortaa.getStringWidth(strings.get(0)) > FontManager.comfortaaBig.getStringWidth("Xeno Client")) {
			len = FontManager.comfortaa.getStringWidth(strings.get(0)) + 45;
		}
		
		RenderUtils2D.drawRoundedRect(-10, -10, len, 55, 10, new Color(0, 0, 0, 190).getRGB());
		
		FontManager.comfortaaBig.drawRainbowStringWithShadow(Xeno.NAME, 10, 10, ColorUtil.rainbowWave(4, 1, 1, 1), 3f, 0.9f, 100);
		
		int count = 30;
		for(String s : strings) {
			FontManager.comfortaa.drawRainbowStringWithShadow(s, 10, count, ColorUtil.rainbowWave(4, 1, 1, 1), 3f, 0.9f, 100);
			count += 10;
		}

		RenderUtils2D.drawRoundedRect(-10, 100, 85, 25, 5, 0x90000000);
		FontManager.comfortaa.drawStringWithShadow("Xeno Main\nMenu", 3, 103, GuiUtil.mouseOver(0, 100, 75, 125, mouseX, mouseY) ? ColorUtil.rainbowWave(4, 1, 1, 0) : -1);
	}

	public static void buttonPress(int mouseX, int mouseY, int mouseButton) {
		if(mouseButton == 0) {
			if(GuiUtil.mouseOver(0, 100, 75, 125, mouseX, mouseY)) {
				Minecraft.getMinecraft().displayGuiScreen(new XenoMainMenu());
				GuiUtil.customMainMenu = true;
				Xeno.config.saveMisc();
			}
		}
	}

}
