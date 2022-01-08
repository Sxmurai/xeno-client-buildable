package me.xenodevs.xeno.utils.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class EntityHelper {
	public static double[] interpolate(Entity entity) {
	    double partialTicks = 
	      (Minecraft.getMinecraft()).getRenderPartialTicks();
	    double[] pos = { entity.lastTickPosX + (
	        entity.posX - entity.lastTickPosX) * partialTicks, 
	        entity.lastTickPosY + (
	        entity.posY - entity.lastTickPosY) * partialTicks, 
	        entity.lastTickPosZ + (
	        entity.posZ - entity.lastTickPosZ) * partialTicks };
	    return pos;
	  }
}
