package me.xenodevs.xeno.gui.mainmenu.custom;

import com.mojang.realmsclient.gui.ChatFormatting;
import fr.lavache.anime.Animate;
import fr.lavache.anime.Easing;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.gui.GuiUtil;
import me.xenodevs.xeno.managers.FontManager;
import me.xenodevs.xeno.utils.render.ColorUtil;
import me.xenodevs.xeno.utils.render.RenderUtils2D;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class XenoMainMenu extends GuiScreen {

    Animate anim = new Animate();
    Animate initAnim = new Animate();

    @Override
    public void initGui() {
        anim.setEase(Easing.EXPO_IN_OUT).setMin(0).setMax(210).setSpeed(75).setReversed(false).setValue(0);
        initAnim.setEase(Easing.EXPO_IN_OUT).setMin(0).setMax(300).setSpeed(300).setReversed(false).setValue(0).reset();
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Xeno.discordManager.update("In Main Menu");
        ScaledResolution sr = new ScaledResolution(mc);

        int minX = sr.getScaledWidth() / 2 - (FontManager.aquireBig.getStringWidth("Xeno Client")) - 5;
        int minY = sr.getScaledHeight() / 2 - 55;
        int maxX = minX + (FontManager.aquireBig.getStringWidth("Xeno Client") * 2) + 5;
        int maxY = minY + 40;

        anim.update();
        initAnim.update();

        if(!GuiUtil.mouseOver(minX, minY, maxX, maxY, mouseX, mouseY))
            anim.setReversed(true);
        else
            anim.setReversed(false);

        ResourceLocation path = new ResourceLocation("xeno", "textures/background1.png");
        mc.getTextureManager().bindTexture(path);
        RenderUtils2D.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, this.width, this.height, this.width, this.height);

        Xeno.blurManager.blur(5);

        RenderUtils2D.drawRoundedRect(0, 0, 30, 15, 1, 1, 5, 1, 0x90000000);
        FontManager.comfortaa.drawStringWithShadow("MC", 3, 3, GuiUtil.mouseOver(0, 0, 30, 15, mouseX, mouseY) ? ColorUtil.rainbowWave(4, 1, 1, 0) : -1);

        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_SCISSOR_BIT);
        {
            RenderUtils2D.scissor(sr.getScaledWidth() / 2 - 600, sr.getScaledHeight() / 2 - 50, 1000, 300);
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
        }

        GL11.glPushMatrix();
        GlStateManager.scale(2, 2, 0);
        drawCenteredString(FontManager.aquireBig, ChatFormatting.UNDERLINE + "Xeno Client", sr.getScaledWidth() / 4, sr.getScaledHeight() / 4 - 25, GuiUtil.mouseOver(minX, minY, maxX, maxY, mouseX, mouseY) ? ColorUtil.rainbowWave(6, 0.5f, 1, 0) : -1);
        GL11.glPopMatrix();

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopAttrib();
        GL11.glPopMatrix();

        ArrayList<String> buttons = new ArrayList<>();
        buttons.add("Singleplayer");
        buttons.add("Multiplayer");
        buttons.add("Settings");
        buttons.add("Quit");

        int totalStringLength = 0;
        for(String s : buttons) {
            totalStringLength += FontManager.comfortaaBig.getStringWidth(s) + 20;
        }

        int x = sr.getScaledWidth() / 2 - (totalStringLength / 2);
        for(String str : buttons) {
            FontManager.comfortaaBig.drawStringWithShadow(str, x, sr.getScaledHeight() / 2, (GuiUtil.mouseOver(x, sr.getScaledHeight() / 2, x + FontManager.comfortaaBig.getStringWidth(str) + 19, sr.getScaledHeight() / 2 + FontManager.comfortaaBig.getHeight(), mouseX, mouseY) ? ColorUtil.rainbowWave(4, 1, 1, 0) : -1));
            x += FontManager.comfortaaBig.getStringWidth(str) + 20;
        }

        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_SCISSOR_BIT);
        {
            RenderUtils2D.scissor(sr.getScaledWidth() / 2 - 100, sr.getScaledHeight() / 2 - 105, anim.getValue(), 47);
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
        }
        RenderUtils2D.drawRoundedRect(sr.getScaledWidth() / 2 - 100, sr.getScaledHeight() / 2 - 105, 200, 45, 5, 0x90000000);

        FontManager.comfortaa.drawRainbowStringWithShadow("Authors", sr.getScaledWidth() / 2 - 95, sr.getScaledHeight() / 2 - 100, -1, 4, 1, 1);
        FontManager.comfortaa.drawStringWithShadow("Wolfsurge, Mathew101Q", sr.getScaledWidth() / 2 - 95, sr.getScaledHeight() / 2 - 86, -1);
        FontManager.comfortaa.drawStringWithShadow("SoldierMC, Fyre", sr.getScaledWidth() / 2 - 95, sr.getScaledHeight() / 2 - 72, -1);

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopAttrib();
        GL11.glPopMatrix();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        ScaledResolution sr = new ScaledResolution(mc);

        ArrayList<String> buttons = new ArrayList<>();
        buttons.add("Singleplayer");
        buttons.add("Multiplayer");
        buttons.add("Settings");
        buttons.add("Quit");

        int totalStringLength = 0;
        for(String s : buttons) {
            totalStringLength += FontManager.comfortaaBig.getStringWidth(s) + 20;
        }

        if(mouseButton == 0) {
            int x = sr.getScaledWidth() / 2 - (totalStringLength / 2);
            for(String str : buttons) {
                if(GuiUtil.mouseOver(x, sr.getScaledHeight() / 2, x + FontManager.comfortaaBig.getStringWidth(str) + 20, sr.getScaledHeight() / 2 + FontManager.comfortaaBig.getHeight(), mouseX, mouseY)) {
                    if(str == "Singleplayer")
                        mc.displayGuiScreen(new GuiWorldSelection(this));
                    else if(str == "Multiplayer")
                        mc.displayGuiScreen(new GuiMultiplayer(this));
                    else if(str == "Settings")
                        mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
                    else if(str == "Quit")
                        mc.shutdown();
                }
                x += FontManager.comfortaaBig.getStringWidth(str) + 19;
            }

            int minX = sr.getScaledWidth() / 2 - (FontManager.comfortaaBig.getStringWidth("Xeno Client")) - 5;
            int minY = sr.getScaledHeight() / 2 - 55;
            int maxX = minX + (FontManager.comfortaaBig.getStringWidth("Xeno Client") * 2) + 5;
            int maxY = minY + 40;
            if(GuiUtil.mouseOver(minX, minY, maxX, maxY, mouseX, mouseY) && mouseButton == 0) {
                Desktop d = Desktop.getDesktop();
                try {
                    d.browse(new URI("https://discord.gg/YPeVBdZMQA"));
                } catch (URISyntaxException e) {}
            }

            if(GuiUtil.mouseOver(0, 0, 30, 15, mouseX, mouseY)) {
                mc.displayGuiScreen(new GuiMainMenu());
                GuiUtil.customMainMenu = false;
                Xeno.config.saveMisc();
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {}
}
