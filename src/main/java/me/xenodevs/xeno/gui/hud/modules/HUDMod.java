package me.xenodevs.xeno.gui.hud.modules;

import java.awt.Color;
import me.wolfsurge.api.util.Globals;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.hud.DraggableComponent;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.utils.render.DisplayUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.common.MinecraftForge;

public class HUDMod implements Globals {

	public FontRenderer fr = mc.fontRenderer;
	ScaledResolution sr = new ScaledResolution(mc);
	
	public String name;
	public DraggableComponent drag;
	public Module parent;
	public int x, y, defaultX = 0, defaultY = 0;
	public float width = 50, height = 50;

	public HUDMod(String name, int x, int y, Module parent) {
		defaultX = x;
		defaultY = y;
		this.name = name;
		this.parent = parent;
		sub();
		
		try {
			this.x = (int) Xeno.config.hudConfig.get(name.toLowerCase() + "X");
			this.y = (int) Xeno.config.hudConfig.get(name.toLowerCase() + "Y");
		} catch (NullPointerException e) {
			e.printStackTrace();
			this.x = x;
			this.y = y;
		}

		try {
			// this.setEnabled((boolean) Xeno.config.gettableModuleConfig.get(parent.getName()));
			this.setEnabled(Xeno.moduleManager.getModule(parent.name).enabled);
		} catch (NullPointerException e) {
			e.printStackTrace();
			this.parent.enabled = false;
		}
		
		drag = new DraggableComponent(this.x, this.y, getWidth(), getHeight(), new Color(0, 0, 0, 0).getRGB(), this);
	}
	
	public void sub() {
		MinecraftForge.EVENT_BUS.register(this);
		Xeno.EVENT_BUS.subscribe(this);
	}
	
	public void refresh(int newX, int newY) {
		this.drag.setxPosition(newX);
		this.x = newX;
		this.drag.setyPosition(newY);
		this.y = newY;
	}
	
	public int getWidth() {
		return 50;
	}
	
	public float getHeight() {
		return height;
	}

	public void setHeight(float newHeight) {
		height = newHeight;
	}
	
	public void update() {
		if (drag.getX() < 0)
			drag.setX(0);

		if (drag.getX() > (DisplayUtils.getDisplayWidth()) - drag.getWidth())
			drag.setX((int) ((DisplayUtils.getDisplayWidth()) - drag.getWidth()));

		if (drag.getY() < 0)
			drag.setY(0);

		if (drag.getY() > (DisplayUtils.getDisplayHeight()) - drag.getHeight())
			drag.setY((int) ((DisplayUtils.getDisplayHeight()) - drag.getHeight()));
	}
	
	public void draw() {
		update();
	}
	
	public void renderDummy(int mouseX, int mouseY) {
		drag.draw(mouseX, mouseY);
	}
	
	public int getX() {
		return drag.getxPosition();
	}
	
	public int getY() {
		return drag.getyPosition();
	}

	public boolean isEnabled() {
		return parent.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.parent.enabled = enabled;
	}

}
