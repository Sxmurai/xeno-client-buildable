package me.xenodevs.xeno.utils.other;

import me.xenodevs.xeno.Xeno;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

public class ChatUtil {
	
	static Minecraft mc = Minecraft.getMinecraft();
	
	public static void addChatMessage(String message) {
		message = "\2479" + Xeno.NAME + "\247  > " + message;
		
		Minecraft.getMinecraft().player.sendMessage(new TextComponentString(message));
	}

}
