package me.xenodevs.xeno.clickguis.xeno.component.components.subcomponents;

import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.clickguis.xeno.XenoGui;
import me.xenodevs.xeno.clickguis.xeno.component.Component;
import me.xenodevs.xeno.clickguis.xeno.component.components.Button;
import me.xenodevs.xeno.gui.GuiUtil;
import me.xenodevs.xeno.module.modules.client.ClickGuiModule;
import me.xenodevs.xeno.module.settings.KeybindSetting;
import org.lwjgl.input.Keyboard;

public class KeybindComponent extends Component {

	private boolean hovered;
	private boolean binding;
	private Button parent;
	public double offset;
	private int x;
	private double y;
	public KeybindSetting op;
	
	public KeybindComponent(Button button, double offset, KeybindSetting op) {
		this.parent = button;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
		this.op = op;
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
		TextUtil.drawClickGuiString(binding ? "Press a key..." : (op.name + ": " + Keyboard.getKeyName(op.getKeyCode())), parent.parent.getX() + 6, (float) (parent.parent.getY() + offset + 1), parent.parent.getX() + (parent.parent.getWidth() / 2), parent.parent.getX() + parent.parent.getWidth(), -1);
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
			this.binding = !this.binding;
			if(ClickGuiModule.clickSound.enabled)
				GuiUtil.clickSound();
		}
		
		if(isMouseOnButton(mouseX, mouseY) && button == 1 && this.parent.open) {
			this.op.code = 0;
			this.binding = false;
			if(ClickGuiModule.clickSound.enabled)
				GuiUtil.clickSound();
		}
	}
	
	@Override
	public void keyTyped(char typedChar, int key) {
		if(this.binding) {
			this.op.code = key;
			this.binding = false;
		}
	}
	
	public boolean isMouseOnButton(int x, int y) {
		if(x > this.x && x < this.x + XenoGui.frameWidth && y > this.y && y < this.y + XenoGui.buttonBarHeight) {
			return true;
		}
		return false;
	}
}
