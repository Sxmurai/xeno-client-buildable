package me.xenodevs.xeno.gui.click.component;

import java.util.ArrayList;

import fr.lavache.anime.Animate;
import fr.lavache.anime.Easing;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.click.ClickGui;
import me.xenodevs.xeno.gui.click.ClickGuiVariables;
import me.xenodevs.xeno.gui.click.component.components.Button;
import me.xenodevs.xeno.gui.click.component.components.subcomponents.ColourComponent;
import me.xenodevs.xeno.gui.click.theme.themes.PlainTheme;
import me.xenodevs.xeno.gui.click.theme.themes.XenoTheme;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import net.minecraft.client.gui.FontRenderer;

import static org.lwjgl.opengl.GL11.*;

public class Frame {

	public ArrayList<Component> components;
	public Category category;
	private boolean open;
	private int width;
	private int y;
	private int x;
	private int barHeight;
	private boolean isDragging;
	public int dragX;
	public int dragY;
	public Animate animateXenoTheme = new Animate();
	public Animate animateAcross = new Animate();
	public Animate animateAlpha = new Animate();
	public Animate animateModuleLength = new Animate();

	public Frame(Category cat) {
		this.components = new ArrayList<>();
		this.category = cat;
		this.width = ClickGuiVariables.frameWidth;
		this.x = 5;
		this.y = 5;
		this.barHeight = ClickGuiVariables.frameBarHeight;
		this.dragX = 0;
		this.open = false;
		this.isDragging = false;
		
		double tY = this.barHeight + (ClickGui.theme.isScrollable ? 2.5 : 0);
		for(Module mod : Xeno.moduleManager.getModulesInCategory(category)) {
			Button modButton = new Button(mod, this, tY);
			this.components.add(modButton);
			if(ClickGui.theme instanceof XenoTheme)
				tY += ClickGuiVariables.buttonBarHeight - 0.5;
			else
				tY += ClickGuiVariables.buttonBarHeight;
		}

		Xeno.config.loadClickGUIConfig();

		try {
			setX((int) Xeno.config.clickGUIConfig.get(category.name() + "X"));
			setY((int) Xeno.config.clickGUIConfig.get(category.name() + "Y"));
			setOpen((boolean) Xeno.config.clickGUIConfig.get(category.name() + "Open"));
		} catch (NullPointerException e) {
			e.printStackTrace();
			setY(10);
			setX(ClickGui.frameX);
			setOpen(true);
			ClickGui.frameX += getWidth() + 6 + (ClickGui.theme instanceof PlainTheme ? 4 : 0);
		}

		animateXenoTheme.setEase(Easing.EXPO_IN_OUT).setMin(ClickGuiVariables.frameBarHeight + 3).setMax(ClickGui.maxLength).setReversed(!open).setSpeed(200);
		animateAcross.setEase(Easing.EXPO_IN_OUT).setMin(0).setMax(ClickGuiVariables.frameWidth + 4).setReversed(!open).setSpeed(100);
		animateAlpha.setEase(Easing.EXPO_IN_OUT).setMin(0).setMax(255).setReversed(!open).setSpeed(300);
		animateModuleLength.setEase(Easing.EXPO_IN_OUT).setMin(0).setMax(this.components.size() * ClickGuiVariables.buttonBarHeight).setReversed(!open).setSpeed(300);
	}

    public void open() {} // Animations maybe
	
	public void close() {} // Animations maybe
	
	public ArrayList<Component> getComponents() {
		return components;
	}
	
	public void setX(int newX) {
		this.x = newX;
	}
	
	public void setY(int newY) {
		this.y = newY;
	}
	
	public void setDrag(boolean drag) {
		this.isDragging = drag;
	}
	
	public boolean isOpen() {
		return open;
	}
	
