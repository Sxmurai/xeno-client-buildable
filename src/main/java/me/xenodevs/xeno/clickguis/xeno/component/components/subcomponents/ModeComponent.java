package me.xenodevs.xeno.clickguis.xeno.component.components.subcomponents;

import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.clickguis.xeno.XenoGui;
import me.xenodevs.xeno.clickguis.xeno.XenoGui;
import me.xenodevs.xeno.clickguis.xeno.component.Component;
import me.xenodevs.xeno.clickguis.xeno.component.components.Button;
import me.xenodevs.xeno.gui.GuiUtil;
import me.xenodevs.xeno.module.modules.client.ClickGuiModule;
import me.xenodevs.xeno.module.settings.ModeSetting;

public class ModeComponent extends Component {

	private boolean hovered;
	private Button parent;
	public ModeSetting op;
	public double offset;
	private int x;
	private double y;
	
	public ModeComponent(Button button, ModeSetting op, double offset) {
		this.parent = button;
		this.op = op;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
	}
	
	@Override
	public void setOff(double newOff) {
		offset = newOff;
	}
	
	@Override
	public int getHeight() {
		return XenoGui.buttonBarHeight;
	}
	
	@Override
	public void renderComponent(int mouseX, int mouseY) {
		TextUtil.drawClickGuiString(op.getName() + ": " + op.getMode(), parent.parent.getX() + 6, (float) (parent.parent.getY() + offset + 2), parent.parent.getX() + (parent.parent.getWidth() / 2), parent.parent.getX() + parent.parent.getWidth(), -1);
	}
	
	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButton(mouseX, mouseY);
		this.y = parent.parent.getY() + offset;
		this.x = parent.parent.getX();
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
			op.cycle();
			if(ClickGuiModule.clickSound.enabled)
				GuiUtil.clickSound();
			if(this.op == ClickGuiModule.theme) {
				XenoGui.themeSwitch();
			}
		}
	}
	
	public boolean isMouseOnButton(int x, int y) {
		if(x > this.x && x < this.x + XenoGui.frameWidth && y > this.y && y < this.y + XenoGui.buttonBarHeight) {
			return true;
		}
		return false;
	}
}
