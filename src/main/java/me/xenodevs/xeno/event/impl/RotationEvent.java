package me.xenodevs.xeno.event.impl;

import me.xenodevs.xeno.event.Event;

public class RotationEvent extends Event {
    float yaw;
    float pitch;

    public RotationEvent() {}

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}
