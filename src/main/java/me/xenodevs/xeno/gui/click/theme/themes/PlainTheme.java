package me.xenodevs.xeno.gui.click.theme.themes;

import java.awt.Color;
import java.util.ArrayList;

import fr.lavache.anime.Animate;
import me.xenodevs.xeno.gui.click.component.components.subcomponents.*;
import me.xenodevs.xeno.module.settings.*;
import org.lwjgl.input.Keyboard;

import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.gui.click.ClickGuiVariables;
import me.xenodevs.xeno.gui.click.component.Component;
import me.xenodevs.xeno.gui.click.component.Frame;
import me.xenodevs.xeno.gui.click.component.components.Button;
import me.xenodevs.xeno.gui.click.theme.Theme;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.modules.client.ClickGuiModule;
import me.xenodevs.xeno.module.modules.client.Colors;
import me.xenodevs.xeno.utils.other.Timer;
import me.xenodevs.xeno.utils.render.Colour;
import me.xenodevs.xeno.utils.render.RenderUtils2D;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

public class PlainTheme extends Theme {

	public PlainTheme() {
		super("Plain", false);
	}

	@Override
	public void drawBoolean(BooleanSetting op, Button parent, double offset, int mouseX, int mouseY, boolean hovered) {
		RenderUtils2D.drawRect(parent.parent.getX() - 2, parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 2, parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight, 0x80000000);
		if(op.isEnabled()) {
			RenderUtils2D.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight, Colors.colourInt);
		}
		
