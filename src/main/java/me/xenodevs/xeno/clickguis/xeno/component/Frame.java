package me.xenodevs.xeno.clickguis.xeno.component;

import fr.lavache.anime.Animate;
import fr.lavache.anime.Easing;
import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.clickguis.xeno.XenoGui;
import me.xenodevs.xeno.clickguis.xeno.component.components.Button;
import me.xenodevs.xeno.clickguis.xeno.component.components.subcomponents.ColourComponent;
import me.xenodevs.xeno.clickguis.xeno.component.components.subcomponents.KeybindComponent;
import me.xenodevs.xeno.managers.FontManager;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.modules.client.ClickGuiModule;
import me.xenodevs.xeno.module.modules.client.Colors;
import me.xenodevs.xeno.utils.render.Colour;
import me.xenodevs.xeno.utils.render.RenderUtils2D;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.util.ArrayList;

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
		this.width = XenoGui.frameWidth;
		this.x = 5;
		this.y = 5;
		this.barHeight = XenoGui.frameBarHeight;
		this.dragX = 0;
		this.open = false;
		this.isDragging = false;
		
		double tY = this.barHeight + 2.5;
		for(Module mod : Xeno.moduleManager.getModulesInCategory(category)) {
			Button modButton = new Button(mod, this, tY);
			this.components.add(modButton);
			tY += XenoGui.buttonBarHeight - 0.5;
		}

		Xeno.config.loadClickGUIConfig();

		try {
			setX((int) Xeno.config.clickGUIConfig.get(category.name() + "X"));
			setY((int) Xeno.config.clickGUIConfig.get(category.name() + "Y"));
			setOpen((boolean) Xeno.config.clickGUIConfig.get(category.name() + "Open"));
		} catch (NullPointerException e) {
			e.printStackTrace();
			setY(10);
			setX(XenoGui.frameX);
			setOpen(true);
			XenoGui.frameX += getWidth() + 6;
		}

		animateXenoTheme.setEase(Easing.EXPO_IN_OUT).setMin(XenoGui.frameBarHeight + 3).setMax(XenoGui.maxLength).setReversed(!open).setSpeed(200);
		animateAcross.setEase(Easing.EXPO_IN_OUT).setMin(0).setMax(XenoGui.frameWidth + 4).setReversed(!open).setSpeed(100);
		animateAlpha.setEase(Easing.EXPO_IN_OUT).setMin(0).setMax(255).setReversed(!open).setSpeed(300);
		animateModuleLength.setEase(Easing.EXPO_IN_OUT).setMin(0).setMax(this.components.size() * XenoGui.buttonBarHeight).setReversed(!open).setSpeed(300);
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
		this.open = true;
		if (animateXenoTheme.getValue() > 30) {
			animateXenoTheme.setEase(Easing.EXPO_IN_OUT).setMin(0).setMax(XenoGui.maxLength).setSpeed(200).setReversed(true);
			animateAcross.setEase(Easing.EXPO_IN_OUT).setMin(0).setMax(XenoGui.frameWidth + 4).setSpeed(100).setReversed(true);
			animateAlpha.setEase(Easing.EXPO_IN_OUT).setMin(0).setMax(255).setReversed(!open).setSpeed(300).setReversed(true);
		} else {
			animateXenoTheme.setEase(Easing.EXPO_IN_OUT).setMin(0).setMax(XenoGui.maxLength).setSpeed(200).setReversed(false).reset();
			animateAcross.setEase(Easing.EXPO_IN_OUT).setMin(0).setMax(XenoGui.frameWidth + 4).setSpeed(100).setReversed(false).reset();
			animateAlpha.setEase(Easing.EXPO_IN_OUT).setMin(0).setMax(255).setReversed(!open).setSpeed(300).setReversed(false).reset();
		}
	}
	
	public void renderFrame(FontRenderer fontRenderer, int mouseX, int mouseY) {
		animateXenoTheme.update();
		animateXenoTheme.setMax(XenoGui.maxLength);
		animateAcross.update();
		animateAlpha.update();

		glPushMatrix();
		open = true;
		XenoGui.buttonBarHeight = 13;

		int length = (int) (XenoGui.frameBarHeight + 3 + animateXenoTheme.getValue());

		RenderUtils2D.drawRoundedRect(x - 2, y, XenoGui.frameWidth + 4, length, XenoGui.frameRoundedRadius, (open ? XenoGui.frameRoundedRadius : 1), (open ? XenoGui.frameRoundedRadius : 1), XenoGui.frameRoundedRadius, new Color(0, 0, 0, 100).getRGB());

		if(ClickGuiModule.blurFrame.isEnabled())
			Xeno.blurManager.blur(x - 2, y - 1, width + 4, length + 1, (int) ClickGuiModule.frameBlurIntensity.getDoubleValue());

		RenderUtils2D.drawRoundedOutline(x - 2, y, XenoGui.frameWidth + 4, length, XenoGui.frameRoundedRadius, (open ? XenoGui.frameRoundedRadius : 1), (open ? XenoGui.frameRoundedRadius : 1), XenoGui.frameRoundedRadius, 2, XenoGui.color);

		int modLength;

		if(open) {
			boolean doGrad = false;
			boolean doBottomGrad = false;
			if(!components.isEmpty()) {
				glPushMatrix();
				glPushAttrib(GL_SCISSOR_BIT);
				{
					RenderUtils2D.scissor(x - 2, y + XenoGui.frameBarHeight + 3, x + XenoGui.frameWidth + 2, (int) (y + XenoGui.frameBarHeight + 3 + animateXenoTheme.getValue()));
					glEnable(GL_SCISSOR_TEST);
				}

				for(Component component : components) {
					component.renderComponent(mouseX, mouseY);

					if(component instanceof Button) {
						modLength = XenoGui.buttonBarHeight - 2;
						Button button = (Button) component;
						if(button.open) {
							for(Component c : button.subcomponents) {
								int extra = 0;
								if(c instanceof ColourComponent && ((ColourComponent) c).open) {
									extra = XenoGui.buttonBarHeight * XenoGui.colourMultiplier;
								}
								modLength += XenoGui.buttonBarHeight + extra;
							}
						}

						if(ClickGuiModule.closedButtonOutline.enabled)
							RenderUtils2D.drawRoundedOutline(x, y + button.offset + 2.5, XenoGui.frameWidth, (button.open ? modLength + 0.5 : XenoGui.buttonBarHeight - 1.5), XenoGui.frameRoundedRadius, 2, (button.open ? XenoGui.color : new Color(XenoGui.color).darker().getRGB()));
						else if(button.open)
							RenderUtils2D.drawRoundedOutline(x, y + button.offset + 2.5, XenoGui.frameWidth, modLength + 0.5, XenoGui.frameRoundedRadius, 2, XenoGui.color);
						if (button.offset < 10 && !doGrad)
							doGrad = true;

						KeybindComponent finalSetting = (KeybindComponent) button.getSubcomponents().get(button.getSubcomponents().size() - 1);
						if (button.offset > (XenoGui.maxLength) && !doBottomGrad || button.open && finalSetting.offset > (XenoGui.maxLength) && !doBottomGrad)
							doBottomGrad = true;
					}
				}
				glDisable(GL_SCISSOR_TEST);
				glPopAttrib();
				glPopMatrix();
			}

			if(doGrad)
				RenderUtils2D.drawGradientRect(x - 1.5, y + XenoGui.frameBarHeight + 3, x + width + 1.5, y + XenoGui.frameBarHeight + 4 + (animateXenoTheme.getValue() / 4), 0x90000000, 0, false);
			if(doBottomGrad)
				RenderUtils2D.drawGradientRect(x - 1, y + XenoGui.frameBarHeight + 4 + animateXenoTheme.getValue() - 1 - (animateXenoTheme.getValue() / 4), x + width + 1, y + XenoGui.frameBarHeight + 4 + (animateXenoTheme.getValue() - 2), 0,  0x90000000, false);
		}

		if(Xeno.moduleManager.getModule("CustomFont").isEnabled()) {
			if(ClickGuiModule.textPos.is("Center")) {
				FontManager.comfortaa.drawStringWithShadow((ClickGuiModule.underline.enabled ? TextFormatting.UNDERLINE : "") + category.name, ((x + width / 2) - FontManager.comfortaa.getStringWidth((ClickGuiModule.underline.enabled ? TextFormatting.UNDERLINE : "") + category.name) / 2), y + 4, -1);
			}

			if(ClickGuiModule.textPos.is("Left")) {
				FontManager.comfortaa.drawStringWithShadow((ClickGuiModule.underline.enabled ? TextFormatting.UNDERLINE : "") + category.name, x + 2, y + 4, -1);
			}

			if(ClickGuiModule.textPos.is("Right")) {
				FontManager.comfortaa.drawStringWithShadow((ClickGuiModule.underline.enabled ? TextFormatting.UNDERLINE : "") + category.name, (x + width - FontManager.comfortaa.getStringWidth((ClickGuiModule.underline.enabled ? TextFormatting.UNDERLINE : "") + category.name) - 2), y + 4, -1);
			}
		} else {
			TextUtil.drawClickGuiString((ClickGuiModule.underline.enabled ? TextFormatting.UNDERLINE : "") + category.name, x + 2, y + 3, x + width / 2, x + width, -1);
		}

		RenderUtils2D.drawRectWH(x - 2, y + XenoGui.frameBarHeight + 2.5, animateAcross.getValue(), 1, new Colour(new Colour(Colors.colourInt), (int) animateAlpha.getValue()).getRGB());
		glPopMatrix();
	}
	
	public void refresh() {
		Button first = (Button) this.components.get(0);
		double off = first.offset;
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
