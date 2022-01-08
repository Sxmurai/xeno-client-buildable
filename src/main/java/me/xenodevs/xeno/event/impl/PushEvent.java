package me.xenodevs.xeno.event.impl;

import me.xenodevs.xeno.event.Event;
import net.minecraft.entity.Entity;

public class PushEvent extends Event {

    public Entity entity;
    public double x;
    public double y;
    public double z;
    public boolean airbone;

    public PushEvent(Entity entity, double x, double y, double z, boolean airbone) {
        this.entity = entity;
        this.x = x;
        this.y = y;
        this.z = z;
        this.airbone = airbone;
    }

    public PushEvent(Entity entity) {
        this.entity = entity;
    }

}
