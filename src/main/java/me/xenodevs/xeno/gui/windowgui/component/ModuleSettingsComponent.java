package me.xenodevs.xeno.gui.windowgui.component;

import static org.lwjgl.opengl.GL11.*;

import java.awt.*;
import java.util.ArrayList;

import me.wolfsurge.api.TextUtil;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.GuiUtil;
import me.xenodevs.xeno.managers.FontManager;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.modules.client.Colors;
import me.xenodevs.xeno.utils.render.RenderUtils2D;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

public class ModuleSettingsComponent extends Component {

    static Minecraft mc = Minecraft.getMinecraft();
    public ArrayList<Component> settingComponents;
    double x, y, width, height;
    Module m;
    boolean isDragging = false;
    public double dragX;
    public double dragY;
    public Category currentCategory;

    public ModuleSettingsComponent(Module module, double x, double y, double width, double height) {
        currentCategory = Category.HUD;

        this.m = module;

        ScaledResolution sr = new ScaledResolution(mc);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        settingComponents = new ArrayList<Component>();

        double xOff = x + 4;
        double yOff = y + 44;
        int counter = 0;
    }

    @Override
    public void render(int mouseX, int mouseY) {
        update(mouseX, mouseY);

        GuiUtil.drawBorderedRect(x, y, width, height, 1, -1, 0x90000000);

        glPushMatrix();
        glScaled(2, 2, 2);
        // TextUtil.drawStringWithShadow(m.getName(), (float) (x + 5) / 2, (float) (y + 5) / 2, Colors.colourInt);
        FontManager.drawStringWithShadow(m.name, (float) x + 2, (float) y + 3, -1);
        glPopMatrix();

        RenderUtils2D.drawRect(x, y + 23, x + width, y + 24, -1);
        RenderUtils2D.drawRect(x + width - 21, y + 2, x + width - 2, y + 22, (GuiUtil.mouseOver(x + width - 21, y + 2, x + width - 2, y + 22, mouseX, mouseY) ? 0x50000000 : 0x90000000));

        glPushMatrix();
        glScaled(2, 2, 2);
        mc.fontRenderer.drawString("x", (float) (x + width - 16.5) / 2, (float) (y + 3) / 2, -1, false);
        glPopMatrix();

        for(Component c : settingComponents) {
            c.render(mouseX, mouseY);
        }

        RenderUtils2D.drawRect(x, y + 41, x + width, y + 42, -1);

        GL11.glPushMatrix();
        glPushAttrib(GL_SCISSOR_BIT);
        {
            RenderUtils2D.scissor(x, y + 44, x + width, height - 44);
            glEnable(GL_SCISSOR_TEST);
        }

        glDisable(GL_SCISSOR_TEST);
        glPopAttrib();
        GL11.glPopMatrix();

        handleMouseWheel(mouseX, mouseY);
    }

    public void update(int mouseX, int mouseY) {
        if(this.isDragging) {
            this.x = (mouseX - dragX);
            this.y = (mouseY - dragY);
            double xOff = x + 4;
            double yOff = y + 44;
            int counter = 0;
            for(Component c : settingComponents) {

            }
        }
    }

    public void setDrag(boolean drag) {
        this.isDragging = drag;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if(GuiUtil.mouseOver(x, y + 2, x + width - 22, y + 22, mouseX, mouseY) && !isDragging) {
            setDrag(true);
            dragX = mouseX - x;
            dragY = mouseY - y;
        }

        if(GuiUtil.mouseOver(x + width - 21, y + 2, x + width - 2, y + 22, mouseX, mouseY) && button == 0) {
            mc.displayGuiScreen(null);
        }

        for(Component c : settingComponents) {
            c.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        setDrag(false);
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    public void handleMouseWheel(int mouseX, int mouseY) {
        int dWheel = Mouse.getDWheel();
        /* if(dWheel < 0) {
            for(Component c : settingComponents) {
                ModuleComponent last = (ModuleComponent) settingComponents.get(settingComponents.size() - 1);

                if(GuiUtil.mouseOver(x, y + 44, x + width, y + height, mouseX, mouseY) && last.y > y + 44) {
                    if(c instanceof ModuleComponent) {
                        ((ModuleComponent) c).y -= 13;
                    }
                }
            }
        } else if(dWheel > 0) {
            for(Component c : moduleComponents) {
                if(GuiUtil.mouseOver(x, y + 44, x + width, y + height, mouseX, mouseY) && ((ModuleComponent) moduleComponents.get(0)).y < y + height - 10) {
                    ((ModuleComponent) c).y += 13;
                }
            }
        }

        // Java decided to be weird so I added this to stop an annoying bug
        try {
            if(((ModuleComponent) moduleComponents.get(0)).y > ((ModuleComponent) moduleComponents.get(1)).y) {
                ((ModuleComponent) moduleComponents.get(0)).y = ((ModuleComponent) moduleComponents.get(1)).y;
            }
        } catch (Exception e) {} */
    }
}
