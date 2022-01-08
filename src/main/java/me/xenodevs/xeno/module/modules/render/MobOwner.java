package me.xenodevs.xeno.module.modules.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.managers.FontManager;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ColourPicker;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.module.settings.NumberSetting;
import me.xenodevs.xeno.utils.entity.EntityUtil;
import me.xenodevs.xeno.utils.render.Colour;
import me.xenodevs.xeno.utils.render.GLUtil;
import me.xenodevs.xeno.utils.render.RenderUtils2D;
import me.xenodevs.xeno.utils.render.RenderUtils3D;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class MobOwner extends Module {

    public static BooleanSetting customFont = new BooleanSetting("Custom Font", true);
    public static ModeSetting outlineWhere = new ModeSetting("Outline", "Top", "Left", "Right", "Bottom", "All");
    public static NumberSetting outlineWidth = new NumberSetting("Width", 1, 0, 1.5, 0.05);
    public static ColourPicker outlineColour = new ColourPicker("Outline Colour", new Colour(Colour.RED), true);
    public static ColourPicker fontColour = new ColourPicker("Font Colour", Colour.WHITE);

    public MobOwner() {
        super("MobOwner", "shows who tamed mobs belong to", Category.RENDER);
        this.addSettings(customFont, outlineWhere, outlineWidth, outlineColour, fontColour);
    }

    @Override
    public void onRenderWorld() {
        for(Entity e : mc.world.loadedEntityList) {
            if(e instanceof EntityTameable) {
                Vec3d vec = EntityUtil.getInterpolatedPos(e, mc.getRenderPartialTicks());
                String text = (((EntityTameable) e).getOwner() == null ? "No Owner" : "[Owner] " + ((EntityTameable) e).getOwner().getName());

                GlStateManager.pushMatrix();
                RenderUtils3D.glBillboardDistanceScaled((float) vec.x, (float) vec.y + 1, (float) vec.z, mc.player, 1.0f);
                GlStateManager.disableDepth();

                float stringWidth = (customFont.isEnabled() ? FontManager.comfortaa.getStringWidth(text) : mc.fontRenderer.getStringWidth(text));

                GlStateManager.translate(-(stringWidth / 2.0), 0.0, 0.0);

                double x = 1;
                double width = x + stringWidth + 2;

                RenderUtils2D.drawRect(x, 1, width, 15 + (customFont.isEnabled() && !text.equalsIgnoreCase("No Owner") ? 2 : 0), 0x90000000);
                if(customFont.isEnabled())
                    FontManager.comfortaa.drawStringWithShadow(text, 2, 4, fontColour.getColor().getRGB());
                else
                    mc.fontRenderer.drawStringWithShadow(text, 2, 4, fontColour.getColor().getRGB());

                int height = (customFont.isEnabled() ? FontManager.comfortaa.getHeight() : mc.fontRenderer.FONT_HEIGHT) + 2;

                int outlineCol = outlineColour.getColor().getRGB();

                if(outlineWhere.is("Top"))
                    RenderUtils2D.drawRect(x, 1 - outlineWidth.getDoubleValue(), width, 1, outlineCol);
                else if(outlineWhere.is("Left"))
                    RenderUtils2D.drawRect(x - outlineWidth.getDoubleValue(), 1, x, 15, outlineCol);
                else if(outlineWhere.is("Right"))
                    RenderUtils2D.drawRect(x - 1 + width, 1, x - 1 + width + outlineWidth.getDoubleValue(), 15, outlineCol);
                else if(outlineWhere.is("Bottom"))
                    RenderUtils2D.drawRect(x, 15, width, 15 + outlineWidth.getDoubleValue(), outlineCol);
                else if(outlineWhere.is("All")) {
                    RenderUtils2D.drawRect(x, 1 - outlineWidth.getDoubleValue(), width, 1, outlineCol);
                    RenderUtils2D.drawRect(x - outlineWidth.getDoubleValue(), 1 - outlineWidth.getDoubleValue(), x, 15 + outlineWidth.getDoubleValue(), outlineCol);
                    RenderUtils2D.drawRect(x - 1 + width, 1 - outlineWidth.getDoubleValue(), x - 1 + width + outlineWidth.getDoubleValue(), 15 + outlineWidth.getDoubleValue(), outlineCol);
                    RenderUtils2D.drawRect(x, 15, width, 15 + outlineWidth.getDoubleValue(), outlineCol);
                }

                GlStateManager.enableDepth();
                GlStateManager.popMatrix();
            }
        }
    }
}