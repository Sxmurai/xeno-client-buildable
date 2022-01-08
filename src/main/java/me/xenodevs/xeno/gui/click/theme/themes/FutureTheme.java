package me.xenodevs.xeno.gui.click.theme.themes;

import java.awt.Color;
import java.util.ArrayList;

import me.xenodevs.xeno.gui.GuiUtil;
import me.xenodevs.xeno.gui.click.component.components.subcomponents.*;
import me.xenodevs.xeno.module.settings.*;
import net.minecraft.client.renderer.entity.Render;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.gui.click.ClickGuiVariables;
import me.xenodevs.xeno.gui.click.component.Component;
import me.xenodevs.xeno.gui.click.component.Frame;
import me.xenodevs.xeno.gui.click.component.components.Button;
import me.xenodevs.xeno.gui.click.theme.Theme;
import me.xenodevs.xeno.managers.FontManager;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.modules.client.Colors;
import me.xenodevs.xeno.utils.other.Timer;
import me.xenodevs.xeno.utils.render.Colour;
import me.xenodevs.xeno.utils.render.RenderUtils2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class FutureTheme extends Theme {

	Timer timer = new Timer();

	public FutureTheme() {
		super("Future", false);
	}

	@Override
	public void drawBoolean(BooleanSetting op, Button parent, double offset, int mouseX, int mouseY, boolean hovered) {
		RenderUtils2D.drawRect(parent.parent.getX() - 2, parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 2, parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight, 0x70000000);

		if(op.isEnabled()) {
			RenderUtils2D.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight, new Colour(new Colour(Colors.colourInt), (hovered ? 100 : 150)).getRGB());
		}

		if(hovered && !op.isEnabled())
			RenderUtils2D.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight, new Colour(new Colour(Color.GRAY), 100).getRGB());

		TextUtil.drawStringWithShadow(op.getName(), parent.parent.getX() + 6, (float) (parent.parent.getY() + offset + 2), -1);
	}

	@Override
	public void drawMode(ModeSetting op, Button parent, double offset, int mouseX, int mouseY, boolean hovered) {
		RenderUtils2D.drawRect(parent.parent.getX() - 2, parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 2, parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight, 0x70000000);
		RenderUtils2D.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight, new Colour(new Colour(Colors.colourInt), (hovered ? 100 : 150)).getRGB());

		TextUtil.drawStringWithShadow(op.getName(), parent.parent.getX() + 6, (float) (parent.parent.getY() + offset + 2), -1);
		TextUtil.drawStringWithShadow(op.getMode(), parent.parent.getX() + 6 + TextUtil.getStringWidth(op.getName() + " "), (float) (parent.parent.getY() + offset + 2), Color.GRAY.brighter().getRGB());
	}

	@Override
	public void drawSlider(NumberSetting val, Button parent, double offset, int renderWidth, boolean hovered, int mouseX, int mouseY) {
		RenderUtils2D.drawRect(parent.parent.getX() - 2, parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 2, parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight, 0x70000000);

		if(hovered)
			RenderUtils2D.drawRect(parent.parent.getX() + (renderWidth), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight, new Colour(new Colour(Color.GRAY), 100).getRGB());

		RenderUtils2D.drawRectWH(parent.parent.getX(), parent.parent.getY() + offset, renderWidth, ClickGuiVariables.buttonBarHeight, new Colour(new Colour(Colors.colourInt), (hovered ? 100 : 150)).getRGB());

		TextUtil.drawStringWithShadow(val.getName() + ": " + val.getDoubleValue(), parent.parent.getX() + 6, (float) (parent.parent.getY() + offset + 2), -1);
	}

	@Override
	public void drawColourPicker(ColourComponent cb, ColourPicker op, double offset, boolean open, Button parent, int mouseX, int mouseY) {
		RenderUtils2D.drawRect(parent.parent.getX() - 2, parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 2, parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight, 0x70000000);
		RenderUtils2D.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight, new Color(230, 230, 230, (30)).getRGB());
		TextUtil.drawStringWithShadow(op.getName(), parent.parent.getX() + 6, (float) (parent.parent.getY() + offset + 2), -1);

		if(open) {
			RenderUtils2D.drawRect(parent.parent.getX() - 2, parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight, parent.parent.getX() + parent.parent.getWidth() + 2, parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight + (ClickGuiVariables.buttonBarHeight * ClickGuiVariables.colourMultiplier), 0x70000000);

			cb.drawPicker(op, (parent.parent.getX() + 4), (parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight + 5), (parent.parent.getX() + parent.parent.getWidth() - 14), parent.parent.getY() + offset + (ClickGuiVariables.buttonBarHeight) + 5, (parent.parent.getX() + 4), parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight * 5, mouseX, mouseY);

			TextUtil.drawStringWithShadow("Rainbow", (parent.parent.getX() + 6), (float) (parent.parent.getY() + offset + 90), op.getRainbow() ? new Color(op.getColor().getRed(), op.getColor().getGreen(), op.getColor().getBlue(), 255).getRGB() : -1);
		}

		int size = 13;

		if(!open) {
			GL11.glPushMatrix();
			GL11.glColor3f(1, 1, 1);

			ResourceLocation down = new ResourceLocation("xeno", ("textures/future_gear.png"));
			Minecraft.getMinecraft().getTextureManager().bindTexture(down);
			RenderUtils2D.drawModalRectWithCustomSizedTexture(parent.parent.getX() + parent.parent.getWidth() - 12, parent.parent.getY() + offset + 2, 0.0F, 0.0F, 10, 10, 10, 10);

			GL11.glPopMatrix();
		} else {
			GL11.glPushMatrix();
			GL11.glColor3f(1, 1, 1);

			ResourceLocation down = new ResourceLocation("xeno", ("textures/future_gear_rotated.png"));
			Minecraft.getMinecraft().getTextureManager().bindTexture(down);
			RenderUtils2D.drawModalRectWithCustomSizedTexture(parent.parent.getX() + parent.parent.getWidth() - 12, parent.parent.getY() + offset + 2, 0.0F, 0.0F, 10, 10, 10, 10);

			GL11.glPopMatrix();
		}
	}

	@Override
	public void drawStringComponent(StringSetting ss, StringComponent.CurrentString cs, Button parent, boolean isListening, boolean hovered, double offset, int x, double y, int mouseX, int mouseY) {
		RenderUtils2D.drawRect(parent.parent.getX() - 2, parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 2, parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight, 0x70000000);

		if(hovered && !isListening)
			RenderUtils2D.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight, new Colour(new Colour(Color.GRAY), 100).getRGB());

		if(isListening) {
			RenderUtils2D.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight, new Colour(new Colour(Colors.colourInt), (hovered ? 100 : 150)).getRGB());
			TextUtil.drawStringWithShadow(ss.getName() + " " + "\u00a77" + cs.getString() + "_", parent.parent.getX() + 6, (float) (parent.parent.getY() + offset + 2), -1);
		} else {
			TextUtil.drawStringWithShadow((ss.getName() + " " + "\u00a77") + ss.getText(), parent.parent.getX() + 6, (float) (parent.parent.getY() + offset + 2), -1);
		}
	}

	@Override
	public void drawKeybind(KeybindSetting op, double offset, Button parent, boolean binding, int mouseX, int mouseY) {
		RenderUtils2D.drawRect(parent.parent.getX() - 2, parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 2, parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight, 0x70000000);

		if(binding)
			RenderUtils2D.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight, new Colour(new Colour(Colors.colourInt), (150)).getRGB());

		TextUtil.drawStringWithShadow(binding ? "Press a key" : (op.name + TextFormatting.GRAY + " " + Keyboard.getKeyName(op.getKeyCode())), parent.parent.getX() + 6, (float) (parent.parent.getY() + offset + 2), -1);
	}

	@Override
	public void drawFrame(ArrayList<Component> components, Frame frame, Category category, boolean open, int x, int y, int width, int mouseX, int mouseY) {
		ClickGuiVariables.frameBarHeight = 13;
		ClickGuiVariables.buttonBarHeight = 15;

		int length = ClickGuiVariables.frameBarHeight + 3;
		boolean thing = false;

		int count = 0;
		if(open) {
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

								count++;
							}
						}
					}
				}
			}
		}

		RenderUtils2D.drawRectMC(x - 2, y, x + width + 2, y + ClickGuiVariables.frameBarHeight, new Colour(new Colour(Colors.colourInt), 170).getRGB()); // 0x90FF0000

		int size = 13;

		if(open) {
			ResourceLocation up = new ResourceLocation("xeno", "textures/future_arrow.png");
			Minecraft.getMinecraft().getTextureManager().bindTexture(up);
			Gui.drawModalRectWithCustomSizedTexture(x + width - 15, y, 0.0F, 0.0F, size, size, size, size);
		} else {
			ResourceLocation down = new ResourceLocation("xeno", "textures/future_arrow_down.png");
			Minecraft.getMinecraft().getTextureManager().bindTexture(down);
			Gui.drawModalRectWithCustomSizedTexture(x + width - 15, y, 0.0F, 0.0F, size, size, size, size);
		}

		if(open) {
			if(!components.isEmpty()) {
				for(Component component : components) {
					component.renderComponent(mouseX, mouseY);
				}
			}
		}

		// TextUtil.drawStringWithShadow(category.name, x + 2, y + 1, -1);

		FontManager.drawStringWithShadow(category.name, x + 2, y + 3, -1);

		int ex = 0;
		boolean a = false;
		for(Component c : components) {
			if(c instanceof Button && ((Button) c).open) {
				if(!a) {
					ex += 1;
					//a = true;
				}
				for(Component co : ((Button) c).subcomponents) {
					ex += co.getHeight();
				}
			}
		}

		if(open)
			Gui.drawRect(x - 2, y + ClickGuiVariables.frameBarHeight + components.size() * ClickGuiVariables.buttonBarHeight + ex, x + width + 2, y + ClickGuiVariables.frameBarHeight + components.size() * ClickGuiVariables.buttonBarHeight + ex + 1, 0x70000000);
		else {
			Gui.drawRect(x - 2, y + ClickGuiVariables.frameBarHeight, x + width + 2, y + ClickGuiVariables.frameBarHeight + 3, 0x70000000);
		}
	}

	@Override
	public void drawButton(Frame parent, Button button, Module mod, ArrayList<Component> subcomponents, boolean open, double offset, int mouseX, int mouseY, boolean hovered) {
		ClickGuiVariables.buttonBarHeight = 15;

		RenderUtils2D.drawRect(parent.getX() - 2, parent.getY() + offset, parent.getX() + parent.getWidth() + 2, parent.getY() + offset + ClickGuiVariables.buttonBarHeight, 0x70000000);
		RenderUtils2D.drawRect(parent.getX() - 1, parent.getY() + offset + 1, parent.getX() + parent.getWidth() + 1, parent.getY() + offset + ClickGuiVariables.buttonBarHeight, (mod.enabled ? new Colour(new Colour(Colors.colourInt), (hovered ? 100 : 150)).getRGB() : new Color(230, 230, 230, (hovered ? 26 : 30)).getRGB()));
		TextUtil.drawStringWithShadow(mod.getName(), parent.getX() + 2, (float) (parent.getY() + offset + 2.5), -1);

		if(subcomponents.size() > 2) {
			if(!open) {
				GL11.glPushMatrix();
				GL11.glColor3f(1, 1, 1);

				ResourceLocation down = new ResourceLocation("xeno", ("textures/future_gear.png"));
				Minecraft.getMinecraft().getTextureManager().bindTexture(down);
				RenderUtils2D.drawModalRectWithCustomSizedTexture(parent.getX() + parent.getWidth() - 10, parent.getY() + offset + 3, 0.0F, 0.0F, 10, 10, 10, 10);

				GL11.glPopMatrix();
			} else {
				GL11.glPushMatrix();
				GL11.glColor3f(1, 1, 1);

				ResourceLocation down = new ResourceLocation("xeno", ("textures/future_gear_rotated.png"));
				Minecraft.getMinecraft().getTextureManager().bindTexture(down);
				RenderUtils2D.drawModalRectWithCustomSizedTexture(parent.getX() + parent.getWidth() - 10, parent.getY() + offset + 3, 0.0F, 0.0F, 10, 10, 10, 10);

				GL11.glPopMatrix();
			}
		}

		if(open) {
			if(!subcomponents.isEmpty()) {
				double off = offset + ClickGuiVariables.buttonBarHeight + 1;
				if(open)
					RenderUtils2D.drawRect(parent.getX() - 2, parent.getY() + off - 1, parent.getX() + parent.getWidth() + 2, parent.getY() + off, 0x70000000);
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
		RenderUtils2D.drawRect(parent.parent.getX() - 2, parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth() + 2, parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight, 0x70000000);

		if(hovered)
			RenderUtils2D.drawRect(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + ClickGuiVariables.buttonBarHeight, new Colour(new Colour(Color.GRAY), 100).getRGB());

		TextUtil.drawStringWithShadow(op.getName(), parent.parent.getX() + 6, (float) (parent.parent.getY() + offset + 2), -1);
	}

	@Override
	public void drawDescription(String text, Timer timer, int mouseX, int mouseY) {
		RenderUtils2D.drawRoundedRect(mouseX + 5, mouseY - 14, TextUtil.getStringWidth(text) + 5, 12, ClickGuiVariables.frameRoundedRadius, 0xFF000000);
		RenderUtils2D.drawRoundedOutline(mouseX + 5, mouseY - 14, TextUtil.getStringWidth(text) + 5, 12, ClickGuiVariables.frameRoundedRadius, 2, Colors.colourInt);

		TextUtil.drawStringWithShadow(text, mouseX + 7, mouseY - 14, -1);
	}
}
