package me.xenodevs.xeno.gui.click;

import java.io.IOException;
import java.util.ArrayList;

import me.xenodevs.xeno.gui.GuiUtil;
import me.xenodevs.xeno.gui.click.component.components.subcomponents.KeybindComponent;
import org.lwjgl.input.Mouse;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.click.component.*;
import me.xenodevs.xeno.gui.click.component.components.Button;
import me.xenodevs.xeno.gui.click.theme.Theme;
import me.xenodevs.xeno.gui.click.theme.themes.*;
import me.xenodevs.xeno.module.*;
import me.xenodevs.xeno.module.modules.client.*;
import me.xenodevs.xeno.utils.other.Timer;
import me.xenodevs.xeno.utils.render.*;
import net.minecraft.client.gui.*;

public class ClickGui extends GuiScreen {

    public static ArrayList<Frame> frames;
    public static int color = ClickGuiVariables.clickGuiColor;
    public static int frameX;
    public static Theme theme;
    Timer timer = new Timer();
    public boolean isDraggingFrame = false;
    public static int maxLength = ClickGuiVariables.buttonBarHeight * 15;

    public ClickGui() {
        this.themeSwitch();

        this.frames = new ArrayList<>();
        int x = (DisplayUtils.getDisplayWidth() / 2) - ((Category.values().length * (ClickGuiVariables.frameWidth + 6)) / 2);

        frames.clear();

        for(Category category : Category.values()) {
            Frame frame = new Frame(category);
            frames.add(frame);
        }

        for(Frame f : frames) {
            f.setX(x);
            f.setY(20);
            x += ClickGuiVariables.frameWidth + 6;
            f.setOpen(true);
            f.refresh();
        }

        Xeno.logger.info("Created ClickGUI");
    }

    public static void reset() {
        int x = (DisplayUtils.getDisplayWidth() / 2) - ((Category.values().length * (ClickGuiVariables.frameWidth + 6)) / 2);

        frames = new ArrayList<>();
        for(Category category : Category.values()) {
            Frame frame = new Frame(category);
            frames.add(frame);
        }

        for(Frame f : frames) {
            f.setX(x);
            f.setY(20);
            x += ClickGuiVariables.frameWidth + 6;
            f.setOpen(true);
        }
    }

    @Override
    public void initGui() {
        for(Frame f : frames)
            f.refresh();
    }

    public static void themeSwitch() {
        theme = ClickGuiModule.getTheme();

        ClickGuiModule.resetClickGUI.onClick();
    }

    @Override
    public void onGuiClosed() {
        for(Module m : Xeno.moduleManager.modules)
            Xeno.config.saveModConfig(m);

        Xeno.config.saveClickGUIConfig();

        super.onGuiClosed();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if(ClickGuiModule.darken.isEnabled())
            this.drawDefaultBackground();

        for(Frame f : frames)
            f.refresh();

        this.color = Colors.colourInt;

        theme.drawScreen();

        String descText = "";
        boolean renderDesc = false;

        for(Frame frame : frames) {
            frame.renderFrame(this.fontRenderer, mouseX, mouseY);
            frame.updatePosition(mouseX, mouseY);
            for(Component comp : frame.getComponents()) {
                comp.updateComponent(mouseX, mouseY);

                if(comp instanceof Button && (ClickGui.theme.isScrollable ? mouseOver(frame.getX() - 2, frame.getY() + 3 + ClickGuiVariables.frameBarHeight, frame.getX() + ClickGuiVariables.frameWidth + 2, frame.getY() + 3 + ClickGuiVariables.frameBarHeight + (maxLength), mouseX, mouseY) : true)) {
                    if(((Button) comp).isMouseOnButton(mouseX, mouseY)) {
                        descText = ((Button) comp).mod.description;
                        renderDesc = ClickGuiModule.desc.isEnabled() && frame.isOpen() && (ClickGui.theme instanceof XenoTheme ? frame.animateXenoTheme.getValue() == frame.animateXenoTheme.getMax() : true)
                        && (ClickGui.theme instanceof PlainTheme ? frame.isOpen() && ClickGui.theme instanceof PlainTheme && frame.animateModuleLength.getValue() == frame.animateModuleLength.getMax() : true);
                    }
                }
            }

            if(ClickGuiVariables.limitFrameXandYToDisplaySides) {
                if(frame.getX() - 2 < 0)
                    frame.setX(2);

                if(frame.getX() > DisplayUtils.getDisplayWidth() - frame.getWidth())
                    frame.setX(DisplayUtils.getDisplayWidth() - frame.getWidth());

                if(frame.getY() > DisplayUtils.getDisplayHeight() - frame.getBarHeight())
                    frame.setY(DisplayUtils.getDisplayHeight() - frame.getBarHeight());
            }
        }

        if(renderDesc) {
            theme.drawDescription(descText, timer, mouseX, mouseY);
        }

        checkMouseWheel(mouseX, mouseY);
        overlay();
        GuiUtil.renderButtons(mouseX, mouseY);
    }

    public void overlay() {

    }

