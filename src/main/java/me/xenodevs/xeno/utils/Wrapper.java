package me.xenodevs.xeno.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;

public class Wrapper {

	static Minecraft mc = Minecraft.getMinecraft();
	
	public static EntityPlayerSP getPlayer() {
		return mc.player;
	}
	
	public static WorldClient getWorld() {
		return mc.world;
	}
	
	public static NetHandlerPlayClient getConnection() {
		return mc.getConnection();
	}
	
}
