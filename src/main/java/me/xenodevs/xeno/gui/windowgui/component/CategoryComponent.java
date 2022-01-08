package me.xenodevs.xeno.gui.windowgui.component;

import me.xenodevs.xeno.gui.GuiUtil;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.modules.client.Colors;

public class CategoryComponent extends Component {
	
	public Category c;
	public double x, y, width, height;
	public WindowComponent parent;
	
	public CategoryComponent(Category c, double x, double y, double width, WindowComponent parent) {
		this.c = c;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = 13;
		this.parent = parent;
	}

	@Override
	public void render(int mouseX, int mouseY) {
		boolean isMouseOver = GuiUtil.mouseOver(x, y, x + width, y + 13, mouseX, mouseY);
		GuiUtil.drawBorderedRect(x, y, width, height, 1, -1, isMouseOver ? 0x70000000 : 0x90000000);
		
		mc.fontRenderer.drawStringWithShadow(c.name(), (float) (x + 4), (float) (y + 3), (parent.currentCategory == c ? Colors.colourInt : -1));
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(GuiUtil.mouseOver(x, y, x + width, y + height, mouseX, mouseY) && button == 0) {
			parent.currentCategory = c;
			parent.refreshModules();
		}
		super.mouseClicked(mouseX, mouseY, button);
	}

}
