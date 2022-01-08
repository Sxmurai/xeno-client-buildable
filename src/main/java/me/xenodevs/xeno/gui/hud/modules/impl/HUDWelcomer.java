package me.xenodevs.xeno.gui.hud.modules.impl;

import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.hud.modules.HUDMod;
import me.xenodevs.xeno.module.modules.client.Colors;
import net.minecraft.client.gui.Gui;

public class HUDWelcomer extends HUDMod {

	public HUDWelcomer() {
		super("Welcomer", 100, 0, Xeno.moduleManager.getModule("Welcomer"));
	}	

	@Override
	public void draw() {
		TextUtil.drawStringWithShadow("Welcome to Xeno, " + mc.getSession().getUsername() + "!", getX(), getY(), Colors.colourInt);
		
		super.draw();
	}
	
	@Override
	public void renderDummy(int mouseX, int mouseY) {	
		super.renderDummy(mouseX, mouseY);
		
		drag.setHeight(getHeight());
		drag.setWidth(getWidth());
		
		TextUtil.drawStringWithShadow("Welcome to Xeno, " + mc.getSession().getUsername() + "!", getX(), getY(), this.parent.enabled ? Colors.colourInt : 0xFF900000);
	}
	
	@Override
	public int getWidth() {
		return TextUtil.getStringWidth("Welcome to Xeno, " + mc.getSession().getUsername() + "!");
	}
	
	@Override
	public float getHeight() {
		return 11;
	}
	
}
