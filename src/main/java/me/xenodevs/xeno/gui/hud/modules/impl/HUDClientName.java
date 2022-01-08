package me.xenodevs.xeno.gui.hud.modules.impl;

import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.hud.modules.HUDMod;
import me.xenodevs.xeno.module.modules.client.Colors;
import me.xenodevs.xeno.module.modules.hud.ClientName;
import me.xenodevs.xeno.module.modules.hud.HUD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class HUDClientName extends HUDMod {

	public HUDClientName() {
		super("Client Name", 0, 0, Xeno.moduleManager.getModule("ClientName"));
	}	

	@Override
	public void draw() {
		if(ClientName.mode.is("Text"))
			TextUtil.drawStringWithShadow(Xeno.NAME + " " + Xeno.VERSION, getX(), getY(), Colors.colourInt);
		else if(ClientName.mode.is("Image")) {
			GL11.glPushMatrix();
			GL11.glColor3f(1, 1, 1);

			if(!parent.enabled)
				GL11.glColor3f(0.9f, 0, 0);

			GL11.glScaled(0.5, 0.5, 0.5);

			ResourceLocation down = new ResourceLocation("xeno", ("textures/xeno_logo_full.png"));
			Minecraft.getMinecraft().getTextureManager().bindTexture(down);
			Gui.drawModalRectWithCustomSizedTexture(getX() * 2, getY() * 2, 0.0F, 0.0F, 190, 70, 190, 70);

			GL11.glPopMatrix();
		}

		super.draw();
	}
	
	@Override
	public void renderDummy(int mouseX, int mouseY) {	
		super.renderDummy(mouseX, mouseY);
		
		drag.setHeight(getHeight());
		drag.setWidth(getWidth());
		
		// TextUtil.drawStringWithShadow(Xeno.NAME + " " + Xeno.VERSION, getX(), getY(), this.enabled ? Colors.colourInt : 0xFF900000);

		if(ClientName.mode.is("Text"))
			TextUtil.drawStringWithShadow(Xeno.NAME + " " + Xeno.VERSION, getX(), getY(), this.parent.enabled ? Colors.colourInt : 0xFF900000);
		else if(ClientName.mode.is("Image")) {
			GL11.glPushMatrix();

			if(!parent.enabled)
				GL11.glColor3f(0.9f, 0, 0);

			GL11.glScaled(0.5, 0.5, 0.5);

			ResourceLocation down = new ResourceLocation("xeno", ("textures/xeno_logo_full.png"));
			Minecraft.getMinecraft().getTextureManager().bindTexture(down);
			Gui.drawModalRectWithCustomSizedTexture(getX() * 2, getY() * 2, 0.0F, 0.0F, 190, 70, 190, 70);

			GL11.glPopMatrix();
		}
	}
	
	@Override
	public int getWidth() {
		return (ClientName.mode.is("Text") ? TextUtil.getStringWidth(Xeno.NAME + " " + Xeno.VERSION) : 95);
	}
	
	@Override
	public float getHeight() {
		return (ClientName.mode.is("Text") ? 11 : 35);
	}
	
}
