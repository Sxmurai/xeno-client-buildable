package me.xenodevs.xeno.module.modules.hud;

import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.modules.client.Colors;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.utils.render.ColorUtil;
import me.xenodevs.xeno.utils.render.RenderUtils2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import javax.jws.WebParam;
import java.util.ArrayList;
import java.util.Comparator;

public class ArrayListModule extends Module {

    public static ModeSetting anchor = new ModeSetting("Anchor", "B Right", "B Left", "T Left", "T Right");
    public static BooleanSetting rainbowWave = new BooleanSetting("Rainbow", true);
    public static BooleanSetting outline = new BooleanSetting("Outline", true);
    public static BooleanSetting background = new BooleanSetting("Background", true);
    public static NumberSetting rainbowSpeed = new NumberSetting("Speed", 4, 0.1, 10, 0.1);

    public ArrayListModule() {
        super("ArrayList", "Displays the enabled modules on screen.", Category.HUD, true);
        this.addSettings(anchor, rainbowWave, outline, background, rainbowSpeed);
    }

    public static void drawArrayList() {
        if(Xeno.moduleManager.getModule("ArrayList").isEnabled()) {
            int length = 11;
            Minecraft mc = Minecraft.getMinecraft();
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            ArrayList<Module> enabledModules = new ArrayList<>();
            for (Module m : Xeno
                    .moduleManager.getModules())
                if (m.isEnabled()
                        && m.visible
                        .isEnabled())
                    enabledModules.add(m);

            if(anchor.is("T Right")) {
                float xOffset = sr.getScaledWidth() - 2;
                float yOffset = 0;
                enabledModules.sort(Comparator.comparingInt(mod -> TextUtil.getStringWidth(((Module) mod).name + TextFormatting.GRAY + ((Module) mod).getHUDData())).reversed());

                int index = 0;
                int count = 0;
                for(Module m : enabledModules) {
                    int col = rainbowWave.isEnabled() ? ColorUtil.rainbowWave(rainbowSpeed.getFloatValue(), 1, 1, index) : Colors.colourInt;
                    int outLineCol1 = rainbowWave.isEnabled() ? ColorUtil.rainbowWave(rainbowSpeed.getFloatValue(), 1, 1, index) : Colors.colourInt;
                    int outLineCol2 = rainbowWave.isEnabled() ? ColorUtil.rainbowWave(rainbowSpeed.getFloatValue(), 1, 1, index + 150) : Colors.colourInt;

                    if(background.isEnabled()) {
                        RenderUtils2D.drawRect(xOffset - 2 - TextUtil.getStringWidth(((Module) m).name + ((Module) m).getHUDData()), yOffset, xOffset + 2, yOffset + length, 0x90000000);
                    }

                    if(outline.isEnabled()) {
                        RenderUtils2D.drawGradientRect(xOffset - 3 - TextUtil.getStringWidth(((Module) m).name + ((Module) m).getHUDData()), yOffset, xOffset - 2 - TextUtil.getStringWidth(((Module) m).name + ((Module) m).getHUDData()), yOffset + length, outLineCol1, outLineCol2, false);

                        String text = m.name + TextFormatting.GRAY + m.getHUDData();
                        int diff = 0;
                        int thing = 2;
                        String text2 = "";
                        try {
                            text2 = enabledModules.get(count + 1).name + TextFormatting.GRAY + enabledModules.get(count + 1).getHUDData();
                            diff = Math.min(TextUtil.getStringWidth(text), TextUtil.getStringWidth(text2));
                        } catch (Exception e) {
                            thing = -2;
                        }

                        RenderUtils2D.drawGradientRect(xOffset - 3 - TextUtil.getStringWidth(text), yOffset + length - 1, xOffset - diff - thing, yOffset + length, outLineCol1, outLineCol2, false);
                    }

                    TextUtil.drawStringWithShadow(m.name + TextFormatting.GRAY + m.getHUDData(), xOffset - TextUtil.getStringWidth(m.name + TextFormatting.GRAY + m.getHUDData()), yOffset - 1, col);
                    yOffset += length;
                    index += 150;
                    count++;
                }
            } else if(anchor.is("B Right")) {
                float xOffset = sr.getScaledWidth() - 2;
                float yOffset = sr.getScaledHeight() - length;
                enabledModules.sort(Comparator.comparingInt(mod -> TextUtil.getStringWidth(((Module) mod).name + TextFormatting.GRAY + ((Module) mod).getHUDData())).reversed());

                int index = 0;
                int count = 0;
                for(Module m : enabledModules) {
                    int col = rainbowWave.isEnabled() ? ColorUtil.rainbowWave(rainbowSpeed.getFloatValue(), 1, 1, index) : Colors.colourInt;

                    int outlineCol1 = rainbowWave.isEnabled() ? ColorUtil.rainbowWave(rainbowSpeed.getFloatValue(), 1, 1, index) : Colors.colourInt;
                    int outlineCol2 = rainbowWave.isEnabled() ? ColorUtil.rainbowWave(rainbowSpeed.getFloatValue(), 1, 1, index + 150) : Colors.colourInt;

                    if(background.isEnabled()) {
                        RenderUtils2D.drawRect(xOffset - 2 - TextUtil.getStringWidth(m.name + m.getHUDData()), yOffset, xOffset + 2, yOffset + length, 0x90000000);
                    }

                    if(outline.isEnabled()) {
                        RenderUtils2D.drawGradientRect(xOffset - 3 - TextUtil.getStringWidth(m.name + m.getHUDData()), yOffset, xOffset - 2 - TextUtil.getStringWidth(m.name + m.getHUDData()), yOffset + length, outlineCol1, outlineCol2, false);

                        String text = m.name + TextFormatting.GRAY + m.getHUDData();
                        int diff = 0;
                        int thing = 5;
                        String text2 = "";
                        try {
                            text2 = enabledModules.get(count + 1).name + TextFormatting.GRAY + enabledModules.get(count + 1).getHUDData();
                            diff = Math.min(TextUtil.getStringWidth(text), TextUtil.getStringWidth(text2));
                        } catch (Exception e) {
                            thing = 0;
                        }

                        RenderUtils2D.drawGradientRect(xOffset - 3 - TextUtil.getStringWidth(text), yOffset, xOffset - diff - thing + 3, yOffset + 1, outlineCol1, outlineCol2, false);
                    }

                    TextUtil.drawStringWithShadow(m.name + TextFormatting.GRAY + m.getHUDData(), xOffset - TextUtil.getStringWidth(m.name + TextFormatting.GRAY + m.getHUDData()), yOffset + 0.5f, col);
                    yOffset -= length;
                    index += 150;
                    count++;
                }
            } else if(anchor.is("B Left")) {
                float xOffset = 2;
                float yOffset = sr.getScaledHeight() - length;
                enabledModules.sort(Comparator.comparingInt(mod -> TextUtil.getStringWidth(((Module) mod).name + TextFormatting.GRAY + ((Module) mod).getHUDData())).reversed());

                int index = 0;
                int count = 0;
                for(Module m : enabledModules) {
                    int col = rainbowWave.isEnabled() ? ColorUtil.rainbowWave(rainbowSpeed.getFloatValue(), 1, 1, index) : Colors.colourInt;

                    int outlineCol1 = rainbowWave.isEnabled() ? ColorUtil.rainbowWave(rainbowSpeed.getFloatValue(), 1, 1, index) : Colors.colourInt;
                    int outlineCol2 = rainbowWave.isEnabled() ? ColorUtil.rainbowWave(rainbowSpeed.getFloatValue(), 1, 1, index + 150) : Colors.colourInt;

                    if(background.isEnabled()) {
                        RenderUtils2D.drawRect(xOffset - 2, yOffset, xOffset + 2 + TextUtil.getStringWidth(m.name + m.getHUDData()), yOffset + length, 0x90000000);
                    }

                    if(outline.isEnabled()) {
                        RenderUtils2D.drawGradientRect(xOffset + 2 + TextUtil.getStringWidth(m.name + m.getHUDData()), yOffset, xOffset + TextUtil.getStringWidth(m.name + m.getHUDData()) + 3, yOffset + length, outlineCol1, outlineCol2, false);

                        String text = m.name + TextFormatting.GRAY + m.getHUDData();
                        int diff = 0;
                        String text2 = "";

                        try {
                            text2 = enabledModules.get(count + 1).name + TextFormatting.WHITE + enabledModules.get(count + 1).getHUDData();
                            diff = TextUtil.getStringWidth(text) - TextUtil.getStringWidth(text2);
                        } catch(Exception e) {
                            diff = TextUtil.getStringWidth(text) + 5;
                        }

                        GL11.glPushMatrix();
                        GL11.glTranslated(2, 0, 0);
                        RenderUtils2D.drawRect(xOffset + TextUtil.getStringWidth(text) - (diff), yOffset, xOffset + TextUtil.getStringWidth(text), yOffset + 1, outlineCol1);
                        GL11.glPopMatrix();
                    }

                    TextUtil.drawStringWithShadow(m.name + TextFormatting.GRAY + m.getHUDData(), xOffset, yOffset + 0.5f, col);
                    yOffset -= length;
                    index += 150;
                    count++;
                }
            } else if(anchor.is("T Left")) {
                float xOffset = 0;
                float yOffset = 0;
                enabledModules.sort(Comparator.comparingInt(mod -> TextUtil.getStringWidth(((Module) mod).name + TextFormatting.GRAY + ((Module) mod).getHUDData())).reversed());

                int index = 0;
                int count = 0;
                for(Module m : enabledModules) {
                    int col = rainbowWave.isEnabled() ? ColorUtil.rainbowWave(rainbowSpeed.getFloatValue(), 1, 1, index) : Colors.colourInt;

                    int outLineCol1 = rainbowWave.isEnabled() ? ColorUtil.rainbowWave(rainbowSpeed.getFloatValue(), 1, 1, index) : Colors.colourInt;
                    int outLineCol2 = rainbowWave.isEnabled() ? ColorUtil.rainbowWave(rainbowSpeed.getFloatValue(), 1, 1, index + 150) : Colors.colourInt;

                    if(background.isEnabled()) {
                        RenderUtils2D.drawRect(xOffset, yOffset, xOffset + TextUtil.getStringWidth(m.name + m.getHUDData()) + 2, yOffset + length, 0x90000000);
                    }

                    if(outline.isEnabled()) {
                        RenderUtils2D.drawGradientRect(xOffset - 3 - TextUtil.getStringWidth(m.name + m.getHUDData()), yOffset, xOffset - TextUtil.getStringWidth(((Module) m).name + ((Module) m).getHUDData()), yOffset + length, outLineCol1, outLineCol2, false);

                        String text = m.name + TextFormatting.GRAY + m.getHUDData();
                        int diff = 0;
                        String text2 = "";

                        try {
                            text2 = enabledModules.get(count + 1).name + TextFormatting.WHITE + enabledModules.get(count + 1).getHUDData();
                            diff = TextUtil.getStringWidth(text) - TextUtil.getStringWidth(text2);
                        } catch(Exception e) {
                            diff = TextUtil.getStringWidth(text) + 5;
                        }

                        RenderUtils2D.drawRect(xOffset + TextUtil.getStringWidth(text) + 2, yOffset, xOffset + TextUtil.getStringWidth(text) + 3, yOffset + length +1, outLineCol1);

                        RenderUtils2D.drawRect(xOffset + TextUtil.getStringWidth(text) + 2, yOffset + length, (xOffset + TextUtil.getStringWidth(text) + 4) - diff - 2, yOffset + length + 1, outLineCol1);
                    }

                    TextUtil.drawStringWithShadow(m.name + TextFormatting.GRAY + m.getHUDData(), xOffset, yOffset, col);
                    yOffset += length;
                    index += 150;
                    count++;
                }
            }
        }
    }
}
