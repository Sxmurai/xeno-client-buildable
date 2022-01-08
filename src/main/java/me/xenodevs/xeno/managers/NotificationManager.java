package me.xenodevs.xeno.managers;

import me.xenodevs.xeno.notifications.Notification;
import me.xenodevs.xeno.notifications.NotificationType;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;

import java.util.concurrent.LinkedBlockingQueue;

public class NotificationManager {
	Minecraft mc = Minecraft.getMinecraft();

	public LinkedBlockingQueue<Notification> notificationLinkedBlockingQueue = new LinkedBlockingQueue<>();

	public NotificationManager() {
		
	}
	
	public void call(Notification notification) {
		// mc.player.sendChatMessage(addPrefix(notification.name, notification.priority) + notification.message);
	}

	public void renderNotifications() {

	}
	
	public String addPrefix(String prefix, NotificationType type) {
		TextFormatting col = TextFormatting.GRAY;
		
		if(type == NotificationType.INFO)
			col = TextFormatting.GREEN;
		else if(type == NotificationType.WARN)
			col = TextFormatting.GOLD;
		else if(type == NotificationType.ALERT)
			col = TextFormatting.DARK_RED;
		
		return TextFormatting.GRAY + "[" + col +  prefix + TextFormatting.GRAY + "] " + TextFormatting.WHITE;
	}
	
}
