package me.xenodevs.xeno.clickguis.xeno.component.components.subcomponents;

import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.clickguis.xeno.XenoGui;
import me.xenodevs.xeno.clickguis.xeno.component.Component;
import me.xenodevs.xeno.clickguis.xeno.component.components.Button;
import me.xenodevs.xeno.gui.GuiUtil;
import me.xenodevs.xeno.module.modules.client.ClickGuiModule;
import me.xenodevs.xeno.module.modules.client.Colors;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.utils.render.RenderUtils2D;
import org.lwjgl.opengl.GL11;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SliderComponent extends Component {

	private boolean hovered;

	private NumberSetting val;
	private Button parent;
	public double offset;
	public int x;
	private double y;
	private boolean dragging = false;

	private double renderWidth;
	
	public SliderComponent(NumberSetting value, Button button, double offset) {
		this.val = value;
		this.parent = button;
		this.x = button.parent.getX() + button.parent.getWidth();
		this.y = button.parent.getY() + button.offset;
		this.offset = offset;
	}
	
	@Override
	public int getHeight() {
		return XenoGui.buttonBarHeight;
	}
	
	@Override
	public void renderComponent(int mouseX, int mouseY) {
		RenderUtils2D.drawRect(parent.parent.getX() + 2, parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + XenoGui.buttonBarHeight, 0);
		RenderUtils2D.drawRect(parent.parent.getX(), parent.parent.getY() + offset + 1, parent.parent.getX() + (int) renderWidth, parent.parent.getY() + 1 + offset + XenoGui.buttonBarHeight, hovered ? Colors.col.brighter().getRGB() : Colors.col.getRGB());

		GL11.glPushMatrix();
		TextUtil.drawClickGuiString(val.getName() + ": " + val.getDoubleValue(), (parent.parent.getX() + 6), (float) (parent.parent.getY() + offset + 2), (parent.parent.getX() + (parent.parent.getWidth() / 2)), (parent.parent.getX() + parent.parent.getWidth()), -1);
		GL11.glPopMatrix();
	}
	
	@Override
	public void setOff(double newOff) {
		offset = newOff;
	}
	
	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.hovered = isMouseOnButtonD(mouseX, mouseY) || isMouseOnButtonI(mouseX, mouseY);
		this.y = parent.parent.getY() + offset;
		this.x = parent.parent.getX();
		
		double diff = Math.min(XenoGui.frameWidth, Math.max(0, mouseX - this.x));

		double min = val.getMinimum();
		double max = val.getMaximum();
		
		renderWidth = (XenoGui.frameWidth) * (val.getDoubleValue() - min) / (max - min);
		
		if (dragging) {
			if (diff == 0) {
				val.setValue(val.getMinimum());
			}
			else {
				double newValue = roundToPlace(((diff / (XenoGui.frameWidth)) * (max - min) + min), 2);
				val.setValue(newValue);
			}
		}
	}
	
	private static double roundToPlace(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButtonD(mouseX, mouseY) && button == 0 && this.parent.open) {
			dragging = true;
			if(ClickGuiModule.clickSound.enabled)
				GuiUtil.clickSound();
		}
		if(isMouseOnButtonI(mouseX, mouseY) && button == 0 && this.parent.open) {
			dragging = true;
			if(ClickGuiModule.clickSound.enabled)
				GuiUtil.clickSound();
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		dragging = false;
	}
	
	public boolean isMouseOnButtonD(int x, int y) {
		if(x > this.x && x < this.x + (parent.parent.getWidth() / 2) && y > this.y && y < this.y + XenoGui.buttonBarHeight) {
			return true;
		}
		return false;
	}
	
	public boolean isMouseOnButtonI(int x, int y) {
		if(x > this.x + parent.parent.getWidth() / 2 && x < this.x + parent.parent.getWidth() && y > this.y && y < this.y + XenoGui.buttonBarHeight) {
			return true;
		}
		return false;
	}
}