		TextUtil.drawStringWithShadow(op.getName(), parent.parent.getX() + 6, (float) (parent.parent.getY() + offset + 1), -1);
	}
	
	@Override
	public void drawMode(ModeSetting op, Button parent, double offset, int mouseX, int mouseY, boolean hovered) {
		RenderUtils2D.drawRect(parent.parent.getX() - 2, parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 2, parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight, 0x80000000);
		TextUtil.drawStringWithShadow(op.getName() + ": " + op.getMode(), parent.parent.getX() + 6, (float) (parent.parent.getY() + offset + 1), -1);
	}
	
	@Override
	public void drawSlider(NumberSetting val, Button parent, double offset, int renderWidth, boolean hovered, int mouseX, int mouseY) {
		RenderUtils2D.drawRect(parent.parent.getX() - 2, parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 2, parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight, 0x80000000);
		RenderUtils2D.drawRect(parent.parent.getX() + 2, parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight, 0);
		
		RenderUtils2D.drawRectWH(parent.parent.getX(), parent.parent.getY() + offset, renderWidth, ClickGuiVariables.buttonBarHeight, hovered ? Colors.col.brighter().getRGB() : Colors.col.getRGB());

		TextUtil.drawStringWithShadow(val.getName() + ": " + val.getDoubleValue(), parent.parent.getX() + 6, (float) (parent.parent.getY() + offset + 1), -1);
	}
	
	@Override
	public void drawColourPicker(ColourComponent cb, ColourPicker op, double offset, boolean open, Button parent, int mouseX, int mouseY) {
		RenderUtils2D.drawRect(parent.parent.getX() - 2, parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 2, parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight, 0x80000000);
		
		if(open) {
			RenderUtils2D.drawRect(parent.parent.getX(), parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight + (ClickGuiVariables.buttonBarHeight * ClickGuiVariables.colourMultiplier), 0x70000000);
			
			cb.drawPicker(op, (parent.parent.getX() + 4), (parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight), (parent.parent.getX() + parent.parent.getWidth() - 14), parent.parent.getY() + offset + (ClickGuiVariables.buttonBarHeight), (parent.parent.getX() + 4), parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight * 5 - 1, mouseX, mouseY);
			TextUtil.drawCenteredString("Rainbow", (parent.parent.getX() + (parent.parent.getWidth() / 2)), (float) (parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight * 6), op.getRainbow() ? new Color(op.getColor().getRed(), op.getColor().getGreen(), op.getColor().getBlue(), 255).getRGB() : -1);
		}
		
		TextUtil.drawStringWithShadow(op.getName(), parent.parent.getX() + 6, (float) (parent.parent.getY() + offset + 1), -1);
	}

	@Override
	public void drawStringComponent(StringSetting ss, StringComponent.CurrentString cs, Button parent, boolean isListening, boolean hovered, double offset, int x, double y, int mouseX, int mouseY) {
		RenderUtils2D.drawRect(parent.parent.getX() - 2, parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 2, parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight, 0x80000000);
		if(isListening) {
			TextUtil.drawStringWithShadow(ss.getName() + " " + "\u00a77" + cs.getString() + "_", parent.parent.getX() + 6, (float) (parent.parent.getY() + offset + 1), -1);
		} else {
			TextUtil.drawStringWithShadow((ss.getName() + " " + "\u00a77") + ss.getText(), parent.parent.getX() + 6, (float) (parent.parent.getY() + offset + 1), -1);
		}
	}

	@Override
	public void drawKeybind(KeybindSetting op, double offset, Button parent, boolean binding, int mouseX, int mouseY) {
		RenderUtils2D.drawRect(parent.parent.getX() - 2, parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 2, parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight, 0x80000000);
		TextUtil.drawStringWithShadow(binding ? "Listening..." : (op.name + ": " + Keyboard.getKeyName(op.getKeyCode())), parent.parent.getX() + 6, (float) (parent.parent.getY() + offset + 1), -1);
	}
	
	@Override
	public void drawFrame(ArrayList<Component> components, Frame frame, Category category, boolean open, int x, int y, int width, int mouseX, int mouseY) {
		int length = ClickGuiVariables.frameBarHeight + 3;
		boolean thing = false;

		if(true) {
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
								
								if(!thing) {
									length -= 1;
									thing = true;
								}
							}
						}
					}
				}
			}
		}
		
		RenderUtils2D.drawRectMC(x - 2, y, x + width + 2, y + ClickGuiVariables.frameBarHeight, new Colour(new Colour(Colors.colourInt), 254).getRGB()); // 0x90FF0000
		
		if(true) {
			GL11.glPushMatrix();
			if(frame.animateModuleLength.getValue() < components.size() * 13) {
				GL11.glPushAttrib(GL11.GL_SCISSOR_BIT);
				{
					RenderUtils2D.scissor(x - 2, y + ClickGuiVariables.frameBarHeight, width + 4, frame.animateModuleLength.getValue());
					GL11.glEnable(GL11.GL_SCISSOR_TEST);
				}
			}
			if(!components.isEmpty()) {
				for(Component component : components) {
					component.renderComponent(mouseX, mouseY);
				}
			}
			if(frame.animateModuleLength.getValue() < components.size() * 13) {
				GL11.glDisable(GL11.GL_SCISSOR_TEST);
				GL11.glPopAttrib();
			}
			GL11.glPopMatrix();
		}
		
		TextUtil.drawStringWithShadow((ClickGuiModule.underline.enabled ? TextFormatting.UNDERLINE : "") + category.name, x + 2, y + 1, -1);
	}
	
	@Override
	public void drawButton(Frame parent, Button button, Module mod, ArrayList<Component> subcomponents, boolean open, double offset, int mouseX, int mouseY, boolean hovered) {
		RenderUtils2D.drawRect(parent.getX() - 2, parent.getY() + offset, parent.getX() + parent.getWidth() + 2, parent.getY() + offset + ClickGuiVariables.buttonBarHeight, 0x70000000);
		
		TextUtil.drawStringWithShadow(mod.getName(), parent.getX() + 2, (float) (parent.getY() + offset + 1), mod.enabled ? Colors.colourInt : -1);
		
		if(open) {
			if(!subcomponents.isEmpty()) {
				double off = offset + ClickGuiVariables.buttonBarHeight;
				int aa = 0;
				for (Component comp : subcomponents) {
					aa += ClickGuiVariables.buttonBarHeight;
					if (comp instanceof BooleanComponent) {
						comp.setOff(off);
						off += ClickGuiVariables.buttonBarHeight;
					}

					if (comp instanceof SliderComponent) {
						comp.setOff(off);
						off += ClickGuiVariables.buttonBarHeight;
					}

					if (comp instanceof ModeComponent) {
						comp.setOff(off);
						off += ClickGuiVariables.buttonBarHeight;
					}

					if (comp instanceof ColourComponent) {
						comp.setOff(off);

						off += ClickGuiVariables.buttonBarHeight + (((ColourComponent) comp).open ? ClickGuiVariables.buttonBarHeight * ClickGuiVariables.colourMultiplier : 0);
						aa += (((ColourComponent) comp).open ? ClickGuiVariables.buttonBarHeight * (ClickGuiVariables.colourMultiplier - 1) : 0);
					}

					if(comp instanceof StringComponent) {
						comp.setOff(off);
						off += ClickGuiVariables.buttonBarHeight;
					}

					if(comp instanceof ButtonComponent) {
						comp.setOff(off);
						off += ClickGuiVariables.buttonBarHeight;
					}

					if (comp instanceof KeybindComponent) {
						comp.setOff(off);
						off += ClickGuiVariables.buttonBarHeight;
					}

					comp.renderComponent(mouseX, mouseY);

					if(comp instanceof ColourComponent && ((ColourComponent) comp).open) {
						RenderUtils2D.drawRectWH(parent.getX() - 1, parent.getY() + ((ColourComponent) comp).offset, 1, ClickGuiVariables.buttonBarHeight * (ClickGuiVariables.colourMultiplier + 1), Colors.colourInt);
					}
				}
				RenderUtils2D.drawRect(parent.getX() - 2, parent.getY() + offset + ClickGuiVariables.buttonBarHeight, parent.getX() - 1, parent.getY() + offset + ClickGuiVariables.buttonBarHeight + aa, Colors.colourInt);
			}
		}
	}

	@Override
	public void drawButtonComponent(ButtonSetting op, Button parent, double offset, int mouseX, int mouseY, boolean hovered) {
		RenderUtils2D.drawRect(parent.parent.getX() - 2, parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 2, parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight, 0x80000000);

		TextUtil.drawStringWithShadow(op.getName(), parent.parent.getX() + 6, (float) (parent.parent.getY() + offset + 1), -1);
	}

	@Override
	public void drawDescription(String text, Timer timer, int mouseX, int mouseY) {
		RenderUtils2D.drawRoundedRect(mouseX + 5, mouseY - 15, TextUtil.getStringWidth(text) + 5, 15, ClickGuiVariables.frameRoundedRadius, 0xFF000000);
		RenderUtils2D.drawRoundedOutline(mouseX + 5, mouseY - 15, TextUtil.getStringWidth(text) + 5, 15, ClickGuiVariables.frameRoundedRadius, 2, Colors.colourInt);
					
		TextUtil.drawStringWithShadow(text, mouseX + 7, mouseY - 13, -1);
	}
}
