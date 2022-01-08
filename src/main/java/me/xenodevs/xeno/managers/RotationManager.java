package me.xenodevs.xeno.managers;

import me.wolfsurge.api.util.Globals;
import me.xenodevs.xeno.Xeno;
import me.xenodevs.xeno.event.impl.RotationEvent;
import me.xenodevs.xeno.managers.rotation.Rotation;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Comparator;
import java.util.concurrent.LinkedBlockingQueue;

public class RotationManager implements Globals {
    LinkedBlockingQueue<Rotation> rotationQueue;
    Rotation current;

    public RotationManager() {
        rotationQueue = new LinkedBlockingQueue<>();
        Xeno.EVENT_BUS.subscribe(this);
    }

    @SubscribeEvent
    public void doRotations(TickEvent.ClientTickEvent event) {
        if(nullCheck())
            return;

        rotationQueue.stream().sorted(Comparator.comparing(rotation -> rotation.priority));

        if(current != null)
            current = null; // idk

        if(!rotationQueue.isEmpty()) {
            current = rotationQueue.poll();
            current.doRotate();
        }
    }
}
