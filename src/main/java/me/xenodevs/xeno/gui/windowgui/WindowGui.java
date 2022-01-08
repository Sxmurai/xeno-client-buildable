package me.xenodevs.xeno.gui.windowgui;

import java.io.IOException;
import java.util.ArrayList;

import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.windowgui.component.Component;
import me.xenodevs.xeno.gui.windowgui.component.ModuleSettingsComponent;
import me.xenodevs.xeno.gui.windowgui.component.WindowComponent;
import me.xenodevs.xeno.utils.render.DisplayUtils;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Mouse;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class WindowGui extends GuiScreen {
	
	public ArrayList<Component> components = new ArrayList<Component>();
	public static TextFormatting textCol = TextFormatting.RED;
	
	public WindowGui() {
		components.add(new WindowComponent("Xeno Client " + Xeno.VERSION, DisplayUtils.getDisplayWidth() / 2 - 216, DisplayUtils.getDisplayHeight() / 2 - 175, 432, 336));
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		for(Component c : components) {
			if(c instanceof WindowComponent)
				c.updateComponent(mouseX, mouseY);
			
			c.render(mouseX, mouseY);
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for(Component c : components) {
			c.mouseClicked(mouseX, mouseY, mouseButton);
		}
		
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		if(!Mouse.isButtonDown(0)) {
            for(Component comp : components) {
            	if(comp instanceof WindowComponent)
            		((WindowComponent) comp).setDrag(false);
            }
        }

        for(Component comp : components) {
        	if(comp instanceof WindowComponent) {
	            for(Component component : ((WindowComponent) comp).moduleComponents) {
	                component.mouseReleased(mouseX, mouseY, state);
	            }
        	}
        }
        
		super.mouseReleased(mouseX, mouseY, state);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
	}
		
}
