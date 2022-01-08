package me.xenodevs.xeno.gui.taskbar;

import me.wolfsurge.api.util.Globals;
import me.xenodevs.xeno.gui.GuiUtil;
import me.xenodevs.xeno.gui.taskbar.impl.GuiElement;
import me.xenodevs.xeno.gui.taskbar.impl.HudElement;
import me.xenodevs.xeno.managers.FontManager;
import me.xenodevs.xeno.utils.render.ColorUtil;
import me.xenodevs.xeno.utils.render.RenderUtils2D;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import java.util.ArrayList;
import static org.lwjgl.opengl.GL11.*;

public class Taskbar implements Globals {

    public ArrayList<TaskbarElement> elements;

    public Taskbar() {
        elements = new ArrayList<>();
        init();
    }

    public void init() {
        elements.add(new GuiElement());
        elements.add(new HudElement());
    }

    public void renderTaskbar(int mouseX, int mouseY) {
        glPushMatrix();
        ScaledResolution sr = new ScaledResolution(mc);
        RenderUtils2D.drawRoundedRect(-10, sr.getScaledHeight() - 70, elements.size() * 64 + 20, sr.getScaledHeight() + 10, 10, 0x90000000);

        RenderUtils2D.drawRect(0, sr.getScaledHeight() - 100, FontManager.comfortaaBig.getStringWidth("Xeno Client") + 10, sr.getScaledHeight() - 70, 0x90000000);
        RenderUtils2D.drawLeftGradientRect(0, sr.getScaledHeight() - 100, FontManager.comfortaaBig.getStringWidth("Xeno Client") + 10, sr.getScaledHeight() - 98, ColorUtil.rainbowWave(4, 1, 1, 0), ColorUtil.rainbowWave(4, 1, 1, 100));

        FontManager.comfortaaBig.drawStringWithShadow("Xeno Client", 5, sr.getScaledHeight() - 90, -1);
        glPopMatrix();

        for(TaskbarElement element : elements) {
            glPushMatrix();
            element.update();
            element.render();
            GL11.glPopMatrix();
        }
    }

    public void click(int mouseX, int mouseY, int mouseButton) {
        if(mouseButton == 0) {
            for(TaskbarElement element : elements) {
                if(GuiUtil.mouseOver(element.x, element.y, element.x + 64, element.y + 64, mouseX, mouseY)) {
                    element.onClick();
                }
            }
        }
    }
}
