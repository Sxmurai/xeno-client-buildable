package me.xenodevs.xeno.gui.taskbar.impl;

import me.xenodevs.xeno.gui.taskbar.TaskbarElement;
import net.minecraft.client.gui.Gui;

import static org.lwjgl.opengl.GL11.glScaled;

public class HudElement extends TaskbarElement {

    public HudElement() {
        super("HUD", "textures/hud.png", 64);
    }

    @Override
    public void render() {
        glScaled(0.5, 0.5, 0.5);
        mc.getTextureManager().bindTexture(this.imageLocation);
        Gui.drawModalRectWithCustomSizedTexture(x*2, y*2, 0.0F, 0.0F, 64, 64, 64, 64);
    }

    @Override
    public void onClick() {

    }
}
