package me.xenodevs.xeno.gui.hud.modules.impl;

import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.hud.modules.HUDMod;
import me.xenodevs.xeno.managers.TickManager.TPS;
import me.xenodevs.xeno.module.modules.client.Colors;

public class HUDTPS extends HUDMod {

	public HUDTPS() {
		super("TPS", 0, 50, Xeno.moduleManager.getModule("TPS"));
	}	

	@Override
	public void draw() {
		TextUtil.drawStringWithShadow("TPS: " + Xeno.tickManager.getTPS(TPS.CURRENT), getX() + 1, getY(), Colors.colourInt);
		
		super.draw();
	}
	
	@Override
	public void renderDummy(int mouseX, int mouseY) {	
		super.renderDummy(mouseX, mouseY);
		
		drag.setHeight(getHeight());
		drag.setWidth(getWidth());
		
		TextUtil.drawStringWithShadow("TPS: " + Xeno.tickManager.getTPS(TPS.CURRENT), getX() + 1, getY(), this.parent.enabled ? Colors.colourInt : 0xFF900000);
	}
	
	@Override
	public int getWidth() {
		try {
			return TextUtil.getStringWidth("TPS: " + Xeno.tickManager.getTPS(TPS.CURRENT));
		} catch (NullPointerException e) {
			return 0;
		}
	}
	
	@Override
	public float getHeight() {
		return 11;
	}
	
}
