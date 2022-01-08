package me.xenodevs.xeno.gui.windowgui.component;

import me.xenodevs.xeno.gui.GuiUtil;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.modules.client.Colors;
import org.lwjgl.opengl.GL11;

public class ModuleComponent extends Component {

	Module module;
	WindowComponent windowComponent;
	double x, y;
	
	public ModuleComponent(Module module, double x, double y, WindowComponent parent) {
		this.module = module;
		this.windowComponent = parent;
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void render(int mouseX, int mouseY) {
		boolean isMouseOver = GuiUtil.mouseOver(x, y, x + 140, y + 26, mouseX, mouseY);
		GuiUtil.drawBorderedRect(x, y, 140, 26, 1, -1, isMouseOver ? 0x70000000 : 0x90000000);
		
		GL11.glPushMatrix();
		GL11.glScaled(2, 2, 2);
		mc.fontRenderer.drawStringWithShadow(module.name, (float) (x + 4) / 2, (float) (y + 5) / 2, module.isEnabled() ? Colors.colourInt : -1);
		GL11.glPopMatrix();
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(GuiUtil.mouseOver(x, y, x + 140, y + 26, mouseX, mouseY) && button == 0) {
			module.toggle();
		} else if(GuiUtil.mouseOver(x, y, x + 140, y + 26, mouseX, mouseY) && button == 1) {
			this.windowComponent.moduleSettingComponents.add(new ModuleSettingsComponent(module, windowComponent.x + 5, windowComponent.y, 300, 500));
		}
		
		super.mouseClicked(mouseX, mouseY, button);
	}
}
