package me.xenodevs.xeno.gui.hud.modules.impl;

import java.util.ArrayList;
import java.util.Comparator;

import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.hud.modules.HUDMod;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.modules.client.Colors;
import me.xenodevs.xeno.module.modules.hud.ArrayListModule;
import me.xenodevs.xeno.utils.render.ColorUtil;
import me.xenodevs.xeno.utils.render.DisplayUtils;
import me.xenodevs.xeno.utils.render.RenderUtils2D;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.TextFormatting;

public class HUDArrayList extends HUDMod {

	public HUDArrayList() {
		super("ArrayList", DisplayUtils.getDisplayWidth() - TextUtil.getStringWidth("ArrayList"), 0, Xeno.moduleManager.getModule("ArrayList"));
	}

	public int longestModName = 0;
	public int length = 0;
	ArrayList<Module> enabledModules = new ArrayList<Module>();

	@Override
	public void draw() {
		int count = 0;
		update();
		enabledModules = new ArrayList<Module>();

		String mode = "FromRight";
		String vertMode = "Top";

		for(Module m : Xeno.moduleManager.getModules()) {
			if(m.isEnabled() && m.visible.enabled)
				enabledModules.add(m);
		}

		ScaledResolution sr = new ScaledResolution(mc);

		if(this.getX() + (this.getWidth() / 2) < sr.getScaledWidth() / 2) {
			mode = "FromLeft";
		}

		int textSeperation = ArrayListModule.background.isEnabled() ? 12 : 10;

		float sec = (float)ArrayListModule.rainbowSpeed.getDoubleValue();

		int seperation = 150;

		float yOffset = 0;
		float xOffset = 0;

		for(Module m : enabledModules) {
			String text = m.name + TextFormatting.GRAY + m.getHUDData();
			int rainbowThing = parent.enabled ? ArrayListModule.rainbowWave.enabled ? ColorUtil.rainbowWave(sec, 1, 1, count * seperation) : Colors.colourInt : 0xFF900000;
			int rainbowTwo = parent.enabled ? ArrayListModule.rainbowWave.enabled ? ColorUtil.rainbowWave(sec, 1, 1, (count + 1) * seperation) : Colors.colourInt : 0xFF900000;

			if(vertMode == "Top") {
				Xeno.moduleManager.getModules().sort(Comparator.comparingInt(mod -> TextUtil.getStringWidth(((Module) mod).name + TextFormatting.GRAY + ((Module) mod).getHUDData())).reversed());
				if(ArrayListModule.background.enabled) {
					if(mode == ("FromRight"))
						Gui.drawRect((int) (getX() + drag.getWidth() - 5 - TextUtil.getStringWidth(text)), getY() + (count * textSeperation), getX() + getWidth(), getY() + (count * textSeperation) + textSeperation, 0x70000000);
					else if(mode == ("FromLeft"))
						Gui.drawRect(getX(), getY() + (count * textSeperation), getX() + TextUtil.getStringWidth(text) + 5, getY() + (count * textSeperation) + textSeperation, 0x70000000);
				}

				if(ArrayListModule.outline.enabled && mode == ("FromRight") && ArrayListModule.background.enabled)
					RenderUtils2D.drawGradientRect(getX() + drag.getWidth() - 6 - TextUtil.getStringWidth(text), getY() + (count * textSeperation), getX() + getWidth() - 5 - TextUtil.getStringWidth(text), getY() + (count * textSeperation) + textSeperation, rainbowThing, rainbowTwo, false);
				if(ArrayListModule.outline.enabled && mode == ("FromLeft") && ArrayListModule.background.enabled)
					Gui.drawRect(getX() + TextUtil.getStringWidth(text) + 5, getY() + (count * textSeperation), getX() + TextUtil.getStringWidth(text) + 6, getY() + (count * textSeperation) + textSeperation, rainbowThing);

				if(mode == ("FromRight")) {
					int formula = getY() + (count * textSeperation);

					TextUtil.drawStringWithShadow(text, (int) (getX() + drag.getWidth() - 3 - TextUtil.getStringWidth(text)), formula, rainbowThing);
				} else if(mode == ("FromLeft")) {
					int formula = getY() + (count * textSeperation);

					TextUtil.drawStringWithShadow(text, getX() + 2, formula, rainbowThing);
				}

				if(ArrayListModule.outline.enabled && ArrayListModule.background.enabled) {
					if(mode == "FromRight") {
						int diff = 0;
						int diff2 = 0;
						int thing = 5;
						String text2 = "";

						if(!enabledModules.isEmpty()) {
							if(enabledModules.get(0) == m) {
								RenderUtils2D.drawGradientRect(getX() + drag.getWidth() - 6 - TextUtil.getStringWidth(text), getY() + (count * textSeperation), getX() + getWidth(), getY() + (count * textSeperation) + 1, rainbowThing, rainbowTwo, false);
							}
						}

						try {
							text2 = enabledModules.get(count + 1).name + TextFormatting.GRAY + enabledModules.get(count + 1).getHUDData();
							diff = Math.min(TextUtil.getStringWidth(text), TextUtil.getStringWidth(text2));
						} catch (Exception e) {
							thing = 0;
						}

						RenderUtils2D.drawGradientRect(getX() + drag.getWidth() - 6 - TextUtil.getStringWidth(text), getY() + (count * textSeperation) + 11, getX() + getWidth() - diff - thing + diff2, getY() + (count * textSeperation) + textSeperation, rainbowThing, rainbowTwo, false);
						RenderUtils2D.drawGradientRect(getX() + drag.getWidth() - 1, getY() + (count * textSeperation), getX() + drag.getWidth(), getY() + (count * textSeperation) + 12, rainbowThing, rainbowTwo, false);
					} else if (mode == "FromLeft") {
						if(!enabledModules.isEmpty()) {
							if(enabledModules.get(0) == m) {
								RenderUtils2D.drawGradientRect(getX(), getY() + (count * textSeperation), getX() + TextUtil.getStringWidth(text) + 5, getY() + (count * textSeperation) + 1, rainbowThing, rainbowTwo, false);
							}
						}

						int diff = 0;
						String text2 = "";

						try {
							text2 = enabledModules.get(count + 1).name + TextFormatting.GRAY + enabledModules.get(count + 1).getHUDData();
							diff = TextUtil.getStringWidth(text) - TextUtil.getStringWidth(text2);
						} catch(Exception e) {
							diff = TextUtil.getStringWidth(text) + 5;
						}

						Gui.drawRect(this.getX() + TextUtil.getStringWidth(text) + 5, getY() + (count * textSeperation) + 11, (this.getX() + TextUtil.getStringWidth(text) + 6) - diff - 1, getY() + (count * textSeperation) + 12, rainbowThing);
						RenderUtils2D.drawGradientRect(getX(), getY() + (count * textSeperation), getX() + 1, getY() + (count * textSeperation) + textSeperation, rainbowThing, rainbowTwo, false);
					}
				}

				if(TextUtil.getStringWidth(text) > longestModName) {
					longestModName = TextUtil.getStringWidth(text);
				}
			}

			count++;
		}

		if(!enabledModules.isEmpty())
			drag.setHeight(count*textSeperation);
		drag.setWidth(getWidth());

		super.draw();
	}

