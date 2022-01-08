package me.xenodevs.xeno.gui;

import me.wolfsurge.api.TextUtil;
import me.wolfsurge.api.util.Globals;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.hud.HudConfig;
import me.xenodevs.xeno.module.modules.client.ClickGuiModule;
import me.xenodevs.xeno.module.modules.client.Colors;
import me.xenodevs.xeno.utils.render.RenderUtils2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

import javax.annotation.Nullable;
import java.awt.*;

import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glScissor;

public class GuiUtil implements Globals {

    public static boolean customMainMenu = true;

    public static void renderButtons(int mouseX, int mouseY) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        // ClickGUI Button
        int x = (sr.getScaledWidth() / 2) - 70;
        int y = 0;
        int width = 70;
        int height = 15;

        RenderUtils2D.drawRoundedRect(x, y, width, height, 1, 2, 1, 1, (mouseOver(x, y, x + width, y + height, mouseX, mouseY) ? Colors.colourInt : Color.DARK_GRAY.darker().getRGB()));

        TextUtil.drawCenteredString("ClickGUI", x + (width / 2), y + 2, -1);

        // HUD Editor Button
        int x2 = (sr.getScaledWidth() / 2);
        int y2 = 0;
        int width2 = 70;
        int height2 = 15;

        RenderUtils2D.drawRoundedRect(x2, y2, width2, height2, 1, 1, 2, 1, (mouseOver(x2, y2, x2 + width2, y2 + height2, mouseX, mouseY) ? Colors.colourInt : Color.DARK_GRAY.darker().getRGB()));

        TextUtil.drawCenteredString("HUD", x2 + (width / 2), y2 + 2, -1);

