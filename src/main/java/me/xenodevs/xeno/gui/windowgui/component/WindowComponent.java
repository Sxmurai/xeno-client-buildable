package me.xenodevs.xeno.gui.windowgui.component;

import static org.lwjgl.opengl.GL11.*;

import java.awt.*;
import java.util.ArrayList;

import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.GuiUtil;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.modules.client.Colors;
import me.xenodevs.xeno.utils.render.RenderUtils2D;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

public class WindowComponent extends Component {
	
	static Minecraft mc = Minecraft.getMinecraft();
	public ArrayList<Component> moduleComponents, categoryComponents, moduleSettingComponents;
	double x, y, width, height;
	String headerText;
	boolean isDragging = false;
	public double dragX;
	public double dragY;
	public Category currentCategory;
	
	public WindowComponent(String headerText, double x, double y, double width, double height) {
		currentCategory = Category.HUD;
		
		this.headerText = headerText;

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		categoryComponents = new ArrayList<>();
		
		double cWidth = this.width / Category.values().length - 3;
		double cX = 4;
		
		for(Category c : Category.values()) {
			categoryComponents.add(new CategoryComponent(c, x + cX, y + 26, cWidth, this));
			cX += cWidth + 2;
		}
		
		moduleComponents = new ArrayList<>();
		moduleSettingComponents = new ArrayList<>();

		double xOff = x + 4;
		double yOff = y + 44;
		int counter = 0;
		for(Module m : Xeno.moduleManager.getModules()) {
			moduleComponents.add(new ModuleComponent(m, xOff, yOff, this));
			counter++;
			if(counter == 3) {
				counter = 0;
				xOff = x + 4;
				yOff += 28;
			} else
				xOff += 142;
		}
		
		refreshModules();
	}

	public void refreshModules() {
		moduleComponents = new ArrayList<Component>();
		
		double xOff = x + 4;
		double yOff = y + 44;
		int counter = 0;
		
		for(Module m : Xeno.moduleManager.getModulesInCategory(this.currentCategory)) {
			moduleComponents.add(new ModuleComponent(m, xOff, yOff, this));
			counter++;
			if(counter == 3) {
				counter = 0;
				xOff = x + 4;
				yOff += 28;
			} else
				xOff += 142;
		}
	}
	
	@Override
	public void render(int mouseX, int mouseY) {
		update(mouseX, mouseY);
		
		GuiUtil.drawBorderedRect(x, y, width, height, 1, -1, 0x90000000);

		glPushMatrix();
		glScaled(2, 2, 2);
		mc.fontRenderer.drawStringWithShadow(headerText, (float) (x + 5) / 2, (float) (y + 5) / 2, Colors.colourInt);
		glPopMatrix();

		RenderUtils2D.drawRect(x, y + 23, x + width, y + 24, -1);
		RenderUtils2D.drawRect(x + width - 21, y + 2, x + width - 2, y + 22, (GuiUtil.mouseOver(x + width - 21, y + 2, x + width - 2, y + 22, mouseX, mouseY) ? 0x50000000 : 0x90000000));

		glPushMatrix();
		glScaled(2, 2, 2);
		mc.fontRenderer.drawString("x", (float) (x + width - 16.5) / 2, (float) (y + 3) / 2, -1, false);
		glPopMatrix();
		
		for(Component c : categoryComponents) {
			c.render(mouseX, mouseY);
		}

		RenderUtils2D.drawRect(x, y + 41, x + width, y + 42, -1);
		
		GL11.glPushMatrix();
		glPushAttrib(GL_SCISSOR_BIT);
		{
			RenderUtils2D.scissor(x, y + 44, x + width, height - 44);
			glEnable(GL_SCISSOR_TEST);
		}
		
		for(Component c : moduleComponents) {
			c.render(mouseX, mouseY);
		}
		
		glDisable(GL_SCISSOR_TEST);
		glPopAttrib();
		GL11.glPopMatrix();

		for(Component c : moduleSettingComponents) {
			c.render(mouseX, mouseY);
		}

		handleMouseWheel(mouseX, mouseY);
	}
	
	public void update(int mouseX, int mouseY) {
		if(this.isDragging) {
			this.x = (mouseX - dragX);
			this.y = (mouseY - dragY);
			double xOff = x + 4;
			double yOff = y + 44;
			int counter = 0;
			for(Component c : moduleComponents) {
				if(c instanceof ModuleComponent) {
					counter++;
					((ModuleComponent) c).x = xOff;
					((ModuleComponent) c).y = yOff;
					if(counter == 3) {
						counter = 0;
						xOff = x + 4;
						yOff += 28;
					} else
						xOff += 142;
				}
			}
			
			double cWidth = this.width / Category.values().length - 3;
			double cX = x + 4;
			for(Component c : categoryComponents) {
				if(c instanceof CategoryComponent) {
					((CategoryComponent) c).x = cX;
					((CategoryComponent) c).y = y + 26;
					cX += cWidth + 2;
				}
			}
		}

		for(Component c : moduleSettingComponents)
			c.updateComponent(mouseX, mouseY);
	}
	
	public void setDrag(boolean drag) {
		this.isDragging = drag;
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(GuiUtil.mouseOver(x, y + 2, x + width - 22, y + 22, mouseX, mouseY)) {
			setDrag(true);
            dragX = mouseX - x;
            dragY = mouseY - y;
		}
		
		if(GuiUtil.mouseOver(x + width - 21, y + 2, x + width - 2, y + 22, mouseX, mouseY) && button == 0) {
			mc.displayGuiScreen(null);
		}
		
		for(Component c : moduleComponents) {
			if(this.currentCategory == ((ModuleComponent) c).module.category) {
				c.mouseClicked(mouseX, mouseY, button);
			}
		}
		
		for(Component c : categoryComponents) {
			c.mouseClicked(mouseX, mouseY, button);
		}

		for(Component c : moduleSettingComponents) {
			c.mouseClicked(mouseX, mouseY, button);
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		setDrag(false);
		for(Component c : moduleSettingComponents)
			c.mouseReleased(mouseX, mouseY, mouseButton);
		super.mouseReleased(mouseX, mouseY, mouseButton);
	}
	
	public void handleMouseWheel(int mouseX, int mouseY) {		
		int dWheel = Mouse.getDWheel();
        if(dWheel < 0) {
        	for(Component c : moduleComponents) {
        		ModuleComponent last = (ModuleComponent) moduleComponents.get(moduleComponents.size() - 1);
        		
        		if(GuiUtil.mouseOver(x, y + 44, x + width, y + height, mouseX, mouseY) && last.y > y + 44) {
                    if(c instanceof ModuleComponent) {
                    	((ModuleComponent) c).y -= 13;
                    }
                }
        	}
        } else if(dWheel > 0) {
        	for(Component c : moduleComponents) {
        		if(GuiUtil.mouseOver(x, y + 44, x + width, y + height, mouseX, mouseY) && ((ModuleComponent) moduleComponents.get(0)).y < y + height - 10) {
        			((ModuleComponent) c).y += 13;
                }
        	}
        }
        
        // Java decided to be weird so I added this to stop an annoying bug
        try {
        	if(((ModuleComponent) moduleComponents.get(0)).y > ((ModuleComponent) moduleComponents.get(1)).y) {
        		((ModuleComponent) moduleComponents.get(0)).y = ((ModuleComponent) moduleComponents.get(1)).y;
        	}
		} catch (Exception e) {}
	}
}
