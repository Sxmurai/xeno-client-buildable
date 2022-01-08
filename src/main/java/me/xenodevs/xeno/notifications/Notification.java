package me.xenodevs.xeno.notifications;

public class Notification {
	
	public String name, message;
	public NotificationType priority;
	
	public Notification(String name, String message, NotificationType priority) {
		this.name = name;
		this.message = message;
		this.priority = priority;
	}

}
