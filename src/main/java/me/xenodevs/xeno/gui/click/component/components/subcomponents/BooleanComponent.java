package me.xenodevs.xeno.gui.click.component.components.subcomponents;

import me.xenodevs.xeno.gui.GuiUtil;
import me.xenodevs.xeno.gui.click.ClickGui;
import me.xenodevs.xeno.gui.click.ClickGuiVariables;
import me.xenodevs.xeno.gui.click.component.Component;
import me.xenodevs.xeno.gui.click.component.components.Button;
import me.xenodevs.xeno.module.modules.client.ClickGuiModule;
import me.xenodevs.xeno.module.settings.BooleanSetting;

public class BooleanComponent extends Component {

	private boolean hovered;
	private BooleanSetting op;
	private Button parent;
	public double offset;
	private int x;
	private double y;
	
	public BooleanComponent(BooleanSetting option, Button button, double offset) {
		this.op = option;
		this.parent = button;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
	}

	@Override
	public void renderComponent(int mouseX, int mouseY) {
		ClickGui.theme.drawBoolean(op, parent, offset, mouseX, mouseY, hovered);
	}
	
	@Override
	public int getHeight() {
		return ClickGuiVariables.buttonBarHeight;
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
			this.op.toggle();
			if(ClickGuiModule.clickSound.enabled)
				GuiUtil.clickSound();
		}
	}
	
	public boolean isMouseOnButton(int x, int y) {
		if(x > this.x && x < this.x + ClickGuiVariables.frameWidth && y > this.y && y < this.y + ClickGuiVariables.buttonBarHeight) {
			return true;
		}
		return false;
	}
}
