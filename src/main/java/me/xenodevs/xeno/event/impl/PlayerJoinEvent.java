package me.xenodevs.xeno.event.impl;

import me.xenodevs.xeno.event.Event;

public class PlayerJoinEvent extends Event {

	private final String name;

	public PlayerJoinEvent(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
