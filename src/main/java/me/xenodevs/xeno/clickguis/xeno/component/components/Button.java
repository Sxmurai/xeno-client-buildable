package me.xenodevs.xeno.clickguis.xeno.component.components;

import fr.lavache.anime.Animate;
import fr.lavache.anime.Easing;
import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.clickguis.xeno.XenoGui;
import me.xenodevs.xeno.clickguis.xeno.XenoGui;
import me.xenodevs.xeno.clickguis.xeno.component.Component;
import me.xenodevs.xeno.clickguis.xeno.component.Frame;
import me.xenodevs.xeno.clickguis.xeno.component.components.subcomponents.*;
import me.xenodevs.xeno.gui.GuiUtil;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.modules.client.ClickGuiModule;
import me.xenodevs.xeno.module.settings.*;

import java.util.ArrayList;

public class Button extends Component {

	public Module mod;
	public Frame parent;
	public double offset;
	private boolean isHovered;
	public ArrayList<Component> subcomponents;
	public boolean open;
	private int height;
	public Animate animate = new Animate();

	public Button(Module mod, Frame parent, double offset) {
		this.mod = mod;
		this.parent = parent;
		this.offset = offset;
		this.subcomponents = new ArrayList<Component>();
		this.open = false;
		height = XenoGui.buttonBarHeight;
		double opY = offset + XenoGui.buttonBarHeight;

		for(Setting s : mod.settings) {
			if(s instanceof BooleanSetting) {
				BooleanComponent check = new BooleanComponent((BooleanSetting) s, this, opY);
				this.subcomponents.add(check);
				opY += XenoGui.buttonBarHeight;
			}

			if(s instanceof NumberSetting) {
				SliderComponent slider = new SliderComponent((NumberSetting) s, this, opY);
				this.subcomponents.add(slider);
				opY += XenoGui.buttonBarHeight;
			}

			if(s instanceof ModeSetting) {
				ModeComponent modeButton = new ModeComponent(this, (ModeSetting) s, opY);
				this.subcomponents.add(modeButton);
				opY += XenoGui.buttonBarHeight;
			}
			
			if(s instanceof ColourPicker) {
				ColourComponent colourButton = new ColourComponent((ColourPicker) s, this, opY);
				this.subcomponents.add(colourButton);
				opY += XenoGui.buttonBarHeight;
			}

			if(s instanceof StringSetting) {
				StringComponent sb = new StringComponent((StringSetting) s, this, opY);
				this.subcomponents.add(sb);
				opY += XenoGui.buttonBarHeight;
			}

			if(s instanceof ButtonSetting) {
				ButtonComponent buttonComponent = new ButtonComponent((ButtonSetting) s, this, opY);
				this.subcomponents.add(buttonComponent);
				opY += XenoGui.buttonBarHeight;
			}
			
			if(s instanceof KeybindSetting) {
				KeybindComponent keybind = new KeybindComponent(this, opY, (KeybindSetting) s);
				this.subcomponents.add(keybind);
				opY += XenoGui.buttonBarHeight;
			}
		}

		animate.setEase(Easing.EXPO_IN_OUT).setMin(XenoGui.frameBarHeight + 3).setMax(XenoGui.maxLength).setReversed(!open).setSpeed(200);
	}
	
	public ArrayList<Component> getSubcomponents() {
		return subcomponents;
	}

	public void setSubcomponents(ArrayList<Component> subcomponents) {
		this.subcomponents = subcomponents;
	}

	@Override
	public void setOff(double newOff) {
		offset = newOff;
		double opY = offset + XenoGui.buttonBarHeight;
		for(Component comp : this.subcomponents) {
			comp.setOff(opY);
			opY += comp.getHeight();
		}
	}
	
