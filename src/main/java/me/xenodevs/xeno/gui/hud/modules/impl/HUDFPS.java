package me.xenodevs.xeno.gui.hud.modules.impl;

import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.hud.modules.HUDMod;
import me.xenodevs.xeno.module.modules.client.Colors;

public class HUDFPS extends HUDMod {

	public HUDFPS() {
		super("FPS", 0, 20, Xeno.moduleManager.getModule("FPS"));
	}	

	@Override
	public void draw() {
		TextUtil.drawStringWithShadow("FPS: " + mc.getDebugFPS(), getX() + 1, getY(), Colors.colourInt);
		
		super.draw();
	}
	
	@Override
	public void renderDummy(int mouseX, int mouseY) {	
		super.renderDummy(mouseX, mouseY);
		
		drag.setHeight(getHeight());
		drag.setWidth(getWidth());
		
		TextUtil.drawStringWithShadow("FPS: " + mc.getDebugFPS(), getX() + 1, getY(), this.parent.enabled ? Colors.colourInt : 0xFF900000);
	}
	
	@Override
	public int getWidth() {
		return TextUtil.getStringWidth("FPS: " + mc.getDebugFPS());
	}
	
	@Override
	public float getHeight() {
		return 11;
	}
	
}
