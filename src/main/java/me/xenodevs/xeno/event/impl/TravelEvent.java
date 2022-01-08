package me.xenodevs.xeno.event.impl;

import me.xenodevs.xeno.event.Event;

public class TravelEvent extends Event {

	private float strafe;
    private float vertical;
    private float forward;

    public TravelEvent(float strafe, float vertical, float forward) {
        this.strafe = strafe;
        this.vertical = vertical;
        this.forward = forward;
    }

    public float getStrafe() {
        return this.strafe;
    }

    public float getVertical() {
        return this.vertical;
    }

    public float getForward() {
        return this.forward;
    }
	
}
