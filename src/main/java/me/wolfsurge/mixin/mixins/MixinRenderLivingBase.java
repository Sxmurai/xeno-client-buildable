package me.wolfsurge.mixin.mixins;

import java.awt.Color;

import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.module.modules.render.Chams;
import me.xenodevs.xeno.module.modules.render.ESP;
import me.xenodevs.xeno.utils.render.OutlineUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;

import static me.xenodevs.xeno.module.modules.render.ESP.invisibles;

@Mixin(RenderLivingBase.class)
public abstract class MixinRenderLivingBase<T extends EntityLivingBase> extends Render<T> {
	
	@Shadow
    protected ModelBase mainModel;
	
	protected MixinRenderLivingBase(RenderManager renderManager) {
		super(null);
	}

	@Inject(method = "renderModel", at = @At("HEAD"))
    protected void renderModel(T entitylivingbaseIn, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float scaleFactor, final CallbackInfo g) {
        boolean flag = !entitylivingbaseIn.isInvisible();
        boolean flag1 = !flag && !entitylivingbaseIn.isInvisibleToPlayer(Minecraft.getMinecraft().player);

        if (flag || flag1) {
            if (!bindEntityTexture(entitylivingbaseIn)) {
                return;
            }

            if (flag1) {
                GlStateManager.pushMatrix();
                GlStateManager.color(1.0F, 1.0F, 1.0F, 0.15F);
                GlStateManager.depthMask(false);
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(770, 771);
                GlStateManager.alphaFunc(516, 0.003921569F);
            }
            
            Color passivec = Chams.passiveColour.getColor();
            Color mobc = Chams.mobColour.getColor();
            Color playerc = Chams.playerColour.getColor();

			if (Xeno.moduleManager.isModuleEnabled("Chams") && Chams.mode.is("Fill")) {
				if (entitylivingbaseIn instanceof net.minecraft.entity.EntityLiving && !(entitylivingbaseIn instanceof net.minecraft.entity.monster.EntityMob) && Chams.passive.enabled) {
					GL11.glPushAttrib(1048575);
					GL11.glDisable(3008);
					GL11.glDisable(3553);
					GL11.glDisable(2896);
					GL11.glEnable(3042);
					GL11.glBlendFunc(770, 771);
					GL11.glLineWidth(1.5F);
					GL11.glEnable(2960);
					GL11.glDisable(2929);
					GL11.glDepthMask(false);
					GL11.glEnable(10754);
					OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
					GL11.glColor3f(passivec.getRed() / 255.0F, passivec.getGreen() / 255.0F, passivec.getBlue() / 255.0F);
					this.mainModel.render((Entity)entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
					GL11.glEnable(2929);
					GL11.glDepthMask(true);
					this.mainModel.render((Entity)entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
					GL11.glEnable(3042);
					GL11.glEnable(2896);
					GL11.glEnable(3553);
					GL11.glEnable(3008);
					GL11.glPopAttrib();
				}
				if (entitylivingbaseIn instanceof net.minecraft.entity.monster.EntityMob && Chams.mobs.enabled) {
					GL11.glPushAttrib(1048575);
					GL11.glDisable(3008);
					GL11.glDisable(3553);
					GL11.glDisable(2896);
					GL11.glEnable(3042);
					GL11.glBlendFunc(770, 771);
					GL11.glLineWidth(1.5F);
					GL11.glEnable(2960);
					GL11.glDisable(2929);
					GL11.glDepthMask(false);
					GL11.glEnable(10754);
					OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
					GL11.glColor3f(mobc.getRed() / 255.0F, mobc.getGreen() / 255.0F, mobc.getBlue() / 255.0F);
					this.mainModel.render((Entity)entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
					GL11.glEnable(2929);
					GL11.glDepthMask(true);
					this.mainModel.render((Entity)entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
					GL11.glEnable(3042);
					GL11.glEnable(2896);
					GL11.glEnable(3553);
					GL11.glEnable(3008);
					GL11.glPopAttrib();
				}
				if (entitylivingbaseIn instanceof EntityPlayer && Chams.players.enabled) {
					GL11.glPushAttrib(1048575);
					GL11.glDisable(3008);
					GL11.glDisable(3553);
					GL11.glDisable(2896);
					GL11.glEnable(3042);
					GL11.glBlendFunc(770, 771);
					GL11.glLineWidth(1.5F);
					GL11.glEnable(2960);
					GL11.glDisable(2929);
					GL11.glDepthMask(false);
					GL11.glEnable(10754);
					OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
					GL11.glColor3f(playerc.getRed() / 255.0F, playerc.getGreen() / 255.0F, playerc.getBlue() / 255.0F);
					this.mainModel.render((Entity)entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
					GL11.glEnable(2929);
					GL11.glDepthMask(true);
					this.mainModel.render((Entity)entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
					GL11.glEnable(3042);
					GL11.glEnable(2896);
					GL11.glEnable(3553);
					GL11.glEnable(3008);
					GL11.glPopAttrib();
				}
			} else {
				this.mainModel.render((Entity)entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
			}
            
            if (Xeno.moduleManager.getModule("ESP") != null && Xeno.moduleManager.getModule("ESP").isEnabled() && ESP.mode.is("Outline")) {
				if(entitylivingbaseIn.isInvisible() && !invisibles.enabled)
					return;

                if (entitylivingbaseIn instanceof EntityPlayer && entitylivingbaseIn != Minecraft.getMinecraft().player && ESP.players.enabled) {
                    Color n = ESP.playerColour.getColor();
                    OutlineUtils.setColor(n);
                    mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    OutlineUtils.renderOne((float) ESP.lineWidth.getDoubleValue());
                    mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    OutlineUtils.renderTwo();
                    mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    OutlineUtils.renderThree();
                    OutlineUtils.renderFour(n);
                    OutlineUtils.setColor(n);
                    mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    OutlineUtils.renderFive();
                } else if(entitylivingbaseIn instanceof EntityLiving && !(entitylivingbaseIn instanceof EntityMob) && ESP.passive.enabled) {
                	Color n = ESP.passiveColour.getColor();
                    OutlineUtils.setColor(n);
                    mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    OutlineUtils.renderOne((float) ESP.lineWidth.getDoubleValue());
                    mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    OutlineUtils.renderTwo();
                    mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    OutlineUtils.renderThree();
                    OutlineUtils.renderFour(n);
                    OutlineUtils.setColor(n);
                    mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    OutlineUtils.renderFive();
                } else if(entitylivingbaseIn instanceof EntityMob && ESP.mobs.enabled) {
                	Color n = ESP.mobColour.getColor();
                    OutlineUtils.setColor(n);
                    mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    OutlineUtils.renderOne((float) ESP.lineWidth.getDoubleValue());
                    mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    OutlineUtils.renderTwo();
                    mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    OutlineUtils.renderThree();
                    OutlineUtils.renderFour(n);
                    OutlineUtils.setColor(n);
                    mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    OutlineUtils.renderFive();
                }
                
                OutlineUtils.setColor(Color.WHITE);
            }
            
            this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);

            if (flag1) {
                GlStateManager.disableBlend();
                GlStateManager.alphaFunc(516, 0.1F);
                GlStateManager.popMatrix();
                GlStateManager.depthMask(true);
            }
        }
    }
}