    public boolean mouseOver(int minX, int minY, int maxX, int maxY, int mX, int mY) {
        return mX >= minX && mY >= minY && mX <= maxX && mY <= maxY;
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        for(Frame frame : frames) {
            if(frame.isWithinHeader(mouseX, mouseY) && mouseButton == 0 && !isDraggingFrame) {
                frame.setDrag(true);
                frame.dragX = mouseX - frame.getX();
                frame.dragY = mouseY - frame.getY();
                isDraggingFrame = true;
            }
            if(frame.isWithinHeader(mouseX, mouseY) && mouseButton == 1) {
                if(ClickGuiModule.clickSound.enabled)
                    GuiUtil.clickSound();

                frame.setOpen(!frame.isOpen());
            }

            if(ClickGui.theme instanceof XenoTheme && frame.isOpen() && frame.animateXenoTheme.getValue() > 0 &&
                    mouseOver(frame.getX() - 2, frame.getY() + 4 + ClickGuiVariables.frameBarHeight, frame.getX() + ClickGuiVariables.frameWidth + 2, frame.getY() + 4 + ClickGuiVariables.frameBarHeight + (maxLength), mouseX, mouseY)
                    && frame.animateXenoTheme.getValue() == frame.animateXenoTheme.getMax()) {
                if(!frame.getComponents().isEmpty()) {
                    for(Component component : frame.getComponents()) {
                        component.mouseClicked(mouseX, mouseY, mouseButton);
                    }
                }
            } else if(frame.isOpen() && ClickGui.theme instanceof PlainTheme && frame.animateModuleLength.getValue() == frame.animateModuleLength.getMax()) {
                if(!frame.getComponents().isEmpty()) {
                    for(Component component : frame.getComponents()) {
                        component.mouseClicked(mouseX, mouseY, mouseButton);
                    }
                }
            } else if(ClickGui.theme instanceof FutureTheme && frame.isOpen()) {
                if(!frame.getComponents().isEmpty()) {
                    for(Component component : frame.getComponents()) {
                        component.mouseClicked(mouseX, mouseY, mouseButton);
                    }
                }
            }
        }

        if(mouseButton == 0 || mouseButton == 1) {
            GuiUtil.handleButtons(mouseX, mouseY);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        for(Frame frame : frames) {
            if(frame.isOpen() && keyCode != 1) {
                for(Component component : frame.getComponents()) {
                    component.keyTyped(typedChar, keyCode);
                }
            }
        }
        if (keyCode == 1) {
            this.mc.displayGuiScreen(null);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if(!Mouse.isButtonDown(0)) {
            for(Frame frame : frames) {
                frame.setDrag(false);
            }
            isDraggingFrame = false;
        }

        for(Frame frame : frames) {
            if(frame.isOpen()) {
                if(!frame.getComponents().isEmpty()) {
                    for(Component component : frame.getComponents()) {
                        component.mouseReleased(mouseX, mouseY, state);
                    }
                }
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public void checkMouseWheel(int mouseX, int mouseY) {
        if(!ClickGui.theme.isScrollable) {
            int dWheel = Mouse.getDWheel();
            if (dWheel < 0) {
                for (Frame f : frames) {
                    f.setY((f.getY() + (f.getBarHeight() / 2)));
                }
            } else if (dWheel > 0) {
                for (Frame f : frames) {
                    f.setY((f.getY() - (f.getBarHeight() / 2)));
                }
            }
        } else {
            // Scrollable Frames
            for(Frame f : frames) {
                Button first = (Button) f.getComponents().get(0);
                if(first.offset > maxLength) {
                    first.offset = maxLength;
                    f.refresh();
                    return;
                }
            }

            int dWheel = Mouse.getDWheel();
            if (dWheel < 0) {
                for(Frame f : frames) {
                    if (f.isOpen() && mouseOver(f.getX() - 2, f.getY() + ClickGuiVariables.frameBarHeight + 3, f.getX() + ClickGuiVariables.frameWidth + 2, f.getY() + ClickGuiVariables.frameBarHeight + 3 + (maxLength), mouseX, mouseY)) {
                        for (Component c : f.components) {
                            if (c instanceof Button) {
                                Button button = (Button) c;
                                if(!(((Button)f.getComponents().get(0)).offset + 0.5 > ClickGuiVariables.frameBarHeight + 2.5))
                                    button.offset += ClickGuiVariables.buttonBarHeight / 2;
                            }
                        }
                    }
                }
            }

            for(Frame f : frames) {
                Button last = (Button) f.getComponents().get(f.components.size() - 1);
                if (!last.open) {
                    if (last.offset < ClickGuiVariables.frameBarHeight + 3) {
                        last.offset = ClickGuiVariables.frameBarHeight + 3 + ClickGuiVariables.buttonBarHeight;
                        f.refresh();
                        return;
                    }
                } else {
                    KeybindComponent lastSetting = (KeybindComponent) last.getSubcomponents().get(last.getSubcomponents().size() - 1); // The last component is always a keybind

                    if (lastSetting.offset < ClickGuiVariables.frameBarHeight + 3) {
                        lastSetting.offset = ClickGuiVariables.frameBarHeight + 3 + ClickGuiVariables.buttonBarHeight;
                        f.refresh();
                        return;
                    }
                }
            }

            if (dWheel > 0) {
                for (Frame f : frames) {
                    if (f.isOpen() && mouseOver(f.getX() - 2, f.getY() + ClickGuiVariables.frameBarHeight + 3, f.getX() + ClickGuiVariables.frameWidth + 2, f.getY() + ClickGuiVariables.frameBarHeight + 3 + (maxLength), mouseX, mouseY)) {
                        for (Component c : f.components) {
                            if (c instanceof Button) {
                                Button button = (Button) c;
                                button.offset -= ClickGuiVariables.buttonBarHeight / 2;
                            }
                        }
                    }
                }
            }
        }
    }
}
