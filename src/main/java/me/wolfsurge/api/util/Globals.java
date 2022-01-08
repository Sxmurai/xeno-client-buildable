package me.wolfsurge.api.util;

import net.minecraft.client.Minecraft;

public interface Globals {

	Minecraft mc = Minecraft.getMinecraft();
	char SECTIONSIGN = '\u00A7';

	default boolean nullCheck() {
		return mc.world == null || mc.player == null;
	}
	
}
