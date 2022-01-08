package me.xenodevs.xeno.gui.hud.modules.impl;

import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.hud.modules.HUDMod;
import me.xenodevs.xeno.module.modules.client.Colors;

public class HUDPing extends HUDMod {

	public HUDPing() {
		super("Ping", 0, 40, Xeno.moduleManager.getModule("Ping"));
	}	

	@Override
	public void draw() {
		TextUtil.drawStringWithShadow("Ping: " + this.getPing(), getX() + 1, getY(), Colors.colourInt);
		
		super.draw();
	}
	
	@Override
	public void renderDummy(int mouseX, int mouseY) {
		super.renderDummy(mouseX, mouseY);
		
		drag.setHeight(getHeight());
		drag.setWidth(getWidth());
		
		TextUtil.drawStringWithShadow("Ping: " + this.getPing(), getX() + 1, getY(), this.parent.enabled ? Colors.colourInt : 0xFF900000);
	}
	
	private static int getPing() {
        int p = -1;
        
        if (mc.player == null || mc.getConnection() == null || mc.getConnection().getPlayerInfo(mc.player.getName()) == null) {
            p = -1;
        } else {
            p = mc.getConnection().getPlayerInfo(mc.player.getName()).getResponseTime();
        }
        
        return p;
    }
	
	@Override
	public int getWidth() {
		return TextUtil.getStringWidth("Ping: " + this.getPing());
	}
	
	@Override
	public float getHeight() {
		return 11;
	}
	
}
