package me.xenodevs.xeno.utils.render;

import me.wolfsurge.api.util.Globals;
import me.xenodevs.xeno.managers.FontManager;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class NametagRenderer implements Globals {
    public static void drawNametag (double x, double y, double z, String[] text, Color color, int type, boolean customFont) {
        double dist=mc.player.getDistance(x,y,z);
        double scale = 1, offset = 0;
        int start=0;
        switch (type) {
            case 0:
                scale=dist/20*Math.pow(1.2589254,0.1/(dist<25?0.5:2));
                scale=Math.min(Math.max(scale,.5),5);
                offset=scale>2?scale/2:scale;
                scale/=40;
                start=10;
                break;
            case 1:
                scale=-((int)dist)/6.0;
                if (scale<1) scale=1;
                scale*=2.0/75.0;
                break;
            case 2:
                scale=0.0018+0.003*dist;
                if (dist<=8.0) scale=0.0245;
                start=-8;
                break;
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(x-mc.getRenderManager().viewerPosX,y+offset-mc.getRenderManager().viewerPosY,z-mc.getRenderManager().viewerPosZ);
        GlStateManager.rotate(-mc.getRenderManager().playerViewY,0,1,0);
        GlStateManager.rotate(mc.getRenderManager().playerViewX,mc.gameSettings.thirdPersonView==2?-1:1,0,0);
        GlStateManager.scale(-scale,-scale,scale);
        if (type == 2) {
            double width = 0;
            Colour bcolor = new Colour(0,0,0,51);
            for (int i = 0; i < text.length; i++) {
                double w = FontManager.getStringWidth(text[i]) / 2;
                if (w > width) {
                    width = w;
                }
            }
            RenderUtils2D.drawRect(-width - 1, -(customFont ? FontManager.comfortaa.FONT_HEIGHT : mc.fontRenderer.FONT_HEIGHT), width + 2, 3, new Color(0, 0, 0, 150).getRGB());
        }
        GlStateManager.enableTexture2D();
        for (int i=0;i<text.length;i++) {
            GL11.glPushMatrix();
            GL11.glTranslated(0, 0, -1);
            if(customFont)
                FontManager.comfortaa.drawStringWithShadow(text[i], -FontManager.comfortaa.getStringWidth(text[i]) / 2, i * (FontManager.comfortaa.FONT_HEIGHT) + start, color.getRGB());
            else
                mc.fontRenderer.drawStringWithShadow(text[i], -mc.fontRenderer.getStringWidth(text[i]) / 2, i * (mc.fontRenderer.FONT_HEIGHT + 5) + start + 1, color.getRGB());
            GL11.glPopMatrix();
        }
        GlStateManager.disableTexture2D();
        if (type!=2) {
            GlStateManager.popMatrix();
        }
    }

    private static void drawBorderedRect (double x, double y, double x1, double y1, float lineWidth, Colour inside, Colour border) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        inside.glColor();
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(x,y1,0).endVertex();
        bufferbuilder.pos(x1,y1,0).endVertex();
        bufferbuilder.pos(x1,y,0).endVertex();
        bufferbuilder.pos(x,y,0).endVertex();
        tessellator.draw();
        border.glColor();
        GlStateManager.glLineWidth(lineWidth);
        bufferbuilder.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(x,y,0).endVertex();
        bufferbuilder.pos(x,y1,0).endVertex();
        bufferbuilder.pos(x1,y1,0).endVertex();
        bufferbuilder.pos(x1,y,0).endVertex();
        bufferbuilder.pos(x,y,0).endVertex();
        tessellator.draw();
    }
}
