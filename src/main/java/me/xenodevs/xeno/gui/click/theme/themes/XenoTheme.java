package me.xenodevs.xeno.gui.click.theme.themes;

import java.awt.Color;
import java.util.ArrayList;

import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.managers.FontManager;
import me.xenodevs.xeno.utils.render.Colour;
import org.lwjgl.input.Keyboard;
import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.gui.click.ClickGui;
import me.xenodevs.xeno.gui.click.ClickGuiVariables;
import me.xenodevs.xeno.gui.click.component.Component;
import me.xenodevs.xeno.gui.click.component.Frame;
import me.xenodevs.xeno.gui.click.component.components.Button;
import me.xenodevs.xeno.gui.click.component.components.subcomponents.*;
import me.xenodevs.xeno.gui.click.theme.Theme;
import me.xenodevs.xeno.module.*;
import me.xenodevs.xeno.module.modules.client.*;
import me.xenodevs.xeno.module.settings.*;
import me.xenodevs.xeno.utils.other.Timer;
import me.xenodevs.xeno.utils.render.RenderUtils2D;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;

public class XenoTheme extends Theme {

	public XenoTheme() {
		super("Xeno", true);
	}

	@Override
	public void drawBoolean(BooleanSetting op, Button parent, double offset, int mouseX, int mouseY, boolean hovered) {
		TextUtil.drawClickGuiString(op.getName(), parent.parent.getX() + 6, (float) (parent.parent.getY() + offset + 2), parent.parent.getX() + (parent.parent.getWidth() / 2), parent.parent.getX() + parent.parent.getWidth(), op.enabled ? ClickGui.color : -1);
	}
	
	@Override
	public void drawMode(ModeSetting op, Button parent, double offset, int mouseX, int mouseY, boolean hovered) {
		TextUtil.drawClickGuiString(op.getName() + ": " + op.getMode(), parent.parent.getX() + 6, (float) (parent.parent.getY() + offset + 2), parent.parent.getX() + (parent.parent.getWidth() / 2), parent.parent.getX() + parent.parent.getWidth(), -1);
	}
	
	@Override
	public void drawSlider(NumberSetting val, Button parent, double offset, int renderWidth, boolean hovered, int mouseX, int mouseY) {
		RenderUtils2D.drawRect(parent.parent.getX() + 2, parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight, 0);
		RenderUtils2D.drawRect(parent.parent.getX(), parent.parent.getY() + offset + 1, parent.parent.getX() + (int) renderWidth, parent.parent.getY() + 1 + offset + ClickGuiVariables.buttonBarHeight, hovered ? Colors.col.brighter().getRGB() : Colors.col.getRGB());

		GL11.glPushMatrix();
		TextUtil.drawClickGuiString(val.getName() + ": " + val.getDoubleValue(), (parent.parent.getX() + 6), (float) (parent.parent.getY() + offset + 2), (parent.parent.getX() + (parent.parent.getWidth() / 2)), (parent.parent.getX() + parent.parent.getWidth()), -1);
		GL11.glPopMatrix();
	}
	
	@Override
	public void drawColourPicker(ColourComponent cb, ColourPicker op, double offset, boolean open, Button parent, int mouseX, int mouseY) {
		if(open) {
			RenderUtils2D.drawRoundedOutline((parent.parent.getX() + 2), (parent.parent.getY() + offset + 2), ClickGuiVariables.frameWidth - 4, 89, ClickGuiVariables.frameRoundedRadius, 2, ClickGui.color);
			cb.drawPicker(op, (parent.parent.getX() + 4), (parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight), (parent.parent.getX() + parent.parent.getWidth() - 14), parent.parent.getY() + offset + (ClickGuiVariables.buttonBarHeight), (parent.parent.getX() + 5), parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight * 5 - 2, mouseX, mouseY);
			TextUtil.drawCenteredString("Rainbow", (parent.parent.getX() + (parent.parent.getWidth() / 2)), (float) (parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight * 6 - 1), op.getRainbow() ? new Color(op.getColor().getRed(), op.getColor().getGreen(), op.getColor().getBlue(), 255).getRGB() : -1);
		}
		
		TextUtil.drawClickGuiString(op.getName(), parent.parent.getX() + 6, (float) (parent.parent.getY() + offset + 2), parent.parent.getX() + (parent.parent.getWidth() / 2), parent.parent.getX() + parent.parent.getWidth(), new Color(op.getColor().getRed(), op.getColor().getGreen(), op.getColor().getBlue(), 255).getRGB());
	}

