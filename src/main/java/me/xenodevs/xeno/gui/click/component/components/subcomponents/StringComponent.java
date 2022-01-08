package me.xenodevs.xeno.gui.click.component.components.subcomponents;

import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.gui.GuiUtil;
import me.xenodevs.xeno.gui.click.ClickGui;
import me.xenodevs.xeno.gui.click.ClickGuiVariables;
import me.xenodevs.xeno.gui.click.component.Component;
import me.xenodevs.xeno.gui.click.component.components.Button;
import me.xenodevs.xeno.module.modules.client.ClickGuiModule;
import me.xenodevs.xeno.module.settings.StringSetting;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;

public class StringComponent extends Component {

	private boolean hovered, isListening;
	private StringSetting op;
	private Button parent;
	public double offset;
	private int x;
	private double y;
	private CurrentString currentString = new CurrentString("");

	public StringComponent(StringSetting option, Button button, double offset) {
		this.op = option;
		this.parent = button;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
	}

	@Override
	public void renderComponent(int mouseX, int mouseY) {
		GL11.glPushMatrix();
		ClickGui.theme.drawStringComponent(this.op, this.currentString, parent, isListening, isMouseOnButton(mouseX, mouseY), offset, x, y, mouseX, mouseY);
		GL11.glPopMatrix();
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
	public void keyTyped(char typedChar, int key) {
		if (this.isListening) {
			if (key == 1) {
				return;
			}
			if (key == 28) {
				this.enterString();
			} else if (key == 14) {
				this.setString(removeLastChar(this.currentString.getString()));
			} else if (key == 47 && (Keyboard.isKeyDown(157) || Keyboard.isKeyDown(29))) {
				try {
					this.setString(this.currentString.getString() + Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
				this.setString(this.currentString.getString() + typedChar);
			}
			if(key == Keyboard.KEY_RETURN) {
				isListening = false;
			}
		}
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
			this.isListening = !this.isListening;
			if(ClickGuiModule.clickSound.enabled)
				GuiUtil.clickSound();
		}
	}

	private void enterString() {
		if (this.currentString.getString().isEmpty()) {
			this.op.setText(this.op.getDefaultValue());
		} else {
			this.op.setText(this.currentString.getString());
		}
		this.setString("");
	}

	public void setString(String newString) {
		this.currentString = new CurrentString(newString);
	}

	public static String removeLastChar(String str) {
		String output = "";
		if (str != null && str.length() > 0) {
			output = str.substring(0, str.length() - 1);
		}
		return output;
	}

	public static class CurrentString {
		private final String string;

		public CurrentString(String string) {
			this.string = string;
		}

		public String getString() {
			return this.string;
		}
	}
	
	public boolean isMouseOnButton(int x, int y) {
		if(x > this.x && x < this.x + ClickGuiVariables.frameWidth && y > this.y && y < this.y + ClickGuiVariables.buttonBarHeight) {
			return true;
		}
		return false;
	}
}