	@Override
	public void renderDummy(int mouseX, int mouseY) {
		super.renderDummy(mouseX, mouseY);

		if(!drag.isMouseOver)
			update();

		int count = 0;

		enabledModules = new ArrayList<Module>();

		String mode = "FromRight";
		String vertMode = "Top";

		for(Module m : Xeno.moduleManager.modules) {
			if(m.isEnabled() && m.visible.enabled)
				enabledModules.add(m);
		}

		ScaledResolution sr = new ScaledResolution(mc);

		if(this.getX() + (this.getWidth() / 2) < sr.getScaledWidth() / 2) {
			mode = "FromLeft";
		}

		int textSeperation = ArrayListModule.background.isEnabled() ? 12 : 10;

		if(enabledModules.isEmpty()) {
			drag.setHeight(11);
			drag.setWidth(TextUtil.getStringWidth(name));
			TextUtil.drawStringWithShadow(name, drag.getX(), drag.getY() + 2, this.isEnabled() ? Colors.colourInt : 0x90900000);
			super.renderDummy(mouseX, mouseY);
			return;
		}

		float sec = (float) ArrayListModule.rainbowSpeed.getDoubleValue();

		int seperation = 150;

		for(Module m : enabledModules) {
			String text = m.name + TextFormatting.GRAY + m.getHUDData();
			int rainbowThing = (parent.enabled ? (ArrayListModule.rainbowWave.enabled ? ColorUtil.rainbowWave(sec, 1, 1, count * seperation) : Colors.colourInt) : 0xFF900000);
			int rainbowTwo = (parent.enabled ? (ArrayListModule.rainbowWave.enabled ? ColorUtil.rainbowWave(sec, 1, 1, (count + 1) * seperation) : Colors.colourInt) : 0xFF900000);

			if(vertMode == "Top") {
				Xeno.moduleManager.modules.sort(Comparator.comparingInt(mod -> TextUtil.getStringWidth(((Module) mod).name + TextFormatting.GRAY + ((Module) mod).getHUDData())).reversed());
				if(ArrayListModule.background.enabled) {
					if(mode == ("FromRight"))
						Gui.drawRect((int) (getX() + drag.getWidth() - 5 - TextUtil.getStringWidth(text)), getY() + (count * textSeperation), getX() + getWidth(), getY() + (count * textSeperation) + textSeperation, 0x70000000);
					else if(mode == ("FromLeft"))
						Gui.drawRect(getX(), getY() + (count * textSeperation), getX() + TextUtil.getStringWidth(text) + 5, getY() + (count * textSeperation) + textSeperation, 0x70000000);
				}

				if(ArrayListModule.outline.enabled && mode == ("FromRight") && ArrayListModule.background.enabled)
					RenderUtils2D.drawGradientRect(getX() + drag.getWidth() - 6 - TextUtil.getStringWidth(text), getY() + (count * textSeperation), getX() + getWidth() - 5 - TextUtil.getStringWidth(text), getY() + (count * textSeperation) + textSeperation, rainbowThing, rainbowTwo, false);
				if(ArrayListModule.outline.enabled && mode == ("FromLeft") && ArrayListModule.background.enabled)
					Gui.drawRect(getX() + TextUtil.getStringWidth(text) + 5, getY() + (count * textSeperation), getX() + TextUtil.getStringWidth(text) + 6, getY() + (count * textSeperation) + textSeperation, rainbowThing);

				if(mode == ("FromRight")) {
					int formula = getY() + (count * textSeperation);

					TextUtil.drawStringWithShadow(text, (int) (getX() + drag.getWidth() - 3 - TextUtil.getStringWidth(text)), formula, rainbowThing);
				} else if(mode == ("FromLeft")) {
					int formula = getY() + (count * textSeperation);

					TextUtil.drawStringWithShadow(text, getX() + 2, formula, rainbowThing);
				}

				if(ArrayListModule.outline.enabled && ArrayListModule.background.enabled) {
					if(mode == "FromRight") {
						int diff = 0;
						int diff2 = 0;
						int thing = 5;
						String text2 = "";

						if(!enabledModules.isEmpty()) {
							if(enabledModules.get(0) == m) {
								RenderUtils2D.drawGradientRect(getX() + drag.getWidth() - 6 - TextUtil.getStringWidth(text), getY() + (count * textSeperation), getX() + getWidth(), getY() + (count * textSeperation) + 1, rainbowThing, rainbowTwo, false);
							}
						}

						try {
							text2 = enabledModules.get(count + 1).name + TextFormatting.GRAY + enabledModules.get(count + 1).getHUDData();
							diff = Math.min(TextUtil.getStringWidth(text), TextUtil.getStringWidth(text2));
						} catch (Exception e) {
							thing = 0;
						}

						RenderUtils2D.drawGradientRect(getX() + drag.getWidth() - 6 - TextUtil.getStringWidth(text), getY() + (count * textSeperation) + 11, getX() + getWidth() - diff - thing + diff2, getY() + (count * textSeperation) + textSeperation, rainbowThing, rainbowTwo, false);
						RenderUtils2D.drawGradientRect(getX() + drag.getWidth() - 1, getY() + (count * textSeperation), getX() + drag.getWidth(), getY() + (count * textSeperation) + 12, rainbowThing, rainbowTwo, false);
					} else if (mode == "FromLeft") {
						if(!enabledModules.isEmpty()) {
							if(enabledModules.get(0) == m) {
								RenderUtils2D.drawGradientRect(getX(), getY() + (count * textSeperation) , getX() + TextUtil.getStringWidth(text) + 5, getY() + (count * textSeperation) + 1, rainbowThing, rainbowTwo, false);
							}
						}

						int diff = 0;
						String text2 = "";

						try {
							text2 = enabledModules.get(count + 1).name + TextFormatting.WHITE + enabledModules.get(count + 1).getHUDData();
							diff = TextUtil.getStringWidth(text) - TextUtil.getStringWidth(text2);
						} catch(Exception e) {
							diff = TextUtil.getStringWidth(text) + 5;
						}

						Gui.drawRect(this.getX() + TextUtil.getStringWidth(text) + 5, getY() + (count * textSeperation) + 11, (this.getX() + TextUtil.getStringWidth(text) + 6) - diff - 1, getY() + (count * textSeperation) + 12, rainbowThing);
						RenderUtils2D.drawGradientRect(getX(), getY() + (count * textSeperation), getX() + 1, getY() + (count * textSeperation) + textSeperation, rainbowThing, rainbowTwo, false);
					}
				}

				if(TextUtil.getStringWidth(text) > longestModName) {
					longestModName = TextUtil.getStringWidth(text);
				}
			}

			if(ArrayListModule.outline.enabled && ArrayListModule.background.enabled && vertMode == "Bottom") {
				if(mode == "FromRight") {
					RenderUtils2D.drawGradientRect(getX() + getWidth() - 1, getY(), getX() + getWidth(), getY() + (count * textSeperation) + textSeperation, ((parent.enabled ? ArrayListModule.rainbowWave.enabled ? ColorUtil.rainbowWave(sec, 1, 1, 0) : Colors.colourInt : 0xFF900000)), rainbowThing, false);
				} else if(mode == "FromLeft") {
					RenderUtils2D.drawGradientRect(getX(), getY(), getX() + 1, getY() + (count * textSeperation) + textSeperation, ((parent.enabled ? ArrayListModule.rainbowWave.enabled ? ColorUtil.rainbowWave(sec, 1, 1, 0) : Colors.colourInt : 0xFF900000)), rainbowThing, false);
				}
			}

			count++;
		}

		if(!enabledModules.isEmpty())
			drag.setHeight(count*textSeperation);
		drag.setWidth(getWidth());
	}

	@Override
	public int getWidth() {
		return longestModName + 4;
	}

	@Override
	public float getHeight() {
		return 10;
	}
}
