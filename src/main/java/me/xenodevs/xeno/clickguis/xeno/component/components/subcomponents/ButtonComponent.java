package me.xenodevs.xeno.clickguis.xeno.component.components.subcomponents;

import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.clickguis.xeno.XenoGui;
import me.xenodevs.xeno.clickguis.xeno.component.Component;
import me.xenodevs.xeno.clickguis.xeno.component.components.Button;
import me.xenodevs.xeno.gui.GuiUtil;
import me.xenodevs.xeno.module.modules.client.ClickGuiModule;
import me.xenodevs.xeno.module.settings.ButtonSetting;

public class ButtonComponent extends Component {

	private boolean hovered;
	private ButtonSetting op;
	private Button parent;
	public double offset;
	private int x;
	private double y;

	public ButtonComponent(ButtonSetting option, Button button, double offset) {
		this.op = option;
		this.parent = button;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
	}

	@Override
	public void renderComponent(int mouseX, int mouseY) {
		TextUtil.drawClickGuiString(op.getName(), parent.parent.getX() + 6, (float) (parent.parent.getY() + offset + 2), parent.parent.getX() + (parent.parent.getWidth() / 2), parent.parent.getX() + parent.parent.getWidth(), -1);
	}
	
	@Override
	public int getHeight() {
		return XenoGui.buttonBarHeight;
	}
	
	@Override
	public void setOff(double newOff) {
		offset = newOff;
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
			this.op.onClick();
			if(ClickGuiModule.clickSound.enabled)
				GuiUtil.clickSound();
		}
	}
	
	public boolean isMouseOnButton(int x, int y) {
		if(x > this.x && x < this.x + XenoGui.frameWidth && y > this.y && y < this.y + XenoGui.buttonBarHeight) {
			return true;
		}
		return false;
	}
}