	@Override
	public void drawStringComponent(StringSetting ss, StringComponent.CurrentString cs, Button parent, boolean isListening, boolean hovered, double offset, int x, double y, int mouseX, int mouseY) {
		if(isListening) {
			TextUtil.drawStringWithShadow(ss.getName() + " " + "\u00a77" + cs.getString() + "_", parent.parent.getX() + 6, (float) (parent.parent.getY() + offset + 2), -1);
		} else {
			TextUtil.drawStringWithShadow((ss.getName() + " " + "\u00a77") + ss.getText(), parent.parent.getX() + 6, (float) (parent.parent.getY() + offset + 2), -1);
		}
	}

	@Override
	public void drawKeybind(KeybindSetting op, double offset, Button parent, boolean binding, int mouseX, int mouseY) {
		TextUtil.drawClickGuiString(binding ? "Press a key..." : (op.name + ": " + Keyboard.getKeyName(op.getKeyCode())), parent.parent.getX() + 6, (float) (parent.parent.getY() + offset + 1), parent.parent.getX() + (parent.parent.getWidth() / 2), parent.parent.getX() + parent.parent.getWidth(), -1);
	}
	
	@Override
	public void drawFrame(ArrayList<Component> components, Frame frame, Category category, boolean open, int x, int y, int width, int mouseX, int mouseY) {
		open = true;
		ClickGuiVariables.buttonBarHeight = 13;

		int length = (int) (ClickGuiVariables.frameBarHeight + 3 + frame.animateXenoTheme.getValue());

		RenderUtils2D.drawRoundedRect(x - 2, y, ClickGuiVariables.frameWidth + 4, length, ClickGuiVariables.frameRoundedRadius, (open ? ClickGuiVariables.frameRoundedRadius : 1), (open ? ClickGuiVariables.frameRoundedRadius : 1), ClickGuiVariables.frameRoundedRadius, new Color(0, 0, 0, 100).getRGB());

		if(ClickGuiModule.blurFrame.isEnabled())
			Xeno.blurManager.blur(x - 2, y - 1, width + 4, length + 1, (int) ClickGuiModule.frameBlurIntensity.getDoubleValue());

		RenderUtils2D.drawRoundedOutline(x - 2, y, ClickGuiVariables.frameWidth + 4, length, ClickGuiVariables.frameRoundedRadius, (open ? ClickGuiVariables.frameRoundedRadius : 1), (open ? ClickGuiVariables.frameRoundedRadius : 1), ClickGuiVariables.frameRoundedRadius, 2, ClickGui.color);

		int modLength;

		if(open) {
			boolean doGrad = false;
			boolean doBottomGrad = false;
			if(!components.isEmpty()) {
				glPushMatrix();
				glPushAttrib(GL_SCISSOR_BIT);
				{
					RenderUtils2D.scissor(x - 2, y + ClickGuiVariables.frameBarHeight + 3, x + ClickGuiVariables.frameWidth + 2, (int) (y + ClickGuiVariables.frameBarHeight + 3 + frame.animateXenoTheme.getValue()));
					glEnable(GL_SCISSOR_TEST);
				}

				for(Component component : components) {
					component.renderComponent(mouseX, mouseY);

					if(component instanceof Button) {
						modLength = ClickGuiVariables.buttonBarHeight - 2;
						Button button = (Button) component;
						if(button.open) {
							for(Component c : button.subcomponents) {
								int extra = 0;
								if(c instanceof ColourComponent && ((ColourComponent) c).open) {
									extra = ClickGuiVariables.buttonBarHeight * ClickGuiVariables.colourMultiplier;
								}
								modLength += ClickGuiVariables.buttonBarHeight + extra;
							}
						}

						if(ClickGuiModule.closedButtonOutline.enabled)
							RenderUtils2D.drawRoundedOutline(x, y + button.offset + 2.5, ClickGuiVariables.frameWidth, (button.open ? modLength + 0.5 : ClickGuiVariables.buttonBarHeight - 1.5), ClickGuiVariables.frameRoundedRadius, 2, (button.open ? ClickGui.color : new Color(ClickGui.color).darker().getRGB()));
						else if(button.open)
							RenderUtils2D.drawRoundedOutline(x, y + button.offset + 2.5, ClickGuiVariables.frameWidth, modLength + 0.5, ClickGuiVariables.frameRoundedRadius, 2, ClickGui.color);
						if (button.offset < 10 && !doGrad)
							doGrad = true;

						KeybindComponent finalSetting = (KeybindComponent) button.getSubcomponents().get(button.getSubcomponents().size() - 1);
						if (button.offset > (ClickGui.maxLength) && !doBottomGrad || button.open && finalSetting.offset > (ClickGui.maxLength) && !doBottomGrad)
							doBottomGrad = true;
					}
				}
				if(isScrollable) {
					glDisable(GL_SCISSOR_TEST);
					glPopAttrib();
				}
				glPopMatrix();
			}

			if(doGrad)
				RenderUtils2D.drawGradientRect(x - 1.5, y + ClickGuiVariables.frameBarHeight + 3, x + width + 1.5, y + ClickGuiVariables.frameBarHeight + 4 + (frame.animateXenoTheme.getValue() / 4), 0x90000000, 0, false);
			if(doBottomGrad)
				RenderUtils2D.drawGradientRect(x - 1, y + ClickGuiVariables.frameBarHeight + 4 + (frame.animateXenoTheme.getValue() - 1) - (frame.animateXenoTheme.getValue() / 4), x + width + 1, y + ClickGuiVariables.frameBarHeight + 4 + (frame.animateXenoTheme.getValue() - 2), 0,  0x90000000, false);
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

		RenderUtils2D.drawRectWH(x - 2, y + ClickGuiVariables.frameBarHeight + 2.5, frame.animateAcross.getValue(), 1, new Colour(new Colour(Colors.colourInt), (int) frame.animateAlpha.getValue()).getRGB());
	}
	
	@Override
	public void drawButton(Frame parent, Button button, Module mod, ArrayList<Component> subcomponents, boolean open, double offset, int mouseX, int mouseY, boolean hovered) {
		TextUtil.drawClickGuiString(mod.getName(), parent.getX() + 2, (float) (parent.getY() + offset + (Xeno.moduleManager.isModuleEnabled("CustomFont") ? 2.5f : 3f)), parent.getX() + (parent.getWidth() / 2), parent.getX() + parent.getWidth(), mod.isEnabled() ? ClickGui.color : -1);
		
		if(open) {
			if(!subcomponents.isEmpty()) {
				double off = offset + ClickGuiVariables.buttonBarHeight;
				for(Component comp : subcomponents) {
					if(comp instanceof BooleanComponent) {
						comp.setOff(off);
						off += ClickGuiVariables.buttonBarHeight;
					}
					
					if(comp instanceof SliderComponent) {
						comp.setOff(off);
						off += ClickGuiVariables.buttonBarHeight;
					}

					if(comp instanceof ModeComponent) {
						comp.setOff(off);
						off += ClickGuiVariables.buttonBarHeight;
					}

					if(comp instanceof ColourComponent) {
						comp.setOff(off);
						off += ClickGuiVariables.buttonBarHeight + (((ColourComponent) comp).open ? ClickGuiVariables.buttonBarHeight * ClickGuiVariables.colourMultiplier : 0);
					}

					if(comp instanceof StringComponent) {
						comp.setOff(off);
						off += ClickGuiVariables.buttonBarHeight;
					}

					if(comp instanceof ButtonComponent) {
						comp.setOff(off);
						off += ClickGuiVariables.buttonBarHeight;
					}
					
					if(comp instanceof KeybindComponent) {
						comp.setOff(off);
						off += ClickGuiVariables.buttonBarHeight;
					}
					
					comp.renderComponent(mouseX, mouseY);
				}
			}
		}
	}

	@Override
	public void drawButtonComponent(ButtonSetting op, Button parent, double offset, int mouseX, int mouseY, boolean hovered) {
		TextUtil.drawClickGuiString(op.getName(), parent.parent.getX() + 6, (float) (parent.parent.getY() + offset + 2), parent.parent.getX() + (parent.parent.getWidth() / 2), parent.parent.getX() + parent.parent.getWidth(), -1);
	}

	@Override
	public void drawDescription(String text, Timer timer, int mouseX, int mouseY) {
		Xeno.blurManager.blur(mouseX + 5, mouseY - 15, TextUtil.getStringWidth(text) + 5, 15, 3);
		TextUtil.drawStringWithShadow(text, mouseX + 7, mouseY - 13, -1);
	}
}