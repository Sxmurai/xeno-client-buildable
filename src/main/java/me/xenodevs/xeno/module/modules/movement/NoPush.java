package me.xenodevs.xeno.module.modules.movement;

import me.xenodevs.xeno.event.impl.PushEvent;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

public class NoPush extends Module {

    public NoPush() {
        super("NoPush", "stops entities from pushing you around", Category.MOVEMENT);
    }

    @EventHandler
    private final Listener<PushEvent> pushEventListener = new Listener<>(event -> {
        if(event.entity == mc.player)
            event.cancel();
    });

}