	@Override
	public void renderComponent(int mouseX, int mouseY) {
		animate.update();

		TextUtil.drawClickGuiString(mod.getName(), parent.getX() + 2, (float) (parent.getY() + offset + (Xeno.moduleManager.isModuleEnabled("CustomFont") ? 2.5f : 3f)), parent.getX() + (parent.getWidth() / 2), parent.getX() + parent.getWidth(), mod.isEnabled() ? XenoGui.color : -1);

		if(open) {
			if(!subcomponents.isEmpty()) {
				double off = offset + XenoGui.buttonBarHeight;
				for(Component comp : subcomponents) {
					if(comp instanceof BooleanComponent) {
						comp.setOff(off);
						off += XenoGui.buttonBarHeight;
					}

					if(comp instanceof SliderComponent) {
						comp.setOff(off);
						off += XenoGui.buttonBarHeight;
					}

					if(comp instanceof ModeComponent) {
						comp.setOff(off);
						off += XenoGui.buttonBarHeight;
					}

					if(comp instanceof ColourComponent) {
						comp.setOff(off);
						off += XenoGui.buttonBarHeight + (((ColourComponent) comp).open ? XenoGui.buttonBarHeight * XenoGui.colourMultiplier : 0);
					}

					if(comp instanceof StringComponent) {
						comp.setOff(off);
						off += XenoGui.buttonBarHeight;
					}

					if(comp instanceof ButtonComponent) {
						comp.setOff(off);
						off += XenoGui.buttonBarHeight;
					}

					if(comp instanceof KeybindComponent) {
						comp.setOff(off);
						off += XenoGui.buttonBarHeight;
					}

					comp.renderComponent(mouseX, mouseY);
				}
			}
		}
	}
	
	@Override
	public int getHeight() {
		if(this.open) {
			int h = 0;
			for(Component c : this.subcomponents) {
				if(c instanceof ColourComponent && ((ColourComponent) c).open) {
					h += XenoGui.buttonBarHeight * XenoGui.colourMultiplier;
				}
			}
			return (XenoGui.buttonBarHeight * (this.subcomponents.size() + 1) + h);
		}

		return XenoGui.buttonBarHeight;
	}
	
	@Override
	public void updateComponent(int mouseX, int mouseY) {
		this.isHovered = isMouseOnButton(mouseX, mouseY);
		if(!this.subcomponents.isEmpty()) {
			for(Component comp : this.subcomponents) {
				comp.updateComponent(mouseX, mouseY);
			}
		}
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if(isMouseOnButton(mouseX, mouseY) && button == 0) {
			this.mod.toggle();

			if(ClickGuiModule.clickSound.enabled)
				GuiUtil.clickSound();
		}
		if(isMouseOnButton(mouseX, mouseY) && button == 1) {
			this.open = !open;
			this.parent.refresh();
			if (animate.getValue() > 30) {
				animate.setEase(Easing.EXPO_IN_OUT).setMin(0).setMax(this.mod.settings.size() * 13).setSpeed(200).setReversed(true);
			} else {
				animate.setEase(Easing.EXPO_IN_OUT).setMin(0).setMax(this.mod.settings.size() * 13).setSpeed(200).setReversed(false).reset();
			}
			if(ClickGuiModule.clickSound.enabled)
				GuiUtil.clickSound();
		}

		for(Component comp : this.subcomponents) {
			comp.mouseClicked(mouseX, mouseY, button);
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		for(Component comp : this.subcomponents) {
			comp.mouseReleased(mouseX, mouseY, mouseButton);
		}
	}
	
	@Override
	public void keyTyped(char typedChar, int key) {
		for(Component comp : this.subcomponents) {
			comp.keyTyped(typedChar, key);
		}
	}
	
	public boolean isMouseOnButton(int x, int y) {
		if(x > parent.getX() && x < parent.getX() + parent.getWidth() && y > this.parent.getY() + this.offset && y < this.parent.getY() + XenoGui.buttonBarHeight + this.offset) {
			return true;
		}
		return false;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}
}