	public void setOpen(boolean open) {
		if(ClickGui.theme instanceof XenoTheme) {
			this.open = true;
			if (animateXenoTheme.getValue() > 30) {
				animateXenoTheme.setEase(Easing.EXPO_IN_OUT).setMin(0).setMax(ClickGui.maxLength).setSpeed(200).setReversed(true);
				animateAcross.setEase(Easing.EXPO_IN_OUT).setMin(0).setMax(ClickGuiVariables.frameWidth + 4).setSpeed(100).setReversed(true);
				animateAlpha.setEase(Easing.EXPO_IN_OUT).setMin(0).setMax(255).setReversed(!open).setSpeed(300).setReversed(true);
			} else {
				animateXenoTheme.setEase(Easing.EXPO_IN_OUT).setMin(0).setMax(ClickGui.maxLength).setSpeed(200).setReversed(false).reset();
				animateAcross.setEase(Easing.EXPO_IN_OUT).setMin(0).setMax(ClickGuiVariables.frameWidth + 4).setSpeed(100).setReversed(false).reset();
				animateAlpha.setEase(Easing.EXPO_IN_OUT).setMin(0).setMax(255).setReversed(!open).setSpeed(300).setReversed(false).reset();
			}
		} else if(ClickGui.theme instanceof PlainTheme) {
			this.open = true;

			int length = ClickGuiVariables.frameBarHeight + 3;
			if(!components.isEmpty()) {
				for(Component component : components) {
					length += ClickGuiVariables.buttonBarHeight;

					if(component instanceof Button) {
						Button button = (Button) component;
						if(button.open) {
							for(Component c : button.subcomponents) {
								int extra = 0;
								if(c instanceof ColourComponent && ((ColourComponent) c).open) {
									extra = ClickGuiVariables.buttonBarHeight * ClickGuiVariables.colourMultiplier;
								}

								length += ClickGuiVariables.buttonBarHeight + extra;
							}
						}
					}
				}
			}

			if (animateModuleLength.getValue() > 30) {
				animateModuleLength.setEase(Easing.EXPO_IN_OUT).setMin(0).setMax(length).setSpeed(300).setReversed(true);
			} else {
				animateModuleLength.setEase(Easing.EXPO_IN_OUT).setMin(0).setMax(length).setSpeed(300).setReversed(false).reset();
			}
		} else {
			this.open = !this.open;
			if (this.open)
				this.open();
			else
				this.close();
			Xeno.config.saveClickGUIConfig();
		}
	}
	
	public void renderFrame(FontRenderer fontRenderer, int mouseX, int mouseY) {
		int length = ClickGuiVariables.frameBarHeight + 3;
		if(!components.isEmpty()) {
			for(Component component : components) {
				length += ClickGuiVariables.buttonBarHeight;

				if(component instanceof Button) {
					Button button = (Button) component;
					if(button.open) {
						for(Component c : button.subcomponents) {
							int extra = 0;
							if(c instanceof ColourComponent && ((ColourComponent) c).open) {
								extra = ClickGuiVariables.buttonBarHeight * ClickGuiVariables.colourMultiplier;
							}

							length += ClickGuiVariables.buttonBarHeight + extra;
						}
					}
				}
			}
		}

		animateXenoTheme.update();
		animateXenoTheme.setMax(ClickGui.maxLength);
		animateAcross.update();
		animateAlpha.update();
		animateModuleLength.update();
		animateModuleLength.setMax(length);

		glPushMatrix();
		ClickGui.theme.drawFrame(components, this, category, open, getX(), getY(), getWidth(), mouseX, mouseY);
		glPopMatrix();
	}
	
	public void refresh() {
		Button first = (Button) this.components.get(0);
		double off = (ClickGui.theme.isScrollable ? first.offset : this.barHeight);
		for(Component comp : components) {
			comp.setOff(off);
			off += comp.getHeight();
		}
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getBarHeight() {
		return barHeight;
	}
	
	public void updatePosition(int mouseX, int mouseY) {
		if(this.isDragging) {
			this.setX(mouseX - dragX);
			this.setY(mouseY - dragY);
		}
	}
	
	public boolean isWithinHeader(int x, int y) {
		if(x >= (this.x - 2) && x <= (this.x + this.width + 2) && y >= this.y && y <= this.y + this.barHeight) {
			return true;
		}
		return false;
	}
}
