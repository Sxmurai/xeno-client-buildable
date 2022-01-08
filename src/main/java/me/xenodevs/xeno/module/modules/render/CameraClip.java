package me.xenodevs.xeno.module.modules.render;

import me.xenodevs.xeno.event.impl.CameraClipEvent;
import me.xenodevs.xeno.module.Category;
import me.xenodevs.xeno.module.Module;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;

public class CameraClip extends Module {

    public CameraClip() {
        super("CameraClip", "lets the 3rd person camera clip through blocks", Category.RENDER);
    }

    @EventHandler
    private final Listener<CameraClipEvent> listener = new Listener<>(event -> {
        event.cancel();
    });

}
