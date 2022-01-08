package me.xenodevs.xeno.gui.hud.modules.impl;

import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.hud.modules.HUDMod;
import me.xenodevs.xeno.module.modules.client.Colors;
import net.minecraft.util.text.TextFormatting;

public class HUDCoords extends HUDMod {

	public HUDCoords() {
		super("Coordinates", 0, 30, Xeno.moduleManager.getModule("Coordinates"));
	}	

	@Override
	public void draw() {
		TextUtil.drawStringWithShadow("XYZ " + TextFormatting.WHITE + mc.player.getPosition().x + " " + mc.player.getPosition().y + " " + mc.player.getPosition().z, getX() + 1, getY(), Colors.colourInt);
		
		super.draw();
	}
	
	@Override
	public void renderDummy(int mouseX, int mouseY) {	
		super.renderDummy(mouseX, mouseY);
		drag.setHeight(getHeight());
		drag.setWidth(getWidth());
		
		TextUtil.drawStringWithShadow("XYZ " + TextFormatting.WHITE + mc.player.getPosition().x + " " + mc.player.getPosition().y + " " + mc.player.getPosition().z, getX() + 1, getY(), this.parent.enabled ? Colors.colourInt : 0xFF900000);
	}
	
	@Override
	public int getWidth() {
		try {
			return TextUtil.getStringWidth("XYZ " + TextFormatting.WHITE + mc.player.getPosition().x + " " + mc.player.getPosition().y + " " + mc.player.getPosition().z);
		} catch(NullPointerException e) {
			return 0;
		}
	}
	
	@Override
	public float getHeight() {
		return 11;
	}
	
}