        RenderUtils2D.drawRoundedOutline(x, (double) y - 5, width + width2, height + 5, 2, 2, Colors.colourInt);
    }

    public static void handleButtons(int mouseX, int mouseY) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        // ClickGUI Button
        int x = (sr.getScaledWidth() / 2) - 70;
        int y = 0;
        int width = 70;
        int height = 15;
        if(mouseOver(x, y, x + width, y + height, mouseX, mouseY)) {
            mc.displayGuiScreen(Xeno.clickGui);
            if(ClickGuiModule.clickSound.enabled)
                clickSound();
        }

        // ClickGUI Button
        int x2 = (sr.getScaledWidth() / 2);
        int y2 = 0;
        int width2 = 70;
        int height2 = 15;
        if(mouseOver(x2, y2, x2 + width2, y2 + height2, mouseX, mouseY)) {
            mc.displayGuiScreen(new HudConfig());
            if(ClickGuiModule.clickSound.enabled)
                clickSound();
        }
    }

    public static boolean mouseOver(double minX, double minY, double maxX, double maxY, int mX, int mY) {
        return mX >= minX && mY >= minY && mX <= maxX && mY <= maxY;
    }

    public static void clickSound() {
        mc.getSoundHandler().playSound(new ISound() {

            @Override
            public ResourceLocation getSoundLocation() {
                return new ResourceLocation("cosmos", "sounds/click.ogg");
            }

            @Nullable
            @Override
            public SoundEventAccessor createAccessor(SoundHandler handler) {
                return new SoundEventAccessor(new ResourceLocation("cosmos", "sounds/click.ogg"), "click");
            }

            @Override
            public Sound getSound() {
                return new Sound("click", 1, 1, 1, Sound.Type.SOUND_EVENT, false);
            }

            @Override
            public SoundCategory getCategory() {
                return SoundCategory.VOICE;
            }

            @Override
            public boolean canRepeat() {
                return false;
            }

            @Override
            public int getRepeatDelay() {
                return 0;
            }

            @Override
            public float getVolume() {
                return 1;
            }

            @Override
            public float getPitch() {
                return 1;
            }

            @Override
            public float getXPosF() {
                return mc.player != null ? (float) mc.player.posX : 0;
            }

            @Override
            public float getYPosF() {
                return mc.player != null ? (float) mc.player.posY: 0;
            }

            @Override
            public float getZPosF() {
                return mc.player != null ? (float) mc.player.posZ : 0;
            }

            @Override
            public AttenuationType getAttenuationType() {
                return AttenuationType.LINEAR;
            }
        });
    }

    static Minecraft mc = Minecraft.getMinecraft();
    private static final BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
    private static final Tessellator tessellator = Tessellator.getInstance();

    public static final void scissor(double x, double y, double width, double height) {
        ScaledResolution sr = new ScaledResolution(mc);
        final double scale = sr.getScaleFactor();

        y = sr.getScaledHeight() - y;

        x *= scale;
        y *= scale;
        width *= scale;
        height *= scale;

        glScissor((int) x, (int) (y - height), (int) width, (int) height);
    }

    public static void drawBorderedRect(double x, double y, double width, double height, double lineSize, int borderColor, int color) {
        RenderUtils2D.drawRect(x, y, x + width, y + height, color);
        RenderUtils2D.drawRect(x, y, x + width, y + lineSize, borderColor);
        RenderUtils2D.drawRect(x, y, x + lineSize, y + height, borderColor);
        RenderUtils2D.drawRect(x + width, y, x + width - lineSize, y + height, borderColor);
        RenderUtils2D.drawRect(x, y + height, x + width, y + height - lineSize, borderColor);
    }

    public static Color shadeColour(Color color, int precent) {
        int r = (color.getRed() * (100 + precent) / 100);
        int g = (color.getGreen() * (100 + precent) / 100);
        int b = (color.getBlue() * (100 + precent) / 100);
        return new Color(r,g,b);
    }

    public static int shadeColour(int color, int precent) {
        int r = (((color & 0xFF0000) >> 16) * (100 + precent) / 100);
        int g = (((color & 0xFF00) >> 8) * (100 + precent) / 100);
        int b = ((color & 0xFF) * (100 + precent) / 100);
        return new Color(r,g,b).hashCode();
    }

    public static Color releasedDynamicRainbow(final int delay, int alpha) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        rainbowState %= 360.0;

        Color c = Color.getHSBColor((float) (rainbowState / 360.0), 1f, 1f);
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
    }

    public static void hexColor(int hexColor) {
        float red = (float) (hexColor >> 16 & 0xFF) / 255.0f;
        float green = (float) (hexColor >> 8 & 0xFF) / 255.0f;
        float blue = (float) (hexColor & 0xFF) / 255.0f;
        float alpha = (float) (hexColor >> 24 & 0xFF) / 255.0f;
        glColor4f(red, green, blue, alpha);
    }

    public static Color getSinState(final Color c1, final double delay, final int a, final type t) {
        double sineState;
        sineState = Math.sin(2400 - System.currentTimeMillis() / delay) * Math.sin(2400 - System.currentTimeMillis() / delay);
        float[] hsb = Color.RGBtoHSB(c1.getRed(), c1.getGreen(), c1.getBlue(), null);
        Color c = null;
        switch (t) {
            case HUE:
                sineState /= hsb[0];
                sineState = Math.min(1.0, sineState);
                c = Color.getHSBColor((float) sineState, 1f, 1f);
                break;
            case SATURATION:
                sineState /= hsb[1];
                sineState = Math.min(1.0, sineState);
                c = Color.getHSBColor(hsb[0], (float) sineState, 1f);
                break;
            case BRIGHTNESS:
                sineState /= hsb[2];
                sineState = Math.min(1.0, sineState);
                c = Color.getHSBColor(hsb[0], 1f, (float) sineState);
                break;
            case SPECIAL:
                sineState /= hsb[1];
                sineState = Math.min(1.0, sineState);
                c = Color.getHSBColor(hsb[0], 1f, (float) sineState);
                break;
        }
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), a);
    }

    public static Color getSinState(final Color c1, final Color c2, final double delay, final int a) {
        double sineState;
        sineState = Math.sin(2400 - System.currentTimeMillis() / delay) * Math.sin(2400 - System.currentTimeMillis() / delay);
        float[] hsb = Color.RGBtoHSB(c1.getRed(), c1.getGreen(), c1.getBlue(), null);
        float[] hsb2 = Color.RGBtoHSB(c2.getRed(), c2.getGreen(), c2.getBlue(), null);
        Color c;
        sineState /= hsb[0];
        sineState *= sineState/hsb2[0];
        sineState = Math.min(1.0, sineState);
        c = Color.getHSBColor((float) sineState, 1f, 1f);

        return new Color(c.getRed(), c.getGreen(), c.getBlue(), a);
    }

    public enum type {
        HUE,
        SATURATION,
        BRIGHTNESS,
        SPECIAL
    }

    public static int rainbow(int delay) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / 360.0f), (float) 0.5, (float) 1).getRGB();
    }

    public static int rainbowVibrant(int delay) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / 360.0f), (float) 1, (float) 1).getRGB();
    }

    public static int rainbowWave(float seconds, float saturation, float brightness, int index) {
        float hue = ((System.currentTimeMillis() + index) % (int)(seconds * 1000) / (float)(seconds * 1000));
        int color = Color.HSBtoRGB(hue, saturation, brightness);
        return color;
    }

    public static int getDisplayWidth() {
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) size.getWidth() / 2;
        return width;
    }

    public static int getDisplayHeight() {
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        int height = (int) size.getHeight() / 2;
        return height;
    }
}
