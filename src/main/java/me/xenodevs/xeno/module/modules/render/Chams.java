package me.xenodevs.xeno.module.modules.render;

import java.awt.Color;

import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.xenodevs.xeno.module.settings.BooleanSetting;
import me.xenodevs.xeno.module.settings.ColourPicker;
import me.xenodevs.xeno.module.settings.ModeSetting;
import me.xenodevs.xeno.utils.render.Colour;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;

public class Chams extends Module {

	public static Chams INSTANCE;

	public static BooleanSetting passive = new BooleanSetting("Passive", true);
	public static BooleanSetting mobs = new BooleanSetting("Mobs", true);
	public static BooleanSetting players = new BooleanSetting("Players", true);
	public static BooleanSetting crystals = new BooleanSetting("Crystals", true);

	public static ColourPicker passiveColour = new ColourPicker("Passive Colour", new Colour(Color.GREEN));
	public static ColourPicker mobColour = new ColourPicker("Mob Colour", new Colour(Color.RED));
	public static ColourPicker playerColour = new ColourPicker("Player Colour", new Colour(Color.CYAN));
	public static ColourPicker crystalColour = new ColourPicker("Crystal Colour", new Colour(Color.PINK));

	public static ModeSetting mode = new ModeSetting("Mode", "Fill", "Wallhack");
	
	public Chams() {
		super("Chams", "shows entities through walls", 0, Category.RENDER);
		this.addSettings(mode, passive, mobs, players, crystals, passiveColour, mobColour, playerColour, crystalColour);
		INSTANCE = this;
	}

	@Override
	public void onRenderWorld() {
		if(nullCheck())
			return;

		/* if(mode.is("Wallhack")) {
			if(passive.enabled) {
				for(Object e : mc.world.loadedEntityList) {
					if(e instanceof EntityLiving && !(e instanceof EntityMob) && !(e instanceof EntityItem)) {
			            GlStateManager.clear(256);
			            RenderHelper.enableStandardItemLighting();
			            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			            
			            if (e == mc.getRenderViewEntity() && mc.gameSettings.thirdPersonView == 0) {
			              continue;
			            }
			            
			            mc.entityRenderer.disableLightmap();
			            mc.getRenderManager().renderEntityStatic((Entity)e, mc.timer.renderPartialTicks, false);
			            mc.entityRenderer.enableLightmap();
			            mc.entityRenderer.resetData();
					}
				}
			}
			
			if(mobs.enabled) {
				for(Object e : mc.world.loadedEntityList) {
					if(e instanceof EntityMob && e instanceof EntityLiving && !(e instanceof EntityItem)) {
			            GlStateManager.clear(256);
			            RenderHelper.enableStandardItemLighting();
			            GlStateManager.color(1.0F, 0.0F, 1.0F, 1.0F);
			            
			            if (e == mc.getRenderViewEntity() && mc.gameSettings.thirdPersonView == 0) {
			              continue;
			            }
			            
			            mc.entityRenderer.disableLightmap();
			            mc.getRenderManager().renderEntityStatic((Entity)e, mc.timer.renderPartialTicks, false);
			            mc.entityRenderer.enableLightmap();
					}
				}
			}
			
			if(players.enabled) {
				for(Object e : mc.world.loadedEntityList) {
					if(e instanceof EntityOtherPlayerMP && !(e instanceof EntityMob) && !(e instanceof EntityItem)) {
						if(e == mc.player)
							return;
						
			            GlStateManager.clear(256);
			            RenderHelper.enableStandardItemLighting();
			            GlStateManager.color(1.0F, 0.0F, 1.0F, 1.0F);
			            
			            if (e == mc.getRenderViewEntity() && mc.gameSettings.thirdPersonView == 0) {
			            	continue;
			            }
			            
			            mc.entityRenderer.disableLightmap();
			            mc.getRenderManager().renderEntityStatic((Entity)e, mc.timer.renderPartialTicks, false);
			            mc.entityRenderer.enableLightmap();
					}
				}
			}
		} */
		if(mode.is("Wallhack")) {
			for(Entity e : mc.world.loadedEntityList) {
				if(e instanceof EntityLiving && !(e instanceof EntityMob) && !(e instanceof EntityItem)) {
					GlStateManager.clear(256);
					RenderHelper.enableStandardItemLighting();
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

					if (e == mc.getRenderViewEntity() && mc.gameSettings.thirdPersonView == 0) {
						continue;
					}

					mc.entityRenderer.disableLightmap();
					mc.getRenderManager().renderEntityStatic((Entity)e, mc.timer.renderPartialTicks, false);
					mc.entityRenderer.enableLightmap();
					mc.entityRenderer.resetData();
				} else if(e instanceof EntityMob && e instanceof EntityLiving && !(e instanceof EntityItem)) {
					GlStateManager.clear(256);
					RenderHelper.enableStandardItemLighting();
					GlStateManager.color(1.0F, 0.0F, 1.0F, 1.0F);

					if (e == mc.getRenderViewEntity() && mc.gameSettings.thirdPersonView == 0) {
						continue;
					}

					mc.entityRenderer.disableLightmap();
					mc.getRenderManager().renderEntityStatic((Entity)e, mc.timer.renderPartialTicks, false);
					mc.entityRenderer.enableLightmap();
				} else if(players.enabled && e instanceof EntityOtherPlayerMP && !(e instanceof EntityMob) && !(e instanceof EntityItem)) {
					if(e == mc.player)
						return;

					GlStateManager.clear(256);
					RenderHelper.enableStandardItemLighting();
					GlStateManager.color(1.0F, 0.0F, 1.0F, 1.0F);

					if (e == mc.getRenderViewEntity() && mc.gameSettings.thirdPersonView == 0) {
						continue;
					}

					mc.entityRenderer.disableLightmap();
					mc.getRenderManager().renderEntityStatic((Entity)e, mc.timer.renderPartialTicks, false);
					mc.entityRenderer.enableLightmap();
				} else if(crystals.enabled && e instanceof EntityEnderCrystal) {
					if(e == mc.player)
						return;

					GlStateManager.clear(256);
					RenderHelper.enableStandardItemLighting();
					GlStateManager.color(1.0F, 0.0F, 1.0F, 1.0F);

					if (e == mc.getRenderViewEntity() && mc.gameSettings.thirdPersonView == 0) {
						continue;
					}

					mc.entityRenderer.disableLightmap();
					mc.getRenderManager().renderEntityStatic(e, mc.timer.renderPartialTicks, false);
					mc.entityRenderer.enableLightmap();
				}
			}
		}
	}
	
	@Override
	public String getHUDData() {
		return " " + this.mode.getMode();
	}
}
