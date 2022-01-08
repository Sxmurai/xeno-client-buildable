package me.xenodevs.xeno.utils.render;

import com.google.gson.JsonSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class BlurUtil {

    private static final Minecraft MC = Minecraft.getMinecraft();
    private final ResourceLocation resourceLocation;
    private ShaderGroup shaderGroup;
    private Framebuffer framebuffer;

    private int lastFactor;
    private int lastWidth;
    private int lastHeight;

    public BlurUtil() {
        this.resourceLocation = new ResourceLocation("xeno", ("shader/blur.json"));
    }

    public void init() {
        try {
            this.shaderGroup = new ShaderGroup(MC.getTextureManager(), MC.getResourceManager(), MC.getFramebuffer(), resourceLocation);
            this.shaderGroup.createBindFramebuffers(MC.displayWidth, MC.displayHeight);
            this.framebuffer = shaderGroup.mainFramebuffer;
        } catch (JsonSyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    private void setValues(int strength) {
        this.shaderGroup.listShaders.get(0).getShaderManager().getShaderUniform("Radius").set(strength);
        this.shaderGroup.listShaders.get(1).getShaderManager().getShaderUniform("Radius").set(strength);
        this.shaderGroup.listShaders.get(2).getShaderManager().getShaderUniform("Radius").set(strength);
        this.shaderGroup.listShaders.get(3).getShaderManager().getShaderUniform("Radius").set(strength);
    }

    public final void blur(int blurStrength) {
        final ScaledResolution scaledResolution = new ScaledResolution(MC);

        final int scaleFactor = scaledResolution.getScaleFactor();
        final int width = scaledResolution.getScaledWidth();
        final int height = scaledResolution.getScaledHeight();

        if (sizeHasChanged(scaleFactor, width, height) || framebuffer == null || shaderGroup == null) {
            init();
        }

        this.lastFactor = scaleFactor;
        this.lastWidth = width;
        this.lastHeight = height;

        setValues(blurStrength);
        framebuffer.bindFramebuffer(true);
        shaderGroup.render(MC.timer.renderPartialTicks);
        MC.getFramebuffer().bindFramebuffer(true);
        GlStateManager.enableAlpha();
    }

    public final void blur(double x, double y, double areaWidth, double areaHeight, int blurStrength) {
        final ScaledResolution scaledResolution = new ScaledResolution(MC);

        final int scaleFactor = scaledResolution.getScaleFactor();
        final int width = scaledResolution.getScaledWidth();
        final int height = scaledResolution.getScaledHeight();

        if (sizeHasChanged(scaleFactor, width, height) || framebuffer == null || shaderGroup == null) {
            init();
        }

        this.lastFactor = scaleFactor;
        this.lastWidth = width;
        this.lastHeight = height;

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtils2D.scissor(x, y + 1, areaWidth, areaHeight - 1);
        framebuffer.bindFramebuffer(true);
        shaderGroup.render(MC.timer.renderPartialTicks);
        setValues(blurStrength);
        MC.getFramebuffer().bindFramebuffer(false);

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    private boolean sizeHasChanged(int scaleFactor, int width, int height) {
        return (lastFactor != scaleFactor || lastWidth != width || lastHeight != height);
    }

}